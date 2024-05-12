import cars.IObserver;
import cars.User;
import cars.jsonProtocol.Response;
import cars.jsonProtocol.ResponseType;
import cars.jsonProtocol.ServiceProxy;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceProxyTests {
    @Test
    public void LoginTest() throws Exception {
        // setting up input reader - just as we were reading from the socket
        String responsesSeparatedWithNewLines = "{'type'='OK', 'data'=1}" + System.lineSeparator();

        StringReader responseReader = new StringReader(responsesSeparatedWithNewLines);
        var bufferedResponseReader = new BufferedReader(responseReader);

        // setting up writer - our ClientWorker will write responses here. We will
        // assert later what the test output was.
        StringWriter outputWriter = new StringWriter();
        var outputPrintWriter = new PrintWriter(outputWriter);

        // Mock objects - these implement interfaces, and they do nothing yet.
        Closeable mockSocket = Mockito.mock(Closeable.class);
        IObserver mockObs = Mockito.mock(IObserver.class);

        ServiceProxy serviceProxy = new ServiceProxy(mockSocket, bufferedResponseReader, outputPrintWriter);

        System.out.println("calling login");
        serviceProxy.login("vlad", "parola", mockObs);
        System.out.println("called login");

        // check that login succeeded, how do we check this?
        // well, on error the socket is closed.
        Mockito.verify(mockSocket, Mockito.never()).close();

        System.out.println("calling closeConnection");
        bufferedResponseReader.close();
        outputPrintWriter.close();

        // check what was written to the server
        String allResponsesOneResponsePerLine = new String(outputWriter.getBuffer());
        StringReader whatWasWrittenReader = new StringReader(allResponsesOneResponsePerLine);
        var whatWasWrittenCheckInput = new BufferedReader(whatWasWrittenReader);
        var writtenLine = whatWasWrittenCheckInput.readLine();
        Gson gson = new Gson();

        // was the login sent all?
        var sentData = gson.fromJson(writtenLine, Response.class);
        assertEquals(sentData.getType(), ResponseType.LOGIN);

        // ... add verification to see user and pass
        User user = gson.fromJson(sentData.getData().toString(), User.class);
        assertEquals(user.getUsername(), "vlad");
        assertEquals(user.getPassword(), "parola");
    }

    @Test
    public void LogoutTest() throws Exception {
        // setting up input reader - just as we were reading from the socket
        String responsesSeparatedWithNewLines = "{'type'='OK', 'data'=1}" + System.lineSeparator();

        StringReader responseReader = new StringReader(responsesSeparatedWithNewLines);
        var bufferedResponseReader = new BufferedReader(responseReader);

        // setting up writer - our ClientWorker will write responses here. We will
        // assert later what the test output was.
        StringWriter outputWriter = new StringWriter();
        var outputPrintWriter = new PrintWriter(outputWriter);

        // Mock objects - these implement interfaces, and they do nothing yet.
        Closeable mockSocket = Mockito.mock(Closeable.class);
        IObserver mockObs = Mockito.mock(IObserver.class);

        ServiceProxy serviceProxy = new ServiceProxy(mockSocket, bufferedResponseReader, outputPrintWriter);

        System.out.println("calling logout");
        User vlad = new User("vlad", "parola");
        vlad.setId(1L);
        serviceProxy.logout(vlad, mockObs);
        System.out.println("called logout");

        // check that login succeeded, how do we check this?
        // well, on error the socket is closed.
        Mockito.verify(mockSocket, Mockito.never()).close();

        System.out.println("calling closeConnection");
        bufferedResponseReader.close();
        outputPrintWriter.close();

        // check what was written to the server
        String allResponsesOneResponsePerLine = new String(outputWriter.getBuffer());
        StringReader whatWasWrittenReader = new StringReader(allResponsesOneResponsePerLine);
        var whatWasWrittenCheckInput = new BufferedReader(whatWasWrittenReader);
        var writtenLine = whatWasWrittenCheckInput.readLine();
        Gson gson = new Gson();

        // was the login sent all?
        var sentData = gson.fromJson(writtenLine, Response.class);
        assertEquals(sentData.getType(), ResponseType.LOGOUT);

        // ... add verification to see user and pass
        User user = gson.fromJson(sentData.getData().toString(), User.class);
        assertEquals(user.getUsername(), "vlad");
        assertEquals(user.getPassword(), "parola");
    }
}
