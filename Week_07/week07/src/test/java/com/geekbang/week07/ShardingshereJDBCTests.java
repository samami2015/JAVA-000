package com.geekbang.week07;

import com.geekbang.week07.service.OrderDynamicService;
import com.geekbang.week07.service.OrderService;
import com.geekbang.week07.service.OrderShardingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class ShardingshereJDBCTests {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDynamicService orderDynamicService;
    @Autowired
    private OrderShardingService orderShardingService;

    /**
     * 多数据源，手动
     */
    @Test
    void work2_1() {
        int rt = orderService.saveOrder();
        log.info("插入" + rt + "条数据");
        log.info("-------");
        List<Map<String, Object>> maps2 = orderService.getOrders();
        for (Map<String, Object> map : maps2) {
            log.info(map.get("id") + ">>>" + map.get("biz"));
        }
    }

    /**
     * 多数据源[master, slave1, slave2]，自动
     */
    @Test
    void work2_2() {
        int rt = orderDynamicService.saveOrder();
        log.info("插入" + rt + "条数据");
        log.info("-------");
        List<Map<String, Object>> maps2 = orderDynamicService.getOrders();
        for (Map<String, Object> map : maps2) {
            log.info(map.get("id") + ">>>" + map.get("item_name"));
        }
    }

    /**
     * Sharding-Jdbc
     */
    @Test
    void work3() throws SQLException {
        int rt = orderShardingService.saveOrder();
        log.info("插入" + rt + "条数据");
        log.info("-------");
        List<Map<String, Object>> maps2 = orderShardingService.getOrders();
        for (Map<String, Object> map : maps2) {
            log.info(map.get("id") + ">>>" + map.get("item_name"));
        }
    }
}
