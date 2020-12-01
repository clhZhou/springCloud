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
        Jedis jedis = new Jedis("192.168.0.200",6379);
//        int num = 15;
//        jedis.select(num);
////        jedis.set("key"+num,"0");
//        jedis.incr("key15");
//
//        if(jedis.get("key"+num).equals("5")){
//             jedis.set("key15","0");
//        }
//        System.out.println(jedis.get("key"+num));
        boolean keyExist = jedis.exists("nxKey");
        System.out.println(keyExist);
        long ttl1 = jedis.ttl("nxKey");
        System.out.println(ttl1);
        if(keyExist){
            jedis.del("nxKey");
        }
        long ttl = 100;
        jedis.set("nxKey","nxValue","NX","EX",100);

    }
}
