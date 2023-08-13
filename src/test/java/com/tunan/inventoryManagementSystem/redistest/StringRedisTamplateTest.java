package com.tunan.inventoryManagementSystem.redistest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class StringRedisTamplateTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test(){
        stringRedisTemplate.opsForValue().set("user","caigou");
    }

}
