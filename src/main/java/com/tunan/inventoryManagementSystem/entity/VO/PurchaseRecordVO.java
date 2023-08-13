package com.tunan.inventoryManagementSystem.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRecordVO {

    //表的主键
    @NotNull
    private Long id;

    //订单号，非空唯一(XS+订单生成的时间戳+自增数)
    private String recordNumber;

    //猪的功能
    private String pigFunction;

    //猪的品种
    private String pigType;

    //企业名字
    private String enterpriseName;

    //销售数量
    private Integer purchaseCount;

    //总重
    private Float pounds;

    //期望销售总价
    private Float expectedPurchasePrice;

    //实际销售总价
    private Float actualPurchasePrice;

    //订单创建的日期
    private LocalDateTime createDatetime;

    //销售员名字
    private String salesmanName;

}
