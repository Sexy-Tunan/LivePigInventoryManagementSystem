package com.tunan.inventoryManagementSystem.service.impl;

import com.mysql.cj.util.StringUtils;
import com.tunan.inventoryManagementSystem.dao.ProviderDao;
import com.tunan.inventoryManagementSystem.entity.ProviderEntity;
import com.tunan.inventoryManagementSystem.entity.VO.ProviderVO;
import com.tunan.inventoryManagementSystem.service.ProviderService;
import com.tunan.inventoryManagementSystem.utils.CommonBeanUtils;
import com.tunan.inventoryManagementSystem.utils.CommonStaticUtils;
import com.tunan.inventoryManagementSystem.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderDao providerDao;

    private final RedisUtils redisUtils;

    private final CommonBeanUtils commonBeanUtils;

    public ProviderServiceImpl(ProviderDao providerDao, RedisUtils redisUtils, CommonBeanUtils commonBeanUtils) {
        this.providerDao = providerDao;
        this.redisUtils = redisUtils;
        this.commonBeanUtils = commonBeanUtils;
    }

    @Override
    public int getCounts() {
        return providerDao.count();
    }

    /**
     * @Description: 获取供应商下拉列表
     * @Author: CaiGou
     * @Date: 2023/4/22 16:30
     * @Param:
     * @Return:
     **/
    @Override
    public List<String> getCombobox(){

        return providerDao.getAllProviderName();

    }

    /**
     * @Description: 分页查询供应商信息
     * @Author: CaiGou
     * @Date: 2023/4/22 16:33
     * @Param:
     * @Return:
     **/
    @Override
    public List<ProviderVO> getProviders(Integer pageNum, Integer pageSize){

        int offSet = 0;
        if (pageNum > 1){
            offSet = (pageNum - 1)*pageSize;
        }

        return providerDao.pageQueryProviders(offSet, pageSize);
    }

    /**
     * @Description: 条件查询供应商
     * @Author: CaiGou
     * @Date: 2023/4/22 18:13
     * @Param:
     * @Return:
     **/
    @Override
    public List<ProviderVO> getConditionalProvider(Long id, String enterpriseName, String address, String contact, String phoneNumber, Integer pageNum, Integer pageSize) {
        if (id != null){
            ProviderVO oneResult = providerDao.getProviderById(id);
            List<ProviderVO> providerVOS = new ArrayList<>();
            providerVOS.add(oneResult);
            return providerVOS;
        }
        if (enterpriseName != null){
            ProviderVO oneResult = providerDao.getProviderByName(enterpriseName);
            List<ProviderVO> providerVOS = new ArrayList<>();
            providerVOS.add(oneResult);
            return providerVOS;
        }
        if ( !StringUtils.isNullOrEmpty(contact) || !StringUtils.isNullOrEmpty(address) || !StringUtils.isNullOrEmpty(phoneNumber)){
            return providerDao.getProvidersByConditions(contact, address, phoneNumber);
            //TODO 还未确定是否需要做分页查询
        }
        return null;
    }

    /**
     * @Description: 添加供应商
     * @Author: CaiGou
     * @Date: 2023/4/22 16:38
     * @Param:
     * @Return:
     **/
    @Override
    public boolean addProvider(ProviderVO providerVO){
        //TODO 还没有进行手机号码和身份证号的校验
        boolean phoneNumberIsRight = CommonStaticUtils.verifyPhoneNumber(providerVO.getPhoneNumber());
        if (!phoneNumberIsRight){
            return false;
        }
        boolean identityNumberIsRight = CommonStaticUtils.verifyIdentityNumber(providerVO.getIdentityNumber());
        if (!identityNumberIsRight){
            return false;
        }

        ProviderEntity providerEntity = commonBeanUtils.changeToProviderEntity(providerVO);

        LocalDateTime now = LocalDateTime.now();

        providerEntity.setCreateDatetime(now);
        providerEntity.setUpdateDatetime(now);
        providerEntity.setIsDelete(0);

        int influencedRows = providerDao.addProvider(providerEntity);
        if (influencedRows == 1){
            //TODO 如果添加供应商成功，那么应该将redis中的判断条件设为false，让其重新从数据库中查询数据并更新到redis中
            //TODO 或者我们将新添加的供应商手动添加到redis中
            ProviderVO newProvider = providerDao.getProviderByName(providerVO.getEnterpriseName());
            redisUtils.saveObject("provider:"+newProvider.getId(),newProvider.getEnterpriseName(),30);
            redisUtils.saveObject("providerId:"+newProvider.getEnterpriseName(), newProvider.getId().toString(),30);
            return true;
        }
        return false;

    }

    /**
     * @Description: 修改供应商
     * @Author: CaiGou
     * @Date: 2023/4/22 16:54
     * @Param:
     * @Return:
     **/
    @Override
    public boolean updateProvider(ProviderVO providerVO){
        if (providerVO.getId() == null){
            return false;
        }


        //TODO 上面这两段代码的逻辑判断是不是放在controller层判断会比较好
        ProviderEntity newProvider = commonBeanUtils.changeToProviderEntity(providerVO);
        newProvider.setUpdateDatetime(LocalDateTime.now());
        ProviderVO oldProvider = providerDao.getProviderById(providerVO.getId());
        int influencedRow = providerDao.updateProviderBaseMsg(newProvider);

        if (influencedRow == 1){
            //TODO 修改供应商信息，如果修改了供应商名字，那么应该依据将原来的redis的数据删
            //TODO 或者直接将对应commonBeanUtils中的判断条件设置为false，这样就会从数据库中重新查询并存到redis中。
            if ( ! newProvider.getEnterpriseName().equals(oldProvider.getEnterpriseName()) ){
                //说明修改了enterpriseName,redis缓存要重新写入
                redisUtils.saveObject("provider:" + newProvider.getId(),newProvider.getEnterpriseName(),30);
                redisUtils.saveObject("providerId:"+newProvider.getEnterpriseName(), newProvider.getId().toString(),30);
            }
            return true;
        }
        return false;
    }

    /**
     * @Description: 删除provider，根据id
     * @Author: CaiGou
     * @Date: 2023/4/22 17:55
     * @Param:
     * @Return:
     **/
    @Override
    public boolean delProvider(Long id) {
        if(id == null){
            return false;
        }
        return providerDao.delProviderById(id) == 1;
    }

    /**
     * @Description: 删除provider，根据企业名字
     * @Author: CaiGou
     * @Date: 2023/4/22 17:55
     * @Param:
     * @Return:
     **/
    @Override
    public boolean delProvider(String enterpriseName) {
        if (enterpriseName == null){
            return false;
        }
        return providerDao.delProviderByName(enterpriseName) == 1;
    }
}
