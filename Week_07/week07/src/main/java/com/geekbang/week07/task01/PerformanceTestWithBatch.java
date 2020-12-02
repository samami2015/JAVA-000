package com.geekbang.week07.task01;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class PerformanceTestWithBatch {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        log.info("start：" + String.valueOf(System.currentTimeMillis()));
        final int insert = insert();
        log.info("end：" + String.valueOf(System.currentTimeMillis()));
        log.info("成功插入：" + insert);
    }

    //批量导入
    public static int insert() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/electronic_business?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root", "123456");
        connection.setAutoCommit(false); //设置手动提交
        String sql = "insert into electronic_business.orders(`user_id`,`number`,`price`," +
                "`biz`,`create_time`) values(?,?,?,?,now())";
        final PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 1; i <= 1000000; i++) {
            ps.setInt(1, i);
            ps.setInt(2, i);
            ps.setBigDecimal(3, new BigDecimal(i));
            ps.setString(4, "test product without shardingshere" + i);
            ps.addBatch();
        }
        final int[] ints = ps.executeBatch();
        connection.commit();
        ps.close();
        connection.close();
        return ints.length;
    }
}
