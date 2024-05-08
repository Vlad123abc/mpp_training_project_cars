package cars;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils
{
    private Properties props;
    public JdbcUtils(Properties props){
        this.props=props;
    }

    private Connection getNewConnection()
    {
        String driver = props.getProperty("jdbc.driver");
        String url = props.getProperty("jdbc.url");

        Connection con = null;

        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error loading driver " + e);
        }
        catch (SQLException e)
        {
            System.out.println("Error getting connection " + e);
        }

        return con;
    }

    public Connection getConnection()
    {
        return getNewConnection();
    }
}
