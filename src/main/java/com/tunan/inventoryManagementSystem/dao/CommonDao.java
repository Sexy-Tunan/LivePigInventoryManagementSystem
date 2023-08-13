package com.tunan.inventoryManagementSystem.dao;


import com.tunan.inventoryManagementSystem.entity.PigFunction;
import com.tunan.inventoryManagementSystem.entity.PigType;
import com.tunan.inventoryManagementSystem.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonDao {

    //市场销售价格
    float getMarketSellingPrice(Integer pigFunctionId, Integer pigTypeId);

    //市场采购价格
    float getMarketPurchasePrice(Integer pigFunctionId, Integer pigTypeId);

    //获取猪的功能
    List<PigFunction> getAllPigFunction();

    //获取所有猪的种类
    List<PigType> getAllPigType();

    //获取所有角色
    List<Role> getAllRole();

}
