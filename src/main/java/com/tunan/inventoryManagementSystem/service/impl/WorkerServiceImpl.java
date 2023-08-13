package com.tunan.inventoryManagementSystem.service.impl;

import com.mysql.cj.util.StringUtils;
import com.tunan.inventoryManagementSystem.dao.PurchaserDao;
import com.tunan.inventoryManagementSystem.dao.SalesmanDao;
import com.tunan.inventoryManagementSystem.entity.PurchaserEntity;
import com.tunan.inventoryManagementSystem.entity.SalesmanEntity;
import com.tunan.inventoryManagementSystem.entity.VO.PurchaserVO;
import com.tunan.inventoryManagementSystem.entity.VO.SalesmanVO;
import com.tunan.inventoryManagementSystem.service.WorkerService;
import com.tunan.inventoryManagementSystem.utils.CommonBeanUtils;
import com.tunan.inventoryManagementSystem.utils.CommonStaticUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {

    private final PurchaserDao purchaserDao;

    private final SalesmanDao salesmanDao;

    private final CommonBeanUtils commonBeanUtils;

    private final static Integer PURCHASER_ROLE_ID = 3;
    private final static Integer SALESMAN_ROLE_ID = 4;

    public WorkerServiceImpl(PurchaserDao purchaserDao, SalesmanDao salesmanDao, CommonBeanUtils commonBeanUtils) {
        this.purchaserDao = purchaserDao;
        this.salesmanDao = salesmanDao;
        this.commonBeanUtils = commonBeanUtils;
    }


    public PurchaserVO getPurchaserById(Long id) {
        if (id == null){
            return null;
        }

        PurchaserEntity result = purchaserDao.getPurchaserById(id);
        return commonBeanUtils.changeToPurchaserVO(result);
    }

    @Override
    public int getPurchaserCounts() {
        return purchaserDao.count();
    }

    @Override
    public int getSalesmanCounts() {
        return salesmanDao.count();
    }

    @Override
    public List<PurchaserVO> pageQueryPurchasers(Integer pageNum, Integer pageSize) {

        if (pageNum == null || pageSize == null){
            return null;
        }
        if (pageNum < 0 || pageSize < 0){
            return null;
        }

        int offSet = 0;
        if (pageNum > 1){
            offSet = (pageNum-1)*pageSize;
        }

        List<PurchaserEntity> results = purchaserDao.pageQueryPurchasers(offSet, pageSize);
        return commonBeanUtils.changeToPurchaserVOS(results);
    }

    @Override
    public List<PurchaserVO> getConditionalPurchasers(PurchaserVO purchaserVO,@NotNull Integer pageNum, @NotNull Integer pageSize) {
        if (purchaserVO == null){
            return null;
        }
        List<PurchaserEntity> queryResults = new ArrayList<>();
        //如果员工ID不为空，尝试根据id查询员工信息
        if (purchaserVO.getId() != null){
            PurchaserEntity oneResult = purchaserDao.getPurchaserById(purchaserVO.getId());
            queryResults.add(oneResult);
            return commonBeanUtils.changeToPurchaserVOS(queryResults);
        }
        if (purchaserVO.getName() != null){
            queryResults = purchaserDao.getPurchaserByName(purchaserVO.getName());
            //接着根据其他条件进行过滤
            return dealPurchaserResult(queryResults,purchaserVO.getPhoneNumber(),purchaserVO.getAddress(),pageNum,pageSize);
        }

        queryResults = purchaserDao.fuzzyQuery(purchaserVO.getPhoneNumber(), purchaserVO.getAddress());
        return commonBeanUtils.changeToPurchaserVOS(queryResults);
    }

    @Override
    public boolean addPurchaser(PurchaserVO purchaserVO) {
        if (purchaserVO == null){
            return false;
        }
        //首先判断purchaserVO里面的每个属性都不为空，除了id(因为id是在添加入库时，主键自增)
        boolean fieldIsNull = judgePurchaserVOBaseMsgIsNullExceptID(purchaserVO);
        if (fieldIsNull){
            return false;
        }
        //属性除了id都不为空的话，那么接下来就要检验手机号码和身份证号码的格式了
        if ( !CommonStaticUtils.verifyPhoneNumber(purchaserVO.getPhoneNumber())){
            return false;
        }
        if ( !CommonStaticUtils.verifyIdentityNumber(purchaserVO.getIdentityNumber())){
            return false;
        }

        //如果格式符合规格，那么就要添加入职时间和角色了
        PurchaserEntity purchaserEntity = commonBeanUtils.changeToPurchaserEntity(purchaserVO);
        purchaserEntity.setOnBoardingDatetime(LocalDateTime.now());
        purchaserEntity.setIsDimission(0);
        purchaserEntity.setRoleId(PURCHASER_ROLE_ID);

        int influenceRows = purchaserDao.addPurchaser(purchaserEntity);
        return influenceRows == 1;
    }

    @Override
    public boolean updatePurchaser(PurchaserVO purchaserVO) {
        //判断主键是否存在，因为需要找到修改的员工
        if (purchaserVO == null || purchaserVO.getId() == null){
            return false;
        }

        int isSuccess = purchaserDao.updatePurchaser(purchaserVO);

        return isSuccess == 1;
    }

    @Override
    public boolean deletePurchaser(Long id) {
        if (id == null){
            return false;
        }

        int isSuccess = purchaserDao.delPurchaser(id, LocalDateTime.now());

        return isSuccess == 1;
    }

    public SalesmanVO getSalesmanById(Long id) {
        if (id == null){
            return null;
        }

        SalesmanEntity result = salesmanDao.getSalesmanById(id);
        return commonBeanUtils.changeToSalesmanVO(result);
    }

    @Override
    public List<SalesmanVO> pageQuerySalesman(Integer pageNum, Integer pageSize) {

        if (pageNum == null || pageSize == null){
            return null;
        }
        if (pageNum < 0 || pageSize < 0){
            return null;
        }

        int offSet = 0;
        if (pageNum > 1){
            offSet = (pageNum-1)*pageSize;
        }

        List<SalesmanEntity> results = salesmanDao.pageQuerySalesmans(offSet, pageSize);
        return commonBeanUtils.changeToSalesmanVOS(results);
    }

    @Override
    public List<SalesmanVO> getConditionalSalesman(SalesmanVO salesmanVO, Integer pageNum, Integer pageSize) {
        if (salesmanVO == null){
            return null;
        }
        List<SalesmanEntity> queryResults = new ArrayList<>();

        if (salesmanVO.getId() != null){
            SalesmanEntity oneResult = salesmanDao.getSalesmanById(salesmanVO.getId());
            queryResults.add(oneResult);
            return commonBeanUtils.changeToSalesmanVOS(queryResults);
        }

        if (salesmanVO.getName() != null){
            queryResults = salesmanDao.getSalesmanByName(salesmanVO.getName());
            return dealSalesmanResult(queryResults,salesmanVO.getPhoneNumber(),salesmanVO.getAddress(),pageNum,pageSize);
        }

        queryResults = salesmanDao.fuzzyQuery(salesmanVO.getPhoneNumber(),salesmanVO.getAddress());
        return commonBeanUtils.changeToSalesmanVOS(queryResults);
    }

    @Override
    public boolean addSalesman(SalesmanVO salesmanVO) {
        if (salesmanVO == null){
            return false;
        }
        boolean fieldIsNull = judgeSalesmanVOBaseMsgIsNull(salesmanVO);
        if (fieldIsNull){
            return false;
        }
        if ( !CommonStaticUtils.verifyPhoneNumber(salesmanVO.getPhoneNumber())){
            return false;
        }
        if ( !CommonStaticUtils.verifyIdentityNumber(salesmanVO.getIdentityNumber())){
            return false;
        }

        SalesmanEntity salesmanEntity = commonBeanUtils.changeToSalesmanEntity(salesmanVO);
        salesmanEntity.setOnBoardingDatetime(LocalDateTime.now());
        salesmanEntity.setIsDimission(0);
        salesmanEntity.setRoleId(SALESMAN_ROLE_ID);

        int influencedRows = salesmanDao.addSalesman(salesmanEntity);
        return influencedRows == 1;

    }

    @Override
    public boolean updateSalesman(SalesmanVO salesmanVO) {
        if (salesmanVO == null || salesmanVO.getId() == null){
            return false;
        }

        int isSuccess = salesmanDao.updateSalesman(salesmanVO);
        return isSuccess == 1;
    }

    @Override
    public boolean deleteSalesman(Long id) {

        if (id == null){
            return false;
        }

        int isSuccess = salesmanDao.delSalesman(id, LocalDateTime.now());

        return isSuccess == 1;
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/5/2 15:23
     * @Param:
     * @Return:
     **/
    private boolean judgePurchaserVOBaseMsgIsNullExceptID(PurchaserVO purchaserVO){

        if (purchaserVO == null){
            return true;
        }

        if (StringUtils.isNullOrEmpty(purchaserVO.getName())){
            return true;
        }
        if (StringUtils.isNullOrEmpty(purchaserVO.getPhoneNumber())){
            return true;
        }
        if (StringUtils.isNullOrEmpty(purchaserVO.getIdentityNumber())){
            return true;
        }

        return StringUtils.isNullOrEmpty(purchaserVO.getAddress());

    }




    private boolean judgeSalesmanVOBaseMsgIsNull(SalesmanVO salesmanVO){

        if (salesmanVO == null){
            return true;
        }

        if (StringUtils.isNullOrEmpty(salesmanVO.getName())){
            return true;
        }
        if (StringUtils.isNullOrEmpty(salesmanVO.getPhoneNumber())){
            return true;
        }
        if (StringUtils.isNullOrEmpty(salesmanVO.getIdentityNumber())){
            return true;
        }

        return StringUtils.isNullOrEmpty(salesmanVO.getAddress());
    }

    private List<PurchaserVO> dealPurchaserResult(List<PurchaserEntity> result, String phoneNumber, String address, @NotNull Integer pageNum, @NotNull Integer pageSize){
        if (result == null){
            return null;
        }
        List<PurchaserVO> purchaserVOS = commonBeanUtils.changeToPurchaserVOS(result);
        List<PurchaserVO> filteredResult = new LinkedList<>();
        if (phoneNumber == null && address == null){
            return pageBreakPurchasers(purchaserVOS,pageNum,pageSize);
        }

        for (PurchaserVO item : purchaserVOS){
            if(phoneNumber != null){
                if (item.getPhoneNumber().compareTo(phoneNumber) != 0){
                    continue;
                }
            }
            if (address != null){
                if (!item.getAddress().contains(address)){
                    continue;
                }
            }
            filteredResult.add(item);
        }

        return pageBreakPurchasers(filteredResult,pageNum,pageSize);
    }


    private List<PurchaserVO> pageBreakPurchasers(List<PurchaserVO> filteredList,Integer pageNum, Integer pageSize){
        if (filteredList == null){
            return null;
        }

        int filteredListSize = filteredList.size();
        int needFilterPurchaserSize = (pageNum-1)*pageSize; //减一是因为我们的集合是以0下标未开始的
        if(filteredListSize > needFilterPurchaserSize){
            List<PurchaserVO> returnList = new LinkedList<>();
            int number = filteredListSize - needFilterPurchaserSize;
            if (number > pageSize){
                number = pageSize;
            }
            Iterator<PurchaserVO> iterator = filteredList.iterator();
            int count=0;
            int numberOfCycles = needFilterPurchaserSize + number;
            PurchaserVO next;
            for(int i=0; i<numberOfCycles; i++){

                next = iterator.next();
                if(count >= needFilterPurchaserSize){
                    returnList.add(next);
                }
                count++;
            }
            return returnList;
        }
        return null;
    }

    private List<SalesmanVO> dealSalesmanResult(List<SalesmanEntity> result, String phoneNumber, String address, @NotNull Integer pageNum, @NotNull Integer pageSize){
        if (result == null){
            return null;
        }
        List<SalesmanVO> SalesmanVOS = commonBeanUtils.changeToSalesmanVOS(result);
        List<SalesmanVO> filteredResult = new LinkedList<>();
        if (phoneNumber == null && address == null){
            return pageBreakSalesmans(SalesmanVOS,pageNum,pageSize);
        }

        for (SalesmanVO item : SalesmanVOS){
            if(phoneNumber != null){
                if (item.getPhoneNumber().compareTo(phoneNumber) != 0){
                    continue;
                }
            }
            if (address != null){
                if (!item.getAddress().contains(address)){
                    continue;
                }
            }
            filteredResult.add(item);
        }

        return pageBreakSalesmans(filteredResult,pageNum,pageSize);
    }

    private List<SalesmanVO> pageBreakSalesmans(List<SalesmanVO> filteredList,Integer pageNum, Integer pageSize){
        if (filteredList == null){
            return null;
        }

        int filteredListSize = filteredList.size();
        int needFilterSalesmanSize = (pageNum-1)*pageSize; //减一是因为我们的集合是以0下标未开始的
        if(filteredListSize > needFilterSalesmanSize){
            List<SalesmanVO> returnList = new LinkedList<>();
            int number = filteredListSize - needFilterSalesmanSize;
            if (number > pageSize){
                number = pageSize;
            }
            Iterator<SalesmanVO> iterator = filteredList.iterator();
            int count=0;
            int numberOfCycles = needFilterSalesmanSize + number;
            SalesmanVO next;
            for(int i=0; i<numberOfCycles; i++){

                next = iterator.next();
                if(count >= needFilterSalesmanSize){
                    returnList.add(next);
                }
                count++;
            }
            return returnList;
        }
        return null;
    }
}
