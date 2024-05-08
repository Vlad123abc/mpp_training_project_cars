package cars;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class CarDBRepository implements CarRepository
{
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public CarDBRepository(Properties props)
    {
        logger.info("Initializing CarDBRepository with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Car getById(Long aLong)
    {
        return null;
    }

    @Override
    public List<Car> getAll()
    {
        return null;
    }

    @Override
    public void save(Car entity)
    {

    }

    @Override
    public void delete(Long aLong)
    {

    }

    @Override
    public void update(Car entity)
    {

    }
}
