package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.entity.SellingRecordEntity;
import com.tunan.inventoryManagementSystem.entity.VO.SellingRecordVO;

import java.util.List;

public interface SellingRecordService {
    //获取订单总数量
    int getCounts();
    //分页查询采购订单（按时间倒序）
    List<SellingRecordVO> pageQueryForSellingRecordASC(Integer pageNum, Integer pageSize, LoginUser loginUser);
    //分页查询采购订单（按时间顺序）
    List<SellingRecordVO> pageQueryForSellingRecord(Integer pageNum, Integer pageSize, LoginUser loginUser);
    //条件查询采购订单
    List<SellingRecordVO> getConditionalRecords(String recordNumber,     String enterpriseName,
                                                       String pigFunction,  String pigType,
                                                       String startDate,    String deadTime,
                                                       Integer pageNum,     Integer pageSize, LoginUser loginUser);
    //添加采购订单
    boolean addSellingRecord (SellingRecordVO sellingRecordVO, LoginUser loginUser);
    //删除采购订单（根据主键id）
    boolean delSellingRecord (Long id);
    //删除采购订单（根据订单号）
    boolean delSellingRecord (String recordNumber);
    //更新采购订单
    boolean updateSellingRecord(SellingRecordVO sellingRecordVO, LoginUser loginUser);
}
