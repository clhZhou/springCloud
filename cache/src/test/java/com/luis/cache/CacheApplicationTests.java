package com.luis.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class CacheApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setStringSerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
//        System.out.println(redisTemplate.getStringSerializer());
//        redisTemplate.opsForValue().set("name","周瑞");
//        System.out.println(redisTemplate.opsForValue().get("name"));
//        redisTemplate.opsForValue().set("primaryKey", UUID.randomUUID().toString());
        redisTemplate.opsForValue().set("i","10");
        System.out.println(redisTemplate.opsForValue().get("i"));
        redisTemplate.opsForValue().increment("i",1);
        System.out.println(redisTemplate.opsForValue().get("i"));
//        redisTemplate.opsForValue().increment("i",1.1);
//        System.out.println(redisTemplate.opsForValue().get("i"));
        redisTemplate.opsForValue().decrement("i",1);
        System.out.println(redisTemplate.opsForValue().get("i"));


    }

    @Test
    void incr(){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setStringSerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);

        long ttl = 100;
        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForValue().setIfAbsent("timeKey","value"+i,ttl, TimeUnit.SECONDS);
        }


    }

}
