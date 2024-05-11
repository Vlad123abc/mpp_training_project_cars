import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cars.Car;
import cars.IService;
import cars.jsonProtocol.ClientWorker;
import cars.jsonProtocol.Response;
import cars.jsonProtocol.ResponseType;

public class ClientWorkerTests
{
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
    public void logoutTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='LOGOUT', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}" + System.lineSeparator();
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
        Mockito.verify(mockService).logout(Mockito.any(), Mockito.any());

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the logout response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }

    @Test
    public void getAllCarsTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='GET_ALL_CARS', 'data'=null}" + System.lineSeparator();
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
        Car car1 = new Car("Ford", 100);
        car1.setId(1L);
        Car car2 = new Car("Dodge", 10);
        car2.setId(2L);
        cars.add(car1);
        cars.add(car2);
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
        // was the response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.GET_ALL_CARS);

        List<Car> carList = new ArrayList<>();
        var list = gson.fromJson(responseData.getData().toString(), cars.getClass());
        for (var car : list)
        {
            Car c = gson.fromJson(car.toString(), Car.class);
            carList.add(c);
        }
        assertEquals(carList, cars);
    }

    @Test
    public void getAllCarsBrandTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='GET_ALL_CARS_BRAND', 'data'='ford'}" + System.lineSeparator();
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
        Car car1 = new Car("ford", 100);
        car1.setId(1L);
        Car car2 = new Car("dodge", 10);
        car2.setId(2L);
        cars.add(car1);
        cars.add(car2);
        List<Car> resultCars = new ArrayList<>();
        resultCars.add(car1);
        Mockito.when(mockService.getAllCarsBrand(Mockito.eq("ford"))).thenReturn(resultCars);

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
        Mockito.verify(mockService).getAllCarsBrand("ford");

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.GET_ALL_CARS_BRAND);

        List<Car> carList = new ArrayList<>();
        var list = gson.fromJson(responseData.getData().toString(), cars.getClass());
        for (var car : list)
        {
            Car c = gson.fromJson(car.toString(), Car.class);
            carList.add(c);
        }
        assertEquals(carList, resultCars);
    }

    @Test
    public void SaveCarTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='SAVE_CAR', 'data'={'brand'='ford', 'hp'=100, 'id'=0}}" + System.lineSeparator();
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
        Mockito.verify(mockService).saveCar("ford", 100);

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }

    @Test
    public void CarSavedNotificationForwarded() throws Exception
    {
        // setting up input that will be sent
        String r = System.lineSeparator();
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
        Thread.sleep(500);
            // this is just like the service calling its observer, telling hey there's a new car
        var ford = new Car("Ford", 100);
        cw.carSaved(ford);
        Thread.sleep(500);        
        cw.stop();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // did the CW forward the car saved notification?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(ResponseType.SAVE_CAR, responseData.getType());
        var car = gson.fromJson(responseData.getData().toString(), Car.class);
        assertEquals(car, ford);        
    }
    
    @Test
    public void DeleteCarTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='DELETE_CAR', 'data'=1}" + System.lineSeparator();
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
        Mockito.verify(mockService).deleteCar(1L);

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }

    @Test
    public void CarDeletedNotificationForwarded() throws Exception
    {
        // setting up input that will be sent
        String r = System.lineSeparator();
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
        Thread.sleep(500);
        // this is just like the service calling its observer, telling hey there's a new car
        cw.carDeleted(1L);
        Thread.sleep(500);
        cw.stop();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // did the CW forward the car saved notification?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(ResponseType.DELETE_CAR, responseData.getType());
        var car = gson.fromJson(responseData.getData().toString(), Long.class);
        assertEquals(car, 1L);
    }
}