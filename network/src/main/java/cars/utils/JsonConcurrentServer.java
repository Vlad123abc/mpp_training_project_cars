package cars.utils;

import cars.IService;
import cars.jsonProtocol.ClientWorker;

import java.net.Socket;

public class JsonConcurrentServer extends AbstractConcurrentServer {
    private IService server;

    public JsonConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("Json Concurrent Server");
    }

    @Override
    protected Thread createWorker(Socket client) {
        try {
            ClientWorker worker = new ClientWorker(server, client, client.getInputStream(), client.getOutputStream());
            return new Thread(worker);
        } catch (Exception e) {
            // ...
        }
        return null;
    }
}
