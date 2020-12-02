package com.geekbang.week07.service;


import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class OrderShardingService {
    @Autowired
    private DataSource dataSource;

    public List<Map<String, Object>> getOrders() throws SQLException {
        String sql = "select * from electronic_business.orders";
        final Connection connection = dataSource.getConnection();
        final PreparedStatement ps = connection.prepareStatement(sql);
        final ResultSet resultSet = ps.executeQuery();
        List<Map<String, Object>> result = new ArrayList<>();
        while (resultSet.next()){
            Map<String, Object> map = new HashMap<>();
            final int id = resultSet.getInt("id");
            final String user_id = resultSet.getString("user_id");
            final String number = resultSet.getString("number");
            final String price = resultSet.getString("price");
            final String biz = resultSet.getString("biz");
            final String create_time = resultSet.getString("create_time");


            map.put("id", id);
            map.put("user_id", user_id);
            map.put("number", number);
            map.put("price", price);
            map.put("biz", biz);
            map.put("create_time", create_time);

            result.add(map);
        }
        return result;
    }
    public int saveOrder() throws SQLException {
        String sql = "insert into electronic_business.orders(`user_id`,`number`,`price`," +
                "`biz`,`create_time`) values(?,?,?,?,now())";
        final Connection connection = dataSource.getConnection();
        final PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, UUID.randomUUID().toString());
        ps.setInt(2, 1);
        ps.setBigDecimal(3, new BigDecimal(300));
        ps.setString(4, "Test Product with sharingshere case 3");
        ps.addBatch();
        ps.executeBatch();
        ps.close();
        connection.close();
        return 1;
    }
}
