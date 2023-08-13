package com.tunan.inventoryManagementSystem.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderVO {

    //供应商id
    @NotNull
    private Long id;

    //供应商企业名字
    private String enterpriseName;

    //企业联系人(负责人)
    private String contact;

    //联系电话
    private String phoneNumber;

    //负责人身份证号码
    private String identityNumber;

    //企业地址
    private String address;

    //创建时间
    private LocalDateTime createDatetime;

}
