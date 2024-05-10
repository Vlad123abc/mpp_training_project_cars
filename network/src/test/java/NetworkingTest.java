import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.jsonProtocol.ClientWorker;
import cars.jsonProtocol.Response;
import cars.jsonProtocol.ResponseType;
import cars.jsonProtocol.ServiceProxy;

public class NetworkingTest {
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    private IObserver observer = new IObserver() {
        @Override
        public void carSaved(Car car) throws Exception {
            System.out.println("Observer Car Saved");
        }

        @Override
        public void carDeleted(Long id) throws Exception {
            System.out.println("Observer Car Deleted");
        }
    };

    @Test
    public void smoke() throws IOException {
        String r = "{'type'='LOGIN', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}" + System.lineSeparator();
        StringReader sr = new StringReader(r);
        StringWriter sw = new StringWriter();
        var input = new BufferedReader(sr);
        var output = new PrintWriter(sw);
        IService mockService = Mockito.mock(IService.class);
        Closeable mockSocket = Mockito.mock(Closeable.class);
        ClientWorker cw = new ClientWorker(mockService, mockSocket, input, output);
        Thread t = new Thread(cw);
        t.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        cw.stop();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }
}
