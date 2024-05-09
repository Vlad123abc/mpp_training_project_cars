package cars;

import java.util.List;

public interface IService
{
    void login(User user, IObserver client) throws Exception;
    void logout(User user, IObserver client) throws Exception;
    List<Car> getAllCars();
    List<Car> getAllCarsBrand(String brand);
    void saveCar(String brand, Integer hp);
    void deleteCar(Long id);
}
