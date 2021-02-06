# ShardingSphere RAW JDBC XA


## 作业
&ensp;&ensp;&ensp;&ensp;基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式事务应用demo（二选一），提交到github

### 环境配置
&ensp;&ensp;&ensp;&ensp;启动两个MySQL5.7的docker镜像，下面命令直接复制运行即可：

```shell script
# 启动两个mysql
docker run --name mysql11 -p 3311:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_ROOT_HOST=% -d mysql:5.7
docker run --name mysql12 -p 3312:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_ROOT_HOST=% -d mysql:5.7

# 在11上创建数据库demo_ds_0，运行下面的SQL语句初始化数据库和表
docker exec -ti mysql11 mysql -u root -p

create database demo_ds_0;
use demo_ds_0;
CREATE TABLE IF NOT EXISTS t_order_0 (order_id BIGINT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS t_order_1 (order_id BIGINT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (order_id));
    

# 在12上创建数据库demo_ds_1，运行下面的SQL语句初始化数据库和表
docker exec -ti mysql12 mysql -u root -p

create database demo_ds_1;
use demo_ds_1;
CREATE TABLE IF NOT EXISTS t_order_0 (order_id BIGINT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS t_order_1 (order_id BIGINT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (order_id));
```

### 程序配置
### shardingSphere数据库配置文件
&ensp;&ensp;&ensp;&ensp;如上面的docker数据库配置，这里设置了两个数据库，各自有两张表

```yaml
dataSources:
  ds_0: !!com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3311/demo_ds_0?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
    autoCommit: false
  ds_1: !!com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3312/demo_ds_1?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
    autoCommit: false

rules:
- !SHARDING
  tables:
    t_order:
      actualDataNodes: ds_${0..1}.t_order_${0..1}
      databaseStrategy:
        standard:
          shardingColumn: user_id
          shardingAlgorithmName: database_inline
      tableStrategy:
        standard:
          shardingColumn: order_id
          shardingAlgorithmName: t_order_inline
  bindingTables:
    - t_order

  shardingAlgorithms:
    database_inline:
      type: INLINE
      props:
        algorithm-expression: ds_${user_id % 2}
    t_order_inline:
      type: INLINE
      props:
        algorithm-expression: t_order_${order_id % 2}

props:
  sql-show: true
```



