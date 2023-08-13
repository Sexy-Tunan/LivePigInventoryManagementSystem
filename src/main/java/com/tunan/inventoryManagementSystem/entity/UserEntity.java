package com.tunan.inventoryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    //数据库主键的唯一标识
    private Long id;

    //用户名
    private String username;

    //密码
    private String password;

    //身份/角色
    private Integer roleId;

    //用户的工号(例如销售的工号就是销售员表的ID主键)
    private Long workerId;

    //昵称
    private String nickName;

    //真实名字
    private String trueName;

    //创建时间
    private LocalDateTime createDatetime;

    //更新时间
    private LocalDateTime updateDatetime;

    //是否删除
    private Integer isDelete;

}
