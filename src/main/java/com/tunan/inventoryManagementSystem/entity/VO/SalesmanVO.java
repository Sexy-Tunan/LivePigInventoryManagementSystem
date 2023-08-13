package com.tunan.inventoryManagementSystem.entity.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesmanVO {

    //销售员id，数据库主键，唯一标识
    private Long id;

    //角色名称
    private String role;

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

}
