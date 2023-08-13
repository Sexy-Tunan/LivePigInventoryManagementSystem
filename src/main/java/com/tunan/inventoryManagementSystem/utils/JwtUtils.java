package com.tunan.inventoryManagementSystem.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * 自定义封装的JWt工具类
 */
public class JwtUtils {


    //默认有效时间
    public static final Long DEFAULT_EXPIRATION_TIME = 60 * 30 *1000L;// 60 * 30 *1000（单位是毫秒）  半个小时
    //默认加密密钥
    public static final String JWT_SECRET_KEY = "caigou";
    //设置签发人
    public static final String ISS = "tunan";

    /**
     * @Description: 使用Java自带的UUID类生成UUID
     * @Author: CaiGou
     * @Date: 2023/4/18 21:40
     * @Return:
     **/
    public static String getUUID(){
        //这里使用jdk的方法生成UUID主键作为jwt令牌的唯一标识
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/18 21:40
     * @Param: subject token中要存放的数据（json格式）
     * @Return:
     **/
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/18 21:42
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @Return:
     **/
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        //这里是获取当前系统的一个时间戳，然后
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtils.DEFAULT_EXPIRATION_TIME;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);


        return Jwts.builder()
                .setAudience("")            //设置接收人信息。
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer(ISS)     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate); //设置过期时间
    }


    /**
     * @Description: 获取JWT
     * @Author: CaiGou
     * @Date: 2023/4/18 21:39
     * @Param:
     * @Return:
     **/
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) {
//        String jwt = createJWT("2123");
        Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyOTY2ZGE3NGYyZGM0ZDAxOGU1OWYwNjBkYmZkMjZhMSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTYzOTk2MjU1MCwiZXhwIjoxNjM5OTY2MTUwfQ.NluqZnyJ0gHz-2wBIari2r3XpPp06UMn4JS2sWHILs0");
        String subject = claims.getSubject();
        System.out.println(subject);
//        System.out.println(claims);
    }

    /**
     * @Description: 生成加密后的秘钥 secretKey
     * @Author: CaiGou
     * @Date: 2023/4/18 21:39
     * @Param:
     * @Return:
     **/
    public static SecretKey generalKey() {
        //使用本地密码解码
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtils.JWT_SECRET_KEY);
        //根据给定的字节数组长度使用AES加密算法构造密钥。。
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * @Description: 解析
     * @Author: CaiGou
     * @Date: 2023/4/18 21:39
     * @Param:
     * @Return:
     **/
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
