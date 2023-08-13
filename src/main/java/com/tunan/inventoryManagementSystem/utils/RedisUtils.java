package com.tunan.inventoryManagementSystem.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    //使用构造注入的方法，为属性自动注入对象。与@autowried一致，但是构造注入更加可靠
    @Autowired
    public RedisUtils (StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }


    public void saveObject(String key,Object value,Integer minutes){
        String jsonString = JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key,jsonString,minutes,TimeUnit.MINUTES);
    }

    public void saveObject(String key,Object value){
        String jsonString = JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key,jsonString);
    }


    public void saveValue(String key,String value, Integer minutes){
        stringRedisTemplate.opsForValue().set(key,value,minutes, TimeUnit.MINUTES);
    }

    //
    public <T> T getObject(String key, Class<T> clazz){

        String JsonObj = stringRedisTemplate.opsForValue().get(key);

        if (JsonObj == null){
            return null;
        }

        return JSON.parseObject(JsonObj, clazz);
    }

    public <E> void saveObjects(Map<String,E> map){
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        Iterator<Map.Entry<String, E>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,E> entry = it.next();
            saveObject(entry.getKey(),entry.getValue(),10);
        }
    }

    public Set<String> getKeysLike(String pattern){
        return stringRedisTemplate.keys(pattern);
    }


}
