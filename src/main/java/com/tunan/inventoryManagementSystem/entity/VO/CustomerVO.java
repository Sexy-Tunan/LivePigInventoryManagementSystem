package com.tunan.inventoryManagementSystem.entity.VO;

import com.tunan.inventoryManagementSystem.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVO {

    //客户id
    @NotNull
    private Long id;

    //企业名字
    private String enterpriseName;

    //企业联系人(负责人)
    private String contact;

    //联系电话
    private String phoneNumber;

    //身份证号码
    private String identityNumber;

    //企业地址
    private String address;

    //创建时间
    private LocalDateTime createDatetime;

}
