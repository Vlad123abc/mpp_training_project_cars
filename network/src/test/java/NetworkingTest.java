import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        String r = "{'type'='LOGIN', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}" + System.lineSeparator();
        StringReader sr = new StringReader(r);
        StringWriter sw = new StringWriter();
        var input = new BufferedReader(sr );      
        var output = new PrintWriter(sw);
        IService mockService = Mockito.mock(IService.class);
        Closeable mockSocket = Mockito.mock(Closeable.class);        
        ClientWorker cw = new ClientWorker (mockService,mockSocket, input, output);
        cw.run();
    }
}
