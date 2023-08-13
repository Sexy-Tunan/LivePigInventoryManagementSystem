package com.tunan.inventoryManagementSystem.dao;


import com.tunan.inventoryManagementSystem.entity.CustomerEntity;
import com.tunan.inventoryManagementSystem.entity.VO.CustomerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerDao {

    //获取所有供应商的名字
    List<String> getAllCustomerName();

    //获取所有供应商
    List<CustomerVO> getAllCustomer();

    //分页查询供应商信息
    List<CustomerVO> pageQueryCustomers(Integer offset, Integer pageSize);

    //根据企业名字查询供应商
    CustomerVO getCustomerByName(String enterpriseName);

    Long getIdByEnterpriseName(String enterpriseName);

    //根据id数据库主键查询
    CustomerVO getCustomerById(Long id);

    //根据企业的负责人查询供应商(返回结果为list，因为可能重名)
    List<CustomerVO> getCustomersByContact(String contact);

    //根据条件查询供应商
    List<CustomerVO> getCustomersByConditions(String contact,String address, String phoneNumber);

    //添加供应商
    int addCustomer(CustomerEntity customerEntity);

    //删除供应商
    int delCustomerById(Long id);
    int delCustomerByName(String enterpriseName);

    //修改供应商的基本信息
    int updateCustomerBaseMsg(CustomerEntity customerEntity);

    int count();


}
