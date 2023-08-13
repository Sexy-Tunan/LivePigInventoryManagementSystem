package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.UserVO;
import com.tunan.inventoryManagementSystem.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(tags = "用户登录与退出、权限管理")
@RequestMapping("/user")
public class UserController {

    private final UserService  userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/4/16 15:18
     * @Param: 
     * @Return: 
     **/
    @PostMapping("/login")
    @ResponseBody
    public Result<Map<String,String>> LoginVerification(@RequestBody UserVO userVO){
        return userService.loginVerification(userVO);
    }

    @GetMapping("/refreshJWT")
    @ResponseBody
    public Result<Map<String,String>> refreshJWT(
            @RequestParam("token") String token
    ){
        Map<String, String> refreshedJWT = userService.refreshJWT(token);
        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,refreshedJWT);
    }
}
