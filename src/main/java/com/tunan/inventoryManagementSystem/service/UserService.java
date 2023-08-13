package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.entity.VO.UserVO;

import java.util.Map;


public interface UserService {

    Result<Map<String,String>> loginVerification(UserVO userVO);

    Map<String,String> refreshJWT(String token);

    LoginUser getLoginUser();
}
