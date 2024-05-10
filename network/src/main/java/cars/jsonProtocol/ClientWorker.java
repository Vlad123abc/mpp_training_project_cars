package cars.jsonProtocol;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWorker implements Runnable, IObserver
{
    private IService server;

    private Closeable connection;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;

    private volatile boolean connected;

    public ClientWorker(IService server, Closeable connection, InputStream input, OutputStream output)
    {
        this.server = server;
        this.connection = connection;

        gsonFormatter = new Gson();
        this.output = new PrintWriter(output);
        this.input = new BufferedReader(new InputStreamReader(input));
        connected = true;
    }

    @Override
    public void carSaved(Car car) throws Exception
    {

    }

    @Override
    public void carDeleted(Long id) throws Exception
    {

    }

    @Override
    public void run()
    {
        while(connected)
        {
            try
            {
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if (response!=null)
                {
                    sendResponse(response);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            input.close();
            output.close();
            connection.close();
        }
        catch (IOException e)
        {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(Request request)
    {
        Response response = null;

        if (request.getType() == RequestType.LOGIN)
        {
            System.out.println("Login request ..." + request.getType());
            User user = (User) request.getData();
            try
            {
                server.login(user.getUsername(), user.getPassword(), this);
                return new Response.Builder().setType(ResponseType.OK).build();
            }
            catch (Exception e)
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
        synchronized (output)
        {
            output.println(responseLine);
            output.flush();
        }
    }
}
