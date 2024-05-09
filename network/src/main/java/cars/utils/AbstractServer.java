package cars.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer
{
    private int port;
    private ServerSocket server = null;

    public AbstractServer(int port)
    {
        this.port = port;
    }

    public void start() throws Exception
    {
        try
        {
            server = new ServerSocket(port);
            while(true)
            {
                System.out.println("Waiting for clients ...");
                Socket client=server.accept();
                System.out.println("Client connected ...");
                processRequest(client);
            }
        }
        catch (IOException e)
        {
            throw new Exception("Starting server errror ",e);
        }
        finally
        {
            stop();
        }
    }

    protected abstract void processRequest(Socket client);

    public void stop() throws Exception
    {
        try
        {
            System.out.println("Server stopping ...");
            server.close();
            System.out.println("Server stopped");
        }
        catch (IOException e)
        {
            throw new Exception("Closing server error ", e);
        }
    }
}