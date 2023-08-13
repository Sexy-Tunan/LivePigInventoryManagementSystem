package com.tunan.inventoryManagementSystem.logintest;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class login {
    @Test
    public void testPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
        String password = bCryptPasswordEncoder.encode("123456");
        System.out.println(password);
    }
}
