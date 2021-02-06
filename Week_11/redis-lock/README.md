# 基于Redis封装分布式数据操作
***
## 作业要求
- [x] （必做）基于Redis封装分布式数据操作：
 - [x] 1）在Java中实现一个简单的分布式锁；
 - [x] 2）在Java中实现一个分布式计数器，模拟减库存。

## 运行说明
&ensp;&ensp;&ensp;&ensp;直接运行主函数：DemoApplication， 相应的测试信息会进行打印

## 实现思路
&ensp;&ensp;&ensp;&ensp;大致是使用redis分布式锁，对库存操作进行保护

&ensp;&ensp;&ensp;&ensp;每当对库存进行操作时，尝试获取redis锁后进行后续操作

### Redis分布式锁
- 加锁：在Redis的2.6.12及以后中,使用 set key value [NX] [EX] 命令，达到lua脚本的原子性操作
- 释放锁：使用lua脚本进行解锁
