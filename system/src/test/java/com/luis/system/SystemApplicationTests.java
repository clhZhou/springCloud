package com.luis.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@SpringBootTest
class SystemApplicationTests {

    @Autowired
    JdbcTemplate db1JdbcTemplate;
    @Autowired
    JdbcTemplate db2JdbcTemplate;

    @Test
    public void dbTest(){
        String sql="insert into user_info(name,age) values('mic1',18)";
        db1JdbcTemplate.execute(sql);
        db2JdbcTemplate.execute(sql);
    }

    @Test
    public void testQueue(){
        Queue<String> queue = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            queue.add(""+i);
        }
        while (true) {
            //poll 当queue为空时反回null
            System.out.println(queue.poll());
            //remove 当queue为空时报错 java.util.NoSuchElementException
            System.out.println(queue.remove());
        }
    }

    @Test
    public void hashMapUpdateDemo(){
        Map map = new HashMap();
        for (int i = 0; i < 10; i++) {
            map.put("key"+i,i);
        }
        Set<String> set = map.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Object key = iterator.next();
            //移除map中元素，出现 java.util.ConcurrentModificationException
            //iterator每次next会判断 HashMap.this.modCount != this.expectedModCount 不相等会抛出异常
            //这是快速检查并发bug的一种机制，fast-fail
            //建议使用 currentHashMap 或者 iterator 自带的 remove方法
            map.remove(key);
//            System.out.println(key);
//            System.out.println(map.get(key));
        }
    }

}
