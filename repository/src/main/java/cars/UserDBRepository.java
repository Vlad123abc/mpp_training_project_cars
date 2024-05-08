package cars;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository
{
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public UserDBRepository(Properties props)
    {
        logger.info("Initializing UtilizatorDBRepository with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public User getById(Long id)
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Users where id_user = ?"))
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
        return null;
    }

    @Override
    public void save(User entity)
    {

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
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Users where username = ?"))
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
