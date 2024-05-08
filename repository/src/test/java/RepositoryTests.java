import cars.CarDBRepository;
import cars.JdbcUtils;
import cars.UserDBRepository;
import cars.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTests
{
    private Connection createTestConnection()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        return dbUtils.getConnection();
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

    @Test
    public void userRepoSmokeTest()  throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            UserDBRepository repo = new UserDBRepository(conn);
            assertNotNull(repo);
        }        
    }
    
    @Test
    public void UserRepoGetByUserName() throws SQLException
    {
        try(Connection conn = createTestConnection())
        {
            UserDBRepository repo = new UserDBRepository(conn);
            assertNotNull(repo);
            addTestUser(conn, "vlad", "parola");
            var user = repo.getByUsername("vlad");
            assertNotNull(user);
            assertNull(repo.getByUsername("Bela"));
        }
    }

    @Test
    public void UserRepoGetByUserId() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            UserDBRepository repo = new UserDBRepository(conn);
            assertNotNull(repo);
            addTestUser(conn, "vlad", "parola");
            var user = repo.getById(1L);
            assertNotNull(user);
            var nouser  = repo.getById(2L);
            assertNull(nouser);
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
    public void CarRepoGetAllTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            CarDBRepository repo = new CarDBRepository(conn);
            assertNotNull(repo);
            addTestCar(conn, "ford", 500);
            addTestCar(conn, "dodge", 400);
            addTestCar(conn, "chevy", 300);

            var cars = repo.getAll();
            assertNotNull(cars);

            assertEquals(cars.size(), 3);
            assertEquals(cars.get(0).getBrand(), "ford");
            assertEquals(cars.get(0).getHp(), 500);
        }
    }
}
