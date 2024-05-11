import cars.IService;
import cars.jsonProtocol.ClientWorker;
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
    public void LoginTest()
    {
        ServiceProxy serviceProxy = new ServiceProxy("localhost", 55556);

        // Mock objects - these implement interfaces, and they do nothing yet.
        ClientWorker mockCw = Mockito.mock(ClientWorker.class);
    }

    @Test
    public void LogoutTest()
    {

    }
}
