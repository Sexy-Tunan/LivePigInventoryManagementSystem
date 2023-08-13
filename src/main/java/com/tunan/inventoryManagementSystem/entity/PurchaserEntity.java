package com.tunan.inventoryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaserEntity {

    //采购员id，数据库主键，唯一标识
    private Long id;

    //角色id
    private Integer roleId;

    //员工名字
    private String name;

    //员工手机号
    private String phoneNumber;

    //员工身份证号码
    private String identityNumber;

    //员工住址
    private String address;

    //入职时间
    private LocalDateTime onBoardingDatetime;

    //离职时间
    private LocalDateTime dimissionDatetime;

    //是否离职
    private Integer isDimission;

    public PurchaserEntity(Integer roleId, String name, String phoneNumber, String identityNumber, String address, LocalDateTime onBoardingDatetime, Integer isDimission) {
        this.roleId = roleId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.identityNumber = identityNumber;
        this.address = address;
        this.onBoardingDatetime = onBoardingDatetime;
        this.isDimission = isDimission;
    }

    public PurchaserEntity(Integer roleId, String name, String phoneNumber, String identityNumber, String address) {
        this.roleId = roleId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.identityNumber = identityNumber;
        this.address = address;
    }
}
