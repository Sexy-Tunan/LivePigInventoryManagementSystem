package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.entity.VO.CustomerVO;

import java.util.List;

public interface CustomerService {
    //获取经销商的总数量
    int getCounts();
    //获取所有经销商的名字
    List<String> getCombobox();
    //分页查询经销商
    List<CustomerVO> getCustomers(Integer pageNum, Integer pageSize);
    //按条件分页查询经销商
    List<CustomerVO> getConditionalCustomer
    (Long id, String enterpriseName, String address, String contact, String phoneNumber, Integer pageNum, Integer pageSize);
    //添加经销商
    boolean addCustomer(CustomerVO customerVO);
    //更新经销商
    boolean updateCustomer(CustomerVO customerVO);
    //删除经销商（根据数据库主键）
    boolean delCustomer(Long id);
    //删除经销商（根据企业名字）
    boolean delCustomer(String enterpriseName);
}
