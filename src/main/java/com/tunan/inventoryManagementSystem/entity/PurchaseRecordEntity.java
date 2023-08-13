package com.tunan.inventoryManagementSystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRecordEntity {

    //表的主键
    private Long id;

    //订单号，非空唯一
    private String recordNumber;

    //猪的功能
    private Integer pigFunctionId;

    //猪的品种
    private Integer pigTypeId;

    //企业名字
    private Long providerId;

    //采购数量
    private Integer purchaseCount;

    //总重
    private Float pounds;

    //期望采购总价
    private Float expectedPurchasePrice;

    //实际采购总价
    private Float actualPurchasePrice;

    //采购员id
    private Long purchaserId;

    //销售时间
    //数据库的datetime接受特定格式的字符串，这样存进数据库还是datetime类型的值。
    private LocalDateTime createDatetime;

    //修改时间
    private LocalDateTime updateDatetime;

    //订单是否被退回
    private Integer isReturn;

    //订单是否被删除
    private Integer isDelete;

    public PurchaseRecordEntity(Long id, String recordNumber, Integer pigFunctionId, Integer pigTypeId, Long providerId, Integer purchaseCount, Float pounds, Float actualPurchasePrice, Long purchaserId) {
        this.id = id;
        this.recordNumber = recordNumber;
        this.pigFunctionId = pigFunctionId;
        this.pigTypeId = pigTypeId;
        this.providerId = providerId;
        this.purchaseCount = purchaseCount;
        this.pounds = pounds;
        this.actualPurchasePrice = actualPurchasePrice;
        this.purchaserId = purchaserId;
    }
}
