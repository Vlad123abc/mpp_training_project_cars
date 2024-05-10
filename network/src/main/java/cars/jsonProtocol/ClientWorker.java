package cars.jsonProtocol;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ClientWorker implements Runnable, IObserver {
    private IService server;

    private Closeable connection;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;

    private volatile boolean connected;

    public ClientWorker(IService server, Closeable connection, BufferedReader input, PrintWriter output) {
        this.server = server;
        this.connection = connection;

        gsonFormatter = new Gson();
        this.output = output;
        this.input = input;
        connected = true;
    }

    @Override
    public void carSaved(Car car) throws Exception {

    }

    @Override
    public void carDeleted(Long id) throws Exception {

    }
    public void stop() {
        connected = false;
        System.out.println(String.format("Stop requested, Connected is: %s", connected));                        
    }
    @Override
    public void run() {
        while (connected) {
            try
            {
                String requestLine = input.readLine();
                System.out.println(new String("received line:") + requestLine);
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                if (request != null) {
                    Response response = handleRequest(request);
                    if (response != null) {
                        sendResponse(response);
                    }
                }
                System.out.println(String.format("Connected is: %s", connected));                
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(Request request)
    {
        Response response = null;

        if (request.getType() == RequestType.LOGIN)
        {
            System.out.println("Login request ..." + request.getType());
            // User user = (User) request.getData();
            User user = gsonFormatter.fromJson(request.getData().toString(), User.class);
            try {
                server.login(user.getUsername(), user.getPassword(), this);
                return new Response.Builder().setType(ResponseType.OK).build();
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().setType(ResponseType.ERROR).setData(e.getMessage()).build();
            }
        }

        if (request.getType() == RequestType.GET_ALL_CARS)
        {
            System.out.println("GET_ALL_CARS request ..." + request.getType());
            try
            {
                List<Car> cars = this.server.getAllCars();
                return new Response.Builder().setType(ResponseType.GET_ALL_CARS).setData(cars).build();
            } catch (Exception e)
            {
                connected = false;
                return new Response.Builder().setType(ResponseType.ERROR).setData(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException
    {
        String responseLine = gsonFormatter.toJson(response);
        System.out.println("sending response " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }
}
