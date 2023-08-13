package com.tunan.inventoryManagementSystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellingRecordEntity {

    //表的主键
    private Long id;

    //订单号，非空唯一
    private String recordNumber;

    //猪的功能
    private Integer pigFunctionId;

    //猪的品种
    private Integer pigTypeId;

    //企业名字
    private Long customerId;

    //销售数量
    private Integer sellingCount;

    //总重
    private Float pounds;

    //期望销售总价
    private Float expectedSellingPrice;

    //实际销售总价
    private Float actualSellingPrice;

    //销售员id
    private Long salesmanId;

    //创建时间
    private LocalDateTime createDatetime;

    //修改时间
    private LocalDateTime updateDatetime;

    //订单是否被删除
    private Integer isDelete;

    //订单是否被退回
    private Integer isReturn;

    public SellingRecordEntity(Long id, String recordNumber, Integer pigFunctionId, Integer pigTypeId, Long customerId, Integer sellingCount, Float pounds, Float actualSellingPrice, Long salesmanId) {
        this.id = id;
        this.recordNumber = recordNumber;
        this.pigFunctionId = pigFunctionId;
        this.pigTypeId = pigTypeId;
        this.customerId = customerId;
        this.sellingCount = sellingCount;
        this.pounds = pounds;
        this.actualSellingPrice = actualSellingPrice;
        this.salesmanId = salesmanId;
    }
}
