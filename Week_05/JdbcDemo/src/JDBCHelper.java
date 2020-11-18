package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCHelper {

    private static Connection connection;

    private JDBCHelper() {}

    public static Statement getStatement() throws Exception {
        Connection connection = getConnection();
        JDBCHelper.connection = connection;
        return connection.createStatement();
    }

    public static void release(ResultSet resultSet, Statement statement) throws Exception {
        if (null != resultSet) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (null != connection) {
            connection.close();
        }
    }

    public static Connection getConnection() throws Exception {
        Class.forName(JDBCConstant.DRIVER);
        return DriverManager.getConnection(JDBCConstant.URL, JDBCConstant.USER_NAME, JDBCConstant.PWD);
    }

}
