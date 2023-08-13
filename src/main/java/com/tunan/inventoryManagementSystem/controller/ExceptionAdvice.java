package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.domin.NullData;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.exception.ExpiredTokenException;
import com.tunan.inventoryManagementSystem.exception.PasswordException;
import com.tunan.inventoryManagementSystem.exception.UsernameException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 全局异常处理类
 * @Author: CaiGou
 * @Date: 2023/4/16 14:45
 **/
@RestController
@ControllerAdvice
public class ExceptionAdvice {


    /**
     * @Description: 数据库中不存在此用户名时报错
     * @Author: CaiGou
     * @Date: 2023/4/16 14:46
     * @Param:
     * @Return:
     **/
    @ResponseBody
    @ExceptionHandler(UsernameException.class)
    public Result<NullData> handleException(UsernameException e){
        return new Result<NullData>(e.code, e.message, new NullData());
    }

    /**
     * @Description: 当用户名的登录密码错误时，报错，处理
     * @Author: CaiGou
     * @Date: 2023/4/21 16:17
     * @Param:
     * @Return:
     **/
    @ResponseBody
    @ExceptionHandler(PasswordException.class)
    public Result<NullData> handlePasswordException(PasswordException e){
        //TODO
        return new Result<>(e.code,e.message,null);
    }

    @ResponseBody
    @ExceptionHandler(ExpiredTokenException.class)
    public Result<NullData> handleExpiredTokenException(ExpiredTokenException e){
        return new Result<>(e.code,e.message,null);
    }



}
