package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.entity.VO.ProviderVO;

import java.util.List;

public interface ProviderService {
    //获取供应商的总数量
    int getCounts();
    //获取所有供应商名字
    List<String> getCombobox();
    //分页查询供应商名字
    List<ProviderVO> getProviders(Integer pageNum, Integer pageSize);
    //按条件分页查询供应商信息
    List<ProviderVO> getConditionalProvider
    (Long id, String enterpriseName, String address, String contact, String phoneNumber, Integer pageNum, Integer pageSize);
    //添加供应商信息
    boolean addProvider(ProviderVO providerVO);
    //更新供应商信息
    public boolean updateProvider(ProviderVO providerVO);
    //删除供应商（按数据库主键id）
    public boolean delProvider(Long id);
    //删除供应商（按企业名字）
    public boolean delProvider(String enterpriseName);
}
