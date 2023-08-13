package com.tunan.inventoryManagementSystem.dao;

import com.tunan.inventoryManagementSystem.entity.PurchaseRecordEntity;
import com.tunan.inventoryManagementSystem.entity.SellingRecordEntity;

import java.util.List;

public interface ReturnedRecordDao {

    //分页查询有关销售的退货订单
    List<SellingRecordEntity> pageQueryReturnedSellingRecord(Integer offset, Integer pageSize);

    //分页查询有关采购的退货订单
    List<PurchaseRecordEntity> pageQueryReturnedPurchaseRecord(Integer offset, Integer pageSize);

    //恢复删除的退货订单
    int recoverDeletedSellingRecord(Integer id);
    int recoverDeletedPurchaseRecord(Integer id);

}
