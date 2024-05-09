import cars.CarDBRepository;
import cars.JdbcUtils;
import cars.Service;
import cars.UserDBRepository;
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
}
