package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

/**
 * JDBC Demo
 * 批处理,PreparedStatement,手动提交事务
 */
public class JDBCWithPrepareStatementAndBatchAndTxDemo {

    public static void main(String[] args) throws Exception {
        Connection connection = JDBCHelper.getConnection();
        connection.setAutoCommit(false);
        String insertSql = "insert into person (name,score) values (concat('ws',last_insert_id()), ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        for (int i = 0; i < 300; i++) {
            preparedStatement.setInt(1, i);
            preparedStatement.addBatch();
        }    
        int[] rowArray = preparedStatement.executeBatch();
        connection.commit();
        System.out.println(Arrays.toString(rowArray));
        JDBCHelper.release(null, preparedStatement);
    }
}
