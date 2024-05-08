package cars.utils;

import cars.utils.AbstractServer;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer
{

    public AbstractConcurrentServer(int port)
    {
        super(port);
        System.out.println("Concurrent AbstractServer");
    }

    @Override
    protected void processRequest(Socket client)
    {
        Thread tw = createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client);
}
