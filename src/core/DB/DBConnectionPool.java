package core.DB;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {

    private static DBConnectionPool pool;
    private ComboPooledDataSource dataSource;
    public String driver = "com.mysql.jdbc.Driver";
    public String url = "jdbc:mysql://127.0.0.1/xurentest";
    public String userName = "xuren";
    public String password = "xuren";
    public int maxPoolSize = 40;
    public int minPoolSize = 2;
    public int initialPoolSize = 10;
    public int maxStatements = 180;

    private DBConnectionPool() throws PropertyVetoException {
        this.dataSource = new ComboPooledDataSource();
        init();
    }

    public static DBConnectionPool getInstance() throws PropertyVetoException {
        if(pool == null){
            pool = new DBConnectionPool();
        }
        return pool;
    }

    private void init() throws PropertyVetoException {
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setMaxStatements(maxStatements);
        dataSource.setInitialPoolSize(initialPoolSize);
    }



    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
