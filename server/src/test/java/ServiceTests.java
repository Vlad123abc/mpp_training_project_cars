import cars.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests
{
    private Service createTestService()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        Connection conn = dbUtils.getConnection();

        UserDBRepository Urepo = new UserDBRepository(conn);
        CarDBRepository Crepo = new CarDBRepository(conn);

        this.addTestUser(conn, "vlad", "parola");
        this.addTestUser(conn, "mark", "parola");
        this.addTestCar(conn, "ford", 500);
        this.addTestCar(conn, "dodge", 300);

        return new Service(Urepo, Crepo);
    }

    private void addTestUser(Connection conn, String name, String password)
    {
        try(PreparedStatement preStmt = conn.prepareStatement("insert into Users (username, password) values (?, ?)"))
        {
            preStmt.setString(1, name);
            preStmt.setString(2, password);
            preStmt.executeUpdate();
        }
        catch(Exception e)
        {
            System.err.println("Error DB" + e);
        }
    }
    private void addTestCar(Connection conn, String brand, Integer hp)
    {
        try(PreparedStatement preStmt = conn.prepareStatement("insert into Cars (brand, hp) values (?, ?)"))
        {
            preStmt.setString(1, brand);
            preStmt.setInt(2, hp);
            preStmt.executeUpdate();
        }
        catch(Exception e)
        {
            System.err.println("Error DB" + e);
        }
    }

    @Test
    public void userLoginTest() throws SQLException
    {
        Service service = this.createTestService();
        IObserver client = new IObserver()
        {
            @Override
            public void carSaved(Car car) throws Exception
            {

            }

            @Override
            public void carDeleted(Long id) throws Exception
            {

            }
        };

        try
        {
            service.login("vlad", "parola", client);
        }
        catch (Exception e)
        {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "User already logged in.");
        }

        try
        {
            service.login("vlad", "parolaaa", client);
            fail();
        }
        catch (Exception e)
        {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "Authentication failed.");
        }

        try
        {
            service.login("vlad", "parola", client);
            fail();
        }
        catch (Exception e)
        {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "User already logged in.");
        }
    }

    @Test
    public void userLogoutTest() throws SQLException
    {

    }

    @Test
    public void getAllCarsTest() throws SQLException
    {
        Service service = this.createTestService();
        var cars = service.getAllCars();
        assertEquals(cars.size(), 2);

        assertEquals(cars.get(0).getBrand(), "ford");
        assertEquals(cars.get(0).getHp(), 500);
        assertEquals(cars.get(1).getBrand(), "dodge");
        assertEquals(cars.get(1).getHp(), 300);
    }

    @Test
    public void getAllCarsBrandTest() throws SQLException
    {
        Service service = this.createTestService();
        var cars = service.getAllCarsBrand("ford");
        assertEquals(cars.size(), 1);

        assertEquals(cars.get(0).getBrand(), "ford");
        assertEquals(cars.get(0).getHp(), 500);
    }

    @Test
    public void save_deleteCarTest() throws SQLException
    {
        Service service = this.createTestService();
        var cars = service.getAllCars();
        assertEquals(cars.size(), 2);

        service.saveCar("chevy", 100);
        cars = service.getAllCars();
        assertEquals(cars.size(), 3);


        assertEquals(cars.get(2).getId(), 3L);
        assertEquals(cars.get(2).getBrand(), "chevy");
        assertEquals(cars.get(2).getHp(), 100);

        service.deleteCar(3L);
        cars = service.getAllCars();
        assertEquals(cars.size(), 2);

        assertEquals(cars.get(0).getBrand(), "ford");
        assertEquals(cars.get(0).getHp(), 500);
        assertEquals(cars.get(1).getBrand(), "dodge");
        assertEquals(cars.get(1).getHp(), 300);
    }
}
