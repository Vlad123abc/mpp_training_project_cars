import cars.JdbcUtils;
import cars.UserDBRepository;
import cars.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class RepositoryTests
{
    @Test
    public void userRepoSmokeTest()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        Connection conn = dbUtils.getConnection();
        UserDBRepository repo = new UserDBRepository(conn);
        assertNotNull(repo);                
    }

    private void addTestUser(Connection conn, long id, String name, String password)
    {
        try(PreparedStatement preStmt = conn.prepareStatement("insert into Users (id_user, username, password) values (?, ?, ?)"))
        {
            preStmt.setLong(1, id);
            preStmt.setString(2, name);
            preStmt.setString(3, password);
            preStmt.executeUpdate();
        }
        catch(Exception e) {
            System.err.println("Error DB" + e);
        }
    }
    
    @Test
    public void UserRepoGetByUserName()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        Connection conn = dbUtils.getConnection();
        UserDBRepository repo = new UserDBRepository(conn);
        assertNotNull(repo);
        addTestUser(conn, 1, "vlad", "parola");
        var user = repo.getByUsername("vlad");
        assertNotNull(user);
        assertNull(repo.getByUsername("Bela"));
    }

    @Test
    public void UserRepoGetByUserId()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        Connection conn = dbUtils.getConnection();
        UserDBRepository repo = new UserDBRepository(conn);
        assertNotNull(repo);
        addTestUser(conn, 1, "vlad", "parola");
        var user = repo.getById(1L);
        assertNotNull(user);
        var nouser  = repo.getById(2L);
        assertNull(nouser);        
    }        
}
