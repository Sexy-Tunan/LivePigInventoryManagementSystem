package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.entity.VO.PurchaseRecordVO;

import java.util.List;

public interface PurchaseRecordService {
    //获取订单总数量
    int getCounts();
    //分页查询采购订单（按时间倒序）
    List<PurchaseRecordVO> pageQueryForPurchaseRecordASC(Integer pageNum, Integer pageSize, LoginUser loginUser);
    //分页查询采购订单（按时间顺序）
    List<PurchaseRecordVO> pageQueryForPurchaseRecord(Integer pageNum, Integer pageSize, LoginUser loginUser);
    //条件查询采购订单
    List<PurchaseRecordVO> getConditionalRecords(String recordNumber,     String customerName,
                                                       String pigFunction,  String pigType,
                                                       String startDate,    String deadTime,
                                                       Integer pageNum,     Integer pageSize, LoginUser loginUser);
    //添加采购订单
    boolean addPurchaseRecord (PurchaseRecordVO purchaseRecordVO, LoginUser loginUser);
    //删除采购订单（根据主键id）
    boolean delPurchaseRecord (Long id);
    //删除采购订单（根据订单号）
    boolean delPurchaseRecord (String recordNumber);
    //更新采购订单
    boolean updatePurchaseRecord(PurchaseRecordVO purchaseRecordVO, LoginUser loginUser);
}
