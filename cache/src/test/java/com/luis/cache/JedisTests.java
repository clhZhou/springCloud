package com.luis.cache;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

/**
 * @author luis
 * @date 2020/12/1
 */

@SpringBootTest             
public class JedisTests {

    @Test
    void contextLoads() {
//        JedisDbUtil jedisDbUtil = new JedisDbUtil();
//        jedisDbUtil.set("test1","123",1,false,null);
//        jedisDbUtil.get("test", 1).toString();

        Jedis jedis = new Jedis("192.168.0.200",6379);
        int num = 15;
        jedis.select(num);
        jedis.set("key"+num,"value"+num);

        System.out.println(jedis.get("key"+num));
    }
}
