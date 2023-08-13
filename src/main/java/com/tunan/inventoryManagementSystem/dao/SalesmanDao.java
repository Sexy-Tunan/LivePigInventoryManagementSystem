package com.tunan.inventoryManagementSystem.dao;

import com.tunan.inventoryManagementSystem.entity.SalesmanEntity;
import com.tunan.inventoryManagementSystem.entity.VO.SalesmanVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SalesmanDao {


    //根据销售员表的主键查询详细信息
    SalesmanEntity getSalesmanById(Long id);

    //分页查询销售员
    List<SalesmanEntity> pageQuerySalesmans(Integer offset, Integer pageSize);

    //获取所有销售员
    List<SalesmanEntity>  getAllSalesman();

    //根据名字查抄销售员
    List<SalesmanEntity> getSalesmanByName(String name);

    //根据名字查找销售员的ID
    List<Long> getSalesmanIdByName(String name);

    //模糊查询
    List<SalesmanEntity> fuzzyQuery(String phoneNumber, String address);

    //添加销售员
    int addSalesman(SalesmanEntity salesmanEntity);

    //删除销售员
    int delSalesman(Long id, LocalDateTime now);

    //修改销售员
    int updateSalesman(SalesmanVO salesmanVO);

    int count();

}
