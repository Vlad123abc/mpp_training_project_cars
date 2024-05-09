package cars;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository implements UserRepository
{
    private static final Logger logger = LogManager.getLogger();
    private Connection connection = null;
    
    public UserDBRepository(Connection connection)
    {
        logger.info("Initializing UserDBRepository");
        this.connection = connection;
        initializeDbIfNeeded();
    }

    public void initializeDbIfNeeded()
    {
        try {
            final Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users (id_user INTEGER, username TEXT, password TEXT, PRIMARY KEY(id_user))");
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }        
    }
    @Override
    public User getById(Long id)
    {
        logger.traceEntry();

        try(PreparedStatement preStmt = connection.prepareStatement("select * from Users where id_user = ?"))
        {
            preStmt.setLong(1, id);
            try(ResultSet resultSet = preStmt.executeQuery())
            {
                if (resultSet.next())
                {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(id);

                    logger.traceExit();
                    return user;
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        return null;
    }

    @Override
    public List<User> getAll()
    {
        logger.traceEntry();

        List<User> users = new ArrayList<>();

        try(PreparedStatement preStmt = connection.prepareStatement("select * from Users"))
        {
            try(ResultSet resultSet = preStmt.executeQuery())
            {
                while (resultSet.next())
                {
                    int id_user = resultSet.getInt("id_user");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User user = new User(username, password);
                    user.setId((long) id_user);
                    users.add(user);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public void save(User user)
    {
        logger.traceEntry("saving tsak {}", user);
        try(PreparedStatement preStmt = connection.prepareStatement("insert into Users(username, password) values(?, ?)"))
        {
            preStmt.setString(1, user.getUsername());
            preStmt.setString(2, user.getPassword());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            logger.traceExit();
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong)
    {

    }

    @Override
    public void update(User entity)
    {

    }

    @Override
    public User getByUsername(String username)
    {
        logger.traceEntry();

        try(PreparedStatement preStmt = connection.prepareStatement("select * from Users where username = ?"))
        {
            preStmt.setString(1, username);
            try(ResultSet resultSet = preStmt.executeQuery())
            {
                if (resultSet.next())
                {
                    Long id = resultSet.getLong("id_user");
                    String password = resultSet.getString("password");

                    User user = new User(username, password);
                    user.setId(id);

                    logger.traceExit();
                    return user;
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        return null;
    }
}
