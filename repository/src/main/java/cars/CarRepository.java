package cars;

import java.util.List;

public interface CarRepository extends Repository<Long, Car>
{
    List<Car> getAllByBrand(String brand);
}
