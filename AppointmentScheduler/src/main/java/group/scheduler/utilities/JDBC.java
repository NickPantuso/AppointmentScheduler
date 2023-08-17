package group.scheduler.utilities;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.mchange.v2.c3p0.*;

/**
 * Creates connection to the database.
 * @author Nick Pantuso
 */
public abstract class JDBC implements Connection{

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location; //location goes here
    private static final String databaseName; //db name goes here
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName; //username goes here
    private static String password; //password goes here
    public static ComboPooledDataSource cpds = new ComboPooledDataSource();
    public static Connection connection;  // Connection Interface

    /**
     * Sets up configuration for ComboPooledDataSource.
     * @throws PropertyVetoException
     */
    public static void setupConnection() throws PropertyVetoException {
        cpds.setDriverClass(driver);
        cpds.setJdbcUrl(jdbcUrl);
        cpds.setUser(userName);
        cpds.setPassword(password);
        cpds.setMinPoolSize(1);
        cpds.setMaxIdleTime(13);
        cpds.setIdleConnectionTestPeriod(10);
        cpds.setPreferredTestQuery("SELECT 1");
    }

    /**
     * Opens connection to the database.
     */
    public static void openConnection()
    {
        try {
            connection = cpds.getConnection();
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Closes connection to the database.
     */
    public static void closeConnection() {
        try {
            cpds.close();
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Checks if connection to database is still alive. If exception is thrown, reopen the connection.
     */
    public static void checkConnection() {
        try {
            String sql = "SELECT 1";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.executeQuery();
        } catch (Exception e) {
           openConnection();
        }
    }
}
