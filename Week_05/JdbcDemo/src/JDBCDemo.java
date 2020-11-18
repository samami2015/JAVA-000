package src;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * JDBC 基本实现
 */
public class JDBCDemo {

    public static void main(String[] args) throws Exception {
        Statement statement = JDBCHelper.getStatement();
        // select
        String sql = "select id,name from person limit 10";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println("id:" + id + "\t\tname: " + name);

        }

        // UPDATA
        sql = "update person set name='test' where id=1";
        int rows = statement.executeUpdate(sql);
//        if (rows > 0) {
//            System.out.println("更新成功...");
//        }
        // insert
        sql = "insert into person (name,score) values (concat('ws',last_insert_id()), 100);";
        boolean result = statement.execute(sql);
//        if (result) {
//            System.out.println("新增数据成功");
//        }
        // DELETE
        sql = "delete from person where id=1";
        result = statement.execute(sql);

        // 关闭资源
        JDBCHelper.release(resultSet, statement);


    }
}
