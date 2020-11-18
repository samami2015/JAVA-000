package wangsen.springboot.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ApplicationDataSourceHikariSingle implements CommandLineRunner {


    private static Logger logger = LoggerFactory.getLogger(ApplicationDataSourceHikariSingle.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        SpringApplication.run(ApplicationDataSourceHikariSingle.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("[run][获取连接： {}]", connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // select
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("select id,name from person limit 10");
        for (Map<String, Object> row : rows) {
            System.out.println("id:" + row.get("id") + ", name:" + row.get("name"));
        }
        // update
        jdbcTemplate.update("update person set name='test' where id=1");
        // delete
        jdbcTemplate.update("delete from person where id=1");
        // insert
        jdbcTemplate.execute("insert into person (name,score) values (concat('ws',last_insert_id()), 100)");
        System.out.println("执行完成..");
    }
}
