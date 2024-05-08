package cars.utils;

import cars.IService;

import java.net.Socket;

public class JsonConcurrentServer extends AbstractConcurrentServer
{
    private IService server;

    public JsonConcurrentServer(int port, IService server)
    {
        super(port);
        this.server = server;
        System.out.println("Json Concurrent Server");
    }

    @Override
    protected Thread createWorker(Socket client)
    {
//        ChatClientJsonWorker worker=new ChatClientJsonWorker(chatServer, client);
//
//        Thread tw=new Thread(worker);
//        return tw;
        return null;
    }
}
