package cars;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        User userR = userRepository.getByUsername(user.getUsername());
        if (userR != null)
        {
            if(loggedClients.get(user.getId()) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(user.getId(), client);
        }
        else
            throw new Exception("Authentication failed.");
    }

    @Override
    public synchronized void logout(User user, IObserver client) throws Exception
    {
        IObserver localClient = loggedClients.remove(user.getId());
        if (localClient == null)
            throw new Exception("User " + user.getUsername() + " is not logged in.");
    }

    @Override
    public synchronized List<Car> getAllCars()
    {
        return this.carRepository.getAll();
    }

    @Override
    public synchronized List<Car> getAllCarsBrand(String brand)
    {
        return this.carRepository.getAllByBrand(brand);
    }

    @Override
    public synchronized void saveCar(String brand, Integer hp)
    {
        this.carRepository.save(new Car(brand, hp));
        this.notifyCarSaved(brand, hp);
    }

    @Override
    public synchronized void deleteCar(Long id)
    {
        this.carRepository.delete(id);
        this.notifyCarDeleted(id);
    }

    public void notifyCarSaved(String brand, Integer hp)
    {
        Iterable<User> users = this.userRepository.getAll();

        for(User us : users)
        {
            IObserver chatClient = loggedClients.get(us.getId());
            if (chatClient != null)
            {
                try
                {
                    System.out.println("notifying car saved");
                    chatClient.carSaved(new Car(brand, hp));
                }
                catch (Exception e)
                {
                    System.err.println("Error notifying user " + e);
                }
            }
        }
    }

    public void notifyCarDeleted(Long id)
    {
        Iterable<User> users = this.userRepository.getAll();

        for(User us : users)
        {
            IObserver chatClient = loggedClients.get(us.getId());
            if (chatClient != null)
            {
                try
                {
                    System.out.println("notifying car deleted");
                    chatClient.carDeleted(id);
                }
                catch (Exception e)
                {
                    System.err.println("Error notifying user " + e);
                }
            }
        }
    }
}
