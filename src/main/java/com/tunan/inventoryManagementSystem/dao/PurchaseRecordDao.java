package com.tunan.inventoryManagementSystem.dao;

import com.tunan.inventoryManagementSystem.entity.PurchaseRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PurchaseRecordDao {

    //分页查询销售记录单
    List<PurchaseRecordEntity> pageQuery(Integer offset, Integer pageSize, Integer sortType);

    //分页查询销售记录，查询结果受登录用户的身份限制，除非是管理员。
    List<PurchaseRecordEntity> pageQueryLimitToRole(Integer offset, Integer pageSize, Integer sortType, Long workerId);

    //条件查询销售记录，但是受登录用户的身份影响
    List<PurchaseRecordEntity> pageQueryByBaseConditionLimitToRole(Integer pigFunctionId, Integer pigTypeId,
                                                        Integer pageNum, Integer offset, Integer roleId, Long workerId);

    //根据始末时间查询某段时间范围内的所有记录
    List<PurchaseRecordEntity> getPurchaseRecordsByTime(LocalDateTime startDate, LocalDateTime deadTime, Integer roleId, Long purchaserId);


    //根据订单号查询销售订单
    PurchaseRecordEntity getPurchaseRecordByRecordNumber(String recordNumber, Integer roleId, Long purchaserId);

    //根据订单的数据库主键来查询其对应订单的worker的id
    Long getWorkerIdById(Long id);

    //根据purchaserId查找订单
    List<PurchaseRecordEntity> getPurchaseRecordByPurchaserId(Long purchaserId);

    //根据订单的数据库主键来查询其对应订单
    PurchaseRecordEntity getPurchaseRecordById(Long id);

    //根据订单的数据库主键来查询其对应订单，但是受角色身份影响，判断该用户身份是否有权限查看这条信息。
    PurchaseRecordEntity getPurchaseRecordByIdLimitToRole(Long id, Integer roleId, Long purchaserId);

    //根据经销商id查询对应销售单
    List<PurchaseRecordEntity> getPurchaseRecordByProviderId(Long providerId, Integer roleId, Long purchaserId);

    //根据订单号码删除订单
    int delPurchaseRecordByRecordNumber(String recordNumber);

    //根据订单ID(数据库主键)删除订单
    int delPurchaseRecordByID(Long id);

    //添加销售记录
    int addPurchaseRecord(PurchaseRecordEntity purchaseRecordEntity);

    //更改销售记录
    int updatePurchaseRecord(PurchaseRecordEntity purchaseRecordEntity);

    int count();

}
