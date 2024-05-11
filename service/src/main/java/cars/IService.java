package cars;

import java.util.List;

public interface IService
{
    void login(String username, String password, IObserver client) throws Exception;
    void logout(User user, IObserver client) throws Exception;
    List<Car> getAllCars() throws Exception;
    List<Car> getAllCarsBrand(String brand) throws Exception;
    void saveCar(String brand, Integer hp) throws Exception;
    void deleteCar(Long id) throws Exception;
}
