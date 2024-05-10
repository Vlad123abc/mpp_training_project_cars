package cars;


import cars.jsonProtocol.ServiceProxy;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

class TestObserver implements IObserver
{
    @Override
    public void carSaved(Car car) throws Exception
    {
        System.out.println("Observer Save Car");
    }

    @Override
    public void carDeleted(Long id) throws Exception
    {
        System.out.println("Observer Delete Car");
    }
}

public class StartClientConsole
{
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    public void start() throws Exception
    {
        System.out.println("In start");

        String serverIP = defaultServer;
        int serverPort = defaultChatPort;
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new ServiceProxy(serverIP, serverPort);
        TestObserver observer = new TestObserver();

        try
        {
            server.login("vlad", "parola", observer);
            System.out.println("Login Succes!");
        }
        catch (Exception e)
        {
            System.out.println("Login Fail!");
            System.out.println(e.getMessage());
        }

        try
        {
            server.login("mark", "parolaaa", observer);
            System.out.println("Login Succes!");
        }
        catch (Exception e)
        {
            System.out.println("Login Fail!");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String [] args)
    {
        StartClientConsole app = new StartClientConsole();
        try
        {
            app.start();
        }
        catch (Exception e)
        {
            System.out.println("Client console error" + e.getMessage());
        }
    }
}
