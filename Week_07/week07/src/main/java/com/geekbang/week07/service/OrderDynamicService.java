package com.geekbang.week07.service;

import com.geekbang.week07.annotation.Database;
import com.geekbang.week07.config.DynamicDataSourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderDynamicService {
    @Autowired
    private DynamicDataSourceConfig dynamicDataSourceConfig;

    private JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dynamicDataSourceConfig.determineTargetDataSource());
    }

    @Database()
    public List<Map<String, Object>> getOrders(){
        String sql = "select * from electronic_business.orders";
        final JdbcTemplate jdbcTemplate = getJdbcTemplate();
        return jdbcTemplate.queryForList(sql);
    }

    @Database(readOnly = false)
    public int saveOrder(){
        String sql = "insert into electronic_business.orders(`user_id`,`number`,`price`,`biz`,`create_time`) values(?,?,?,?,now())";
        Object[] params = new Object[]{UUID.randomUUID().toString(), 1, new BigDecimal(200), "Test Product with sharingshere case 2"};
        final JdbcTemplate jdbcTemplate = getJdbcTemplate();
        return jdbcTemplate.update(sql, params);
    }

}
