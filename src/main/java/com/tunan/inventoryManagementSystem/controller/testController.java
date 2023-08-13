package com.tunan.inventoryManagementSystem.controller;


import com.tunan.inventoryManagementSystem.dao.CommonDao;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.PigFunction;
import com.tunan.inventoryManagementSystem.entity.PigType;
import com.tunan.inventoryManagementSystem.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("/test")
public class testController {

    @Autowired
    CommonDao commonDao;

    @RequestMapping("getAllRole")
    public Result<List<Role>> getAllRole(){
        List<Role> allRole = commonDao.getAllRole();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,allRole);
    }

    @RequestMapping("getAllFunction")
    public Result<List<PigFunction>> getAllFunction(){
        List<PigFunction> all = commonDao.getAllPigFunction();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,all);
    }

    @RequestMapping("getAllType")
    public Result<List<PigType>> getAllType(){
        List<PigType> all = commonDao.getAllPigType();

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,all);
    }

    @RequestMapping("getMarketSellingPrice")
    public Result<Float> getMarketSellingPrice(){
        Float price = commonDao.getMarketSellingPrice(1,1);

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,price);
    }


    @RequestMapping("getMarketPurchasePrice")
    public Result<Float> getMarketPurchasePrice(){
        Float price = commonDao.getMarketPurchasePrice(1,1);

        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,price);
    }
}
