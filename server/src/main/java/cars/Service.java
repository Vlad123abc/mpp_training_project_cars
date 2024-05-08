package cars;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService
{
    private UserRepository userRepository;
    private CarRepository carRepository;

    private Map<Long, IObserver> loggedClients;

    public Service(UserRepository userRepository, CarRepository carRepository)
    {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, IObserver client) throws Exception
    {
        User userR = userRepository.getById(user.getId());
        if (userR != null)
        {
            if(loggedClients.get(user.getId()) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(user.getId(), client);
            //notify(user);
        }
        else
            throw new Exception("Authentication failed.");
    }
}
