import cars.IObserver;
import cars.jsonProtocol.Response;
import cars.jsonProtocol.ResponseType;
import cars.jsonProtocol.ServiceProxy;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceProxyTests
{
    @Test
    public void LoginTest() throws Exception
    {
        // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(System.lineSeparator());
        var input = new BufferedReader(sr);

        // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter sw = new StringWriter();
        var output = new PrintWriter(sw);

        // Mock objects - these implement interfaces, and they do nothing yet.
        Closeable mockSocket = Mockito.mock(Closeable.class);
        IObserver mockObs = Mockito.mock(IObserver.class);

        ServiceProxy serviceProxy = new ServiceProxy(mockSocket, input, output);

        serviceProxy.login("vlad", "parola", mockObs);

        //assertEquals(input.readLine(), "{'type'='LOGIN', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}");
    }

    @Test
    public void LogoutTest()
    {

    }
}
