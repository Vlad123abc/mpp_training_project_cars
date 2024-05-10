import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.jsonProtocol.ClientWorker;
import cars.jsonProtocol.ServiceProxy;

public class NetworkingTest
{
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    private IObserver observer = new IObserver()
    {
        @Override
        public void carSaved(Car car) throws Exception
        {
            System.out.println("Observer Car Saved");
        }

        @Override
        public void carDeleted(Long id) throws Exception
        {
            System.out.println("Observer Car Deleted");
        }
    };
    @Test
    public void smoke()
    {
        String r = "{}";
        StringReader sr = new StringReader(r);
        StringWriter sw = new StringWriter();
        var input = new BufferedReader(sr );      
        var output = new PrintWriter(sw);
        ClientWorker cw = new ClientWorker (null,null, input, output);
        cw.run();
    }
}
