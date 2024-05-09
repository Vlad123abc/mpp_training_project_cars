package cars.jsonProtocol;

import cars.IObserver;
import cars.IService;
import cars.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ServiceProxy implements IService
{
    private String host;
    private int port;

    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    @Override
    public void login(String username, String password, IObserver client) throws Exception
    {

    }

    @Override
    public List<cars.Car> getAllCars()
    {
        return null;
    }

    @Override
    public List<cars.Car> getAllCarsBrand(String brand)
    {
        return null;
    }

    @Override
    public void saveCar(String brand, Integer hp)
    {

    }

    @Override
    public void deleteCar(Long id)
    {

    }

    @Override
    public void logout(User user, IObserver client) throws Exception
    {

    }
}
