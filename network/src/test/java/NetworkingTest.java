import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
    public void loginOk() throws Exception
    {
            // setting up input that will be sent
        String r = "{'type'='LOGIN', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}" + System.lineSeparator();
            // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(r);
        var input = new BufferedReader(sr);

            // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter sw = new StringWriter();
        var output = new PrintWriter(sw);

            // Mock objects - these implement interfaces, and they do nothing yet.
        IService mockService = Mockito.mock(IService.class);
        Closeable mockSocket = Mockito.mock(Closeable.class);
            // create the worker with input, output and the mock objects - nothing is real here, we test the CW in isolation.
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
        Mockito.verify(mockService).login(Mockito.eq("vlad"), Mockito.eq("parola"),Mockito.any());

            // now we assert the output
            // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
            // was the login response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }

    @Test
    public void loginFail() throws Exception
    {
            // input
        String r = "{'type'='LOGIN', 'data'={'username'='vladaaa', 'password'='parola', 'id'=0}}" + System.lineSeparator();
            // setting up stuff
        StringReader sr = new StringReader(r);
        StringWriter sw = new StringWriter();
        var input = new BufferedReader(sr);
        var output = new PrintWriter(sw);
        IService mockService = Mockito.mock(IService.class);

            // now we say login will be throw an error from our mock service
        Mockito.doThrow(new Exception()).when(mockService).login(Mockito.eq("vladaaa"), Mockito.eq("parola"),Mockito.any());
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

        Mockito.verify(mockService).login(Mockito.eq("vladaaa"), Mockito.eq("parola"),Mockito.any());
        
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        var responseData = gson.fromJson(responseLine, Response.class);
            // assert that the exception was transformed to reject 
        assertEquals(responseData.getType(), ResponseType.ERROR);
    }

    @Test
    public void getAllCarsTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='GET_ALL_CARS', 'data'=null}}" + System.lineSeparator();
        // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(r);
        var input = new BufferedReader(sr);

        // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter sw = new StringWriter();
        var output = new PrintWriter(sw);

        // Mock objects - these implement interfaces, and they do nothing yet.
        IService mockService = Mockito.mock(IService.class);
        // Mockito.when(mockList.size()).thenReturn(100);
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Ford", 100));
        cars.add(new Car("dodge", 10));
        Mockito.when(mockService.getAllCars()).thenReturn(cars);

        Closeable mockSocket = Mockito.mock(Closeable.class);
        // create the worker with input, output and the mock objects - nothing is real here, we test the CW in isolation.
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
        Mockito.verify(mockService).getAllCars();

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the login response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.GET_ALL_CARS);
        assertEquals(responseData.getData(), cars);
    }
}
