package com.tunan.inventoryManagementSystem.dao;

import com.tunan.inventoryManagementSystem.entity.PurchaserEntity;
import com.tunan.inventoryManagementSystem.entity.VO.PurchaserVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Mapper
public interface PurchaserDao {

    //根据采购员表的主键查询详细信息
    PurchaserEntity getPurchaserById(Long id);

    //分页查询采购员
    List<PurchaserEntity> pageQueryPurchasers(Integer offset, Integer pageSize);

    //获取所有采购员
    List<PurchaserEntity> getAllPurchaser();

    //根据名字查抄销售员
    List<PurchaserEntity> getPurchaserByName(String name);

    //根据名字查找销售员的ID
    List<Long> getPurchaserIdByName(String name);

    //模糊查询
    List<PurchaserEntity> fuzzyQuery(String phoneNumber, String address);

    //添加采购员
    int addPurchaser(PurchaserEntity purchaserEntity);

    //删除采购员
    int delPurchaser(Long id, LocalDateTime now);

    //修改采购员
    int updatePurchaser(PurchaserVO purchaserVO);

    int count();

}
