package com.tunan.inventoryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 供货商实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderEntity {

    //数据库唯一标识的主键
    private Long id;

    //企业名字
    private String enterpriseName;

    //企业地址
    private String address;

    //联系人(也可以说是公司法人)
    private String contact;

    //联系人的身份证号码
    private String identityNumber;

    //联系电话
    private String phoneNumber;

    //创建时间
    private LocalDateTime createDatetime;

    //更新日期
    private LocalDateTime updateDatetime;

    //是否被删除
    private Integer isDelete;

    public ProviderEntity(Long id, String enterpriseName, String address, String contact, String phoneNumber,String contactIdentityNumber,LocalDateTime createDatetime) {
        this.id = id;
        this.enterpriseName = enterpriseName;
        this.address = address;
        this.contact = contact;
        this.phoneNumber = phoneNumber;
        this.identityNumber = contactIdentityNumber;
        this.createDatetime = createDatetime;
    }
}
