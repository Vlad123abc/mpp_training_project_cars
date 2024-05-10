package cars.jsonProtocol;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IService
{
    private String host;
    private int port;

    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceProxy(String host, int port)
    {
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws Exception
    {
        try
        {
            gsonFormatter = new Gson();
            connection = new Socket(host,port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private void startReader()
    {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    private void closeConnection()
    {
        finished = true;
        try
        {
            input.close();
            output.close();
            connection.close();
            client = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws Exception
    {
        String reqLine = gsonFormatter.toJson(request);
        try
        {
            output.println(reqLine);
            output.flush();
        }
        catch (Exception e)
        {
            throw new Exception("Error sending object " + e);
        }
    }

    private Response readResponse() throws Exception
    {
        Response response = null;
        try
        {
            response = qresponses.take();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void login(String username, String password, IObserver client) throws Exception
    {
        initializeConnection();

        Request req = new Request.Builder().setType(RequestType.LOGIN).setData(new User(username, password)).build();
        System.out.println("Sending Login Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived Login Response: " + response.toString());
        if (response.getType() == ResponseType.OK)
        {
            System.out.println("Login OK");
            this.client=client;
        }
        if (response.getType() == ResponseType.ERROR)
        {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public List<cars.Car> getAllCars()
    {
        return null;
    }

    @Override
    public List<cars.Car> getAllCarsBrand(String brand)
    {
        return null;
    }

    @Override
    public void saveCar(String brand, Integer hp)
    {

    }

    @Override
    public void deleteCar(Long id)
    {

    }

    @Override
    public void logout(User user, IObserver client) throws Exception
    {

    }

    private void handleUpdate(Response response)
    {
        if (response.getType()== ResponseType.SAVE_CAR)
        {
            Car car = (Car) response.getData();
            System.out.println("The car: " + car);
            try
            {
                client.carSaved(car);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (response.getType()== ResponseType.DELETE_CAR)
        {
            Long id = (Long) response.getData();
            System.out.println("The id of the car: " + id);
            try
            {
                client.carDeleted(id);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response)
    {
        return response.getType()== ResponseType.SAVE_CAR || response.getType()== ResponseType.DELETE_CAR;
    }
    private class ReaderThread implements Runnable
    {
        public void run()
        {
            while(!finished)
            {
                try
                {
                    String responseLine = input.readLine();
                    System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (isUpdate(response))
                    {
                        handleUpdate(response);
                    }
                    else
                    {
                        try
                        {
                            qresponses.put(response);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                catch (IOException e)
                {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}