package com.tunan.inventoryManagementSystem.dao;

import com.tunan.inventoryManagementSystem.entity.PurchaseRecordEntity;
import com.tunan.inventoryManagementSystem.entity.SellingRecordEntity;
import com.tunan.inventoryManagementSystem.entity.VO.SellingRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface SellingRecordDao {

    //分页查询销售记录单
    List<SellingRecordEntity> pageQuery(Integer offset, Integer pageSize, Integer sortType);

    List<SellingRecordEntity> pageQueryLimitToRole(Integer offset, Integer pageSize, Integer sortType, Long workerId);


    //条件查询销售记录单
    List<SellingRecordEntity> pageQueryByBaseCondition( Integer pigFunctionId, Integer pigTypeId,
                                                        Integer pageNum,    Integer offset,
                                                        Integer roleId,     Long workerId);

    //根据始末时间查询某段时间范围内的所有记录
    List<SellingRecordEntity> getSellingRecordsByTime(LocalDateTime startDate, LocalDateTime deadTime, Integer roleId, Long workerId);

    //根据订单的数据库主键来查询其对应订单的worker的id
    Long getWorkerIdById(Long id);

    //根据订单号查询销售订单
    SellingRecordEntity getSellingRecordByRecordNumber(String recordNumber,Integer roleId, Long workerId);

    //根据订单的数据库主键来查询其对应订单
    SellingRecordEntity getSellingRecordById(Long id);

    //根据经销商的企业ID查询对应的销售单
    List<SellingRecordEntity> getSellingRecordByCustomerId(Long customerId,Integer roleId, Long workerId);

    //根据purchaserId查找订单
    List<SellingRecordEntity> getSellingRecordBySalesmanId(Long salesmanId);


    //根据订单号码删除订单
    int delSellingRecordByRecordNumber(String recordNumber);

    //根据订单ID(数据库主键)删除订单
    int delSellingRecordById(Long id);

    //添加销售记录
    int addSellingRecord(SellingRecordEntity sellingRecordEntity);

    //更改销售记录
    int updateSellingRecord(SellingRecordEntity sellingRecordEntity);

    int count();
}

