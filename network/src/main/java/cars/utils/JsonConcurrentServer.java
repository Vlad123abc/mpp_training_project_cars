package cars.utils;

import cars.IService;
import cars.jsonProtocol.ClientWorker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
            var output = new PrintWriter(client.getOutputStream());            
            var input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ClientWorker worker = new ClientWorker(server, client, input, output );
            return new Thread(worker);
        } catch (Exception e) {
            // ...
        }
        return null;
    }
}
