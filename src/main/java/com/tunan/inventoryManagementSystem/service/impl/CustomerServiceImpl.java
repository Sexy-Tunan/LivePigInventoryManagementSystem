package com.tunan.inventoryManagementSystem.service.impl;

import com.tunan.inventoryManagementSystem.dao.CustomerDao;
import com.tunan.inventoryManagementSystem.entity.CustomerEntity;
import com.tunan.inventoryManagementSystem.entity.VO.CustomerVO;
import com.tunan.inventoryManagementSystem.service.CustomerService;
import com.tunan.inventoryManagementSystem.utils.CommonBeanUtils;
import com.tunan.inventoryManagementSystem.utils.CommonStaticUtils;
import com.tunan.inventoryManagementSystem.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    private final RedisUtils redisUtils;

    private final CommonBeanUtils commonBeanUtils;

    public CustomerServiceImpl(CustomerDao customerDao, RedisUtils redisUtils, CommonBeanUtils commonBeanUtils) {
        this.customerDao = customerDao;
        this.redisUtils = redisUtils;
        this.commonBeanUtils = commonBeanUtils;
    }

    @Override
    public int getCounts() {
        return customerDao.count();
    }

    @Override
    public List<String> getCombobox() {

        return customerDao.getAllCustomerName();

    }

    @Override
    public List<CustomerVO> getCustomers(Integer pageNum, Integer pageSize) {

        if (pageNum == null || pageSize == null){
            return null;
        }
        if (pageNum < 0 || pageSize < 0){
            return null;
        }

        int offSet = 0;
        if (pageNum > 1){
            offSet = (pageNum - 1)*pageSize;
        }

        return customerDao.pageQueryCustomers(offSet, pageSize);
    }

    @Override
    public List<CustomerVO> getConditionalCustomer(Long id, String enterpriseName, String address, String contact, String phoneNumber, Integer pageNum, Integer pageSize) {

        if (id != null){
            CustomerVO oneResult = customerDao.getCustomerById(id);
            List<CustomerVO> customerVOS = new ArrayList<>();
            customerVOS.add(oneResult);
            return customerVOS;
        }
        if (enterpriseName != null){
            CustomerVO oneResult = customerDao.getCustomerByName(enterpriseName);
            List<CustomerVO> customerVOS = new ArrayList<>();
            customerVOS.add(oneResult);
            return customerVOS;
        }

        return customerDao.getCustomersByConditions(contact, address, phoneNumber);

    }

    @Override
    public boolean addCustomer(CustomerVO customerVO) {

        boolean phoneNumberIsRight = CommonStaticUtils.verifyPhoneNumber(customerVO.getPhoneNumber());
        if (!phoneNumberIsRight){
            return false;
        }
        boolean identityNumberIsRight = CommonStaticUtils.verifyIdentityNumber(customerVO.getIdentityNumber());
        if (!identityNumberIsRight){
            return false;
        }

        CustomerEntity customerEntity = commonBeanUtils.changeToCustomerEntity(customerVO);

        LocalDateTime now = LocalDateTime.now();

        customerEntity.setCreateDatetime(now);
        customerEntity.setUpdateDatetime(now);
        customerEntity.setIsDelete(0);

        // TODO 其实应该先对添加的经销商的企业名字在数据库查询，查看其是否添加过，如果添加过，只需要将数据库的is_delete字段值修改为1即可，然后对企业名字添加唯一性约束。
        int influencedRows = customerDao.addCustomer(customerEntity);
        if (influencedRows == 1){
            //TODO 如果添加供应商成功，那么应该将redis中的判断条件设为false，让其重新从数据库中查询数据并更新到redis中
            //TODO 或者我们将新添加的供应商手动添加到redis中
            CustomerVO newCustomer = customerDao.getCustomerByName(customerVO.getEnterpriseName());
            redisUtils.saveObject("customer:"+newCustomer.getId(),newCustomer.getEnterpriseName(),30);
            redisUtils.saveObject("customerId:"+newCustomer.getEnterpriseName(), newCustomer.getId().toString(),30);
            return true;
        }
        return false;

    }

    @Override
    public boolean updateCustomer(CustomerVO customerVO) {

        if (customerVO == null){
            return false;
        }

        CustomerEntity newCustomer = commonBeanUtils.changeToCustomerEntity(customerVO);
        CustomerVO oldCustomer = customerDao.getCustomerById(customerVO.getId());

        newCustomer.setUpdateDatetime(LocalDateTime.now());
        int influencedRow = customerDao.updateCustomerBaseMsg(newCustomer);

        if (influencedRow == 1){
            if ( ! newCustomer.getEnterpriseName().equals(oldCustomer.getEnterpriseName()) ){
                redisUtils.saveObject("customer:"+newCustomer.getId(),newCustomer.getEnterpriseName(),30);
                redisUtils.saveObject("customerId:"+newCustomer.getEnterpriseName(), newCustomer.getId().toString(),30);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean delCustomer(Long id) {
        if (id == null){
            return false;
        }
        return customerDao.delCustomerById(id) == 1;
    }

    @Override
    public boolean delCustomer(String enterpriseName) {

        if (enterpriseName == null || enterpriseName.equals("") ){
            return false;
        }

        return customerDao.delCustomerByName(enterpriseName) == 1;
    }
}
