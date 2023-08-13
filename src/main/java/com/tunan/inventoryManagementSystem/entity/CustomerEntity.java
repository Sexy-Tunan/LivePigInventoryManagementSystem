package com.tunan.inventoryManagementSystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    //客户id
    private Long id;

    //企业名字
    private String enterpriseName;

    //企业地址
    private String address;

    //企业联系人(负责人)
    private String contact;

    //身份证号码
    private String identityNumber;

    //联系电话
    private String phoneNumber;

    //创建时间
    private LocalDateTime createDatetime;

    //更新时间
    private LocalDateTime updateDatetime;

    //是否删除
    private Integer isDelete;

    public CustomerEntity(Long id, String enterpriseName, String address, String contact, String phoneNumber) {
        this.id = id;
        this.enterpriseName = enterpriseName;
        this.address = address;
        this.contact = contact;
        this.phoneNumber = phoneNumber;
    }
}
