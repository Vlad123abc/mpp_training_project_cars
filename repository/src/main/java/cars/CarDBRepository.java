package cars;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarDBRepository implements CarRepository
{
    private static final Logger logger = LogManager.getLogger();
    private Connection connection = null;

    public CarDBRepository(Connection connection)
    {
        logger.info("Initializing CarDBRepository");
        this.connection = connection;
        initializeDbIfNeeded();
    }

    public void initializeDbIfNeeded()
    {
        try
        {
            final Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Cars (id_car INTEGER, brand TEXT, hp INTEGER, PRIMARY KEY(id_car))");
        }
        catch (SQLException e)
        {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public Car getById(Long aLong)
    {
        return null;
    }

    @Override
    public List<Car> getAll()
    {
        logger.traceEntry();

        List<Car> cars = new ArrayList<>();

        try(PreparedStatement preStmt = connection.prepareStatement("select * from Cars"))
        {
            try(ResultSet resultSet = preStmt.executeQuery())
            {
                while (resultSet.next())
                {
                    int id_car = resultSet.getInt("id_car");
                    String brand = resultSet.getString("brand");
                    int hp = resultSet.getInt("hp");

                    Car car = new Car(brand, hp);
                    car.setId((long) id_car);
                    cars.add(car);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return cars;
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
