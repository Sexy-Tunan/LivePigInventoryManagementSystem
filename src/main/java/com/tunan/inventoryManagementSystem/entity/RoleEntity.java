package com.tunan.inventoryManagementSystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    //数据库对于角色的唯一标识
    private Integer id;

    //角色的名字
    private Integer roleName;

}
