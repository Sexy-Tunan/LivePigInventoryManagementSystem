package com.tunan.inventoryManagementSystem.dao;


import com.tunan.inventoryManagementSystem.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {


    UserEntity getUserByUsername(String username);

    List<String> getUserPermissions(Integer roleId);

}
