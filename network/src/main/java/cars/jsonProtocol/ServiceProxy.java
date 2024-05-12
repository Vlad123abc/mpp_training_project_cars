package cars.jsonProtocol;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.User;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IService {
    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Closeable connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceProxy(Closeable connection, BufferedReader input, PrintWriter output) {
        this.connection = connection;
        this.input = input;
        this.output = output;

        this.gsonFormatter = new Gson();

        this.qresponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() {
        this.output.flush();
        this.finished = false;

        startReader();
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    public void closeConnection() {
        this.finished = true;
        System.out.println("finished is " + finished);
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws Exception {
        String reqLine = gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new Exception("Error sending object " + e);
        }
    }

    private Response readResponse() throws Exception {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void login(String username, String password, IObserver client) throws Exception {
        initializeConnection();

        Request req = new Request.Builder().setType(RequestType.LOGIN).setData(new User(username, password)).build();
        System.out.println("Sending Login Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived Login Response: " + response.toString());
        if (response.getType() == ResponseType.OK) {
            System.out.println("Login OK");
            this.client = client;
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        Request req = new Request.Builder().setType(RequestType.LOGOUT).setData(user).build();
        System.out.println("Sending Logout Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived Logout Response: " + response.toString());
        this.closeConnection();
        if (response.getType() == ResponseType.OK) {
            System.out.println("Logout OK");
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            throw new Exception(err);
        }
    }

    @Override
    public List<Car> getAllCars() throws Exception {
        Request req = new Request.Builder().setType(RequestType.GET_ALL_CARS).build();

        System.out.println("Sending getAllCars Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived getAllCars Response: " + response.toString());

        if (response.getType() == ResponseType.ERROR) {
            closeConnection();
            String err = response.getData().toString();
            throw new Exception(err);
        }

        List<Car> carList = new ArrayList<>();
        if (response.getData() == null)
            return carList;
        var list = gsonFormatter.fromJson(response.getData().toString(), carList.getClass());
        for (var car : list) {
            Car c = gsonFormatter.fromJson(car.toString(), Car.class);
            carList.add(c);
        }
        return carList;
    }

    @Override
    public List<cars.Car> getAllCarsBrand(String brand) throws Exception {
        Request req = new Request.Builder().setType(RequestType.GET_ALL_CARS_BRAND).setData(brand).build();

        System.out.println("Sending getAllCarsBrand Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived getAllCarsBrand Response: " + response.toString());

        if (response.getType() == ResponseType.ERROR) {
            closeConnection();
            String err = response.getData().toString();
            throw new Exception(err);
        }

        List<Car> carList = new ArrayList<>();
        if (response.getData() == null)
            return carList;
        var list = gsonFormatter.fromJson(response.getData().toString(), carList.getClass());
        for (var car : list) {
            Car c = gsonFormatter.fromJson(car.toString(), Car.class);
            carList.add(c);
        }
        return carList;
    }

    @Override
    public void saveCar(String brand, Integer hp) throws Exception {
        Request req = new Request.Builder().setType(RequestType.SAVE_CAR).setData(new Car(brand, hp)).build();

        System.out.println("Sending saveCar Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived saveCar Response: " + response.toString());

        if (response.getType() == ResponseType.OK) {
            System.out.println("saveCar OK");
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void deleteCar(Long id) throws Exception {
        Request req = new Request.Builder().setType(RequestType.DELETE_CAR).setData(id).build();

        System.out.println("Sending deleteCar Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived deleteCar Response: " + response.toString());

        if (response.getType() == ResponseType.OK) {
            System.out.println("deleteCar OK");
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            closeConnection();
            throw new Exception(err);
        }
    }

    private void handleUpdate(Response response) {
        if (response.getType() == ResponseType.SAVE_CAR) {
            Car car = gsonFormatter.fromJson(response.getData().toString(), Car.class);
            // Car car = (Car) response.getData();
            System.out.println("The car: " + car);
            try {
                client.carSaved(car);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.DELETE_CAR) {
            Long id = gsonFormatter.fromJson(response.getData().toString(), Long.class);
            // Long id = (Long) response.getData();
            System.out.println("The id of the car: " + id);
            try {
                client.carDeleted(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.SAVE_CAR || response.getType() == ResponseType.DELETE_CAR;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                System.out.println("finished is " + finished);
                try {
                    String responseLine = input.readLine();
                    System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (response != null) {
                        if (isUpdate(response)) {
                            handleUpdate(response);
                        } else {
                            try {
                                qresponses.put(response);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error, going to close, error is: " + e);
                    finished = true;
                }
            }
        }
    }
}
