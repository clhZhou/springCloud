package com.luis.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

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

}
