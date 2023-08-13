package com.tunan.inventoryManagementSystem.dao;


import com.tunan.inventoryManagementSystem.entity.ProviderEntity;
import com.tunan.inventoryManagementSystem.entity.VO.ProviderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProviderDao {

    //获取所有供应商的名字
    List<String> getAllProviderName();

    //获取所有供应商
    List<ProviderVO> getAllProvider();

    //分页查询供应商信息
    List<ProviderVO> pageQueryProviders(Integer offset, Integer pageSize);

    //根据企业名字查询供应商
    ProviderVO getProviderByName(String enterpriseName);


    //根据企业名字获取供应商ID
    Long getIdByEnterpriseName(String enterpriseName);


    //根据id数据库主键查询
    ProviderVO getProviderById(Long id);

    //根据企业的负责人查询供应商(返回结果为list，因为可能重名)
    List<ProviderVO> getProvidersByContact(String contact);

    //根据条件查询供应商
    List<ProviderVO> getProvidersByConditions(String contact,String address, String phoneNumber);

    //添加供应商
    int addProvider(ProviderEntity providerEntity);

    //删除供应商
    int delProviderById(Long id);
    int delProviderByName(String enterpriseName);

    //修改供应商的基本信息
    int updateProviderBaseMsg(ProviderEntity providerEntity);

    int count();



}
