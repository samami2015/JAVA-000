package com.geekbang.week07.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    @Qualifier("masterJdbcTemplate")
    private JdbcTemplate masterJdbcTemplate;
    @Autowired
    @Qualifier("slave1JdbcTemplate")
    private JdbcTemplate slave1JdbcTemplate;

    public List<Map<String, Object>> getOrders() {
        String sql = "select * from electronic_business.orders";
        return slave1JdbcTemplate.queryForList(sql);
    }

    public int saveOrder() {
        String sql = "insert into electronic_business.orders(`user_id`,`number`,`price`,`biz`,`create_time`) values(?,?,?,?,now())";
        Object[] params = new Object[]{UUID.randomUUID().toString(), 1, new BigDecimal(100), "Test Product with sharingshere case 1"};
        return masterJdbcTemplate.update(sql, params);
    }
}
