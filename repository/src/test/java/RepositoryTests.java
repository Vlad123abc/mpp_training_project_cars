import cars.JdbcUtils;
import cars.UserDBRepository;
import cars.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class RepositoryTests
{
    private Connection createTestConnection()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        Connection conn = dbUtils.getConnection();
        return conn;
    }

    private void addTestUser(Connection conn, long id, String name, String password)
    {
        try(PreparedStatement preStmt = conn.prepareStatement("insert into Users (username, password) values (?, ?)"))
        {
            preStmt.setString(1, name);
            preStmt.setString(2, password);
            preStmt.executeUpdate();
        }
        catch(Exception e) {
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
            addTestUser(conn, 1, "vlad", "parola");
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
            addTestUser(conn, 1, "vlad", "parola");
            var user = repo.getById(1L);
            assertNotNull(user);
            var nouser  = repo.getById(2L);
            assertNull(nouser);
        }
        finally
        {
        }
    }        
}
