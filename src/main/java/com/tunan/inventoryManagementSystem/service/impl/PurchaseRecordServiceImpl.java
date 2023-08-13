package com.tunan.inventoryManagementSystem.service.impl;

import com.tunan.inventoryManagementSystem.dao.CommonDao;
import com.tunan.inventoryManagementSystem.dao.PigInventoryDao;
import com.tunan.inventoryManagementSystem.dao.ProviderDao;
import com.tunan.inventoryManagementSystem.dao.PurchaseRecordDao;
import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.entity.PurchaseRecordEntity;
import com.tunan.inventoryManagementSystem.entity.Role;
import com.tunan.inventoryManagementSystem.entity.VO.PurchaseRecordVO;
import com.tunan.inventoryManagementSystem.exception.DatabaseUpdateException;
import com.tunan.inventoryManagementSystem.service.PurchaseRecordService;
import com.tunan.inventoryManagementSystem.utils.CommonBeanUtils;
import com.tunan.inventoryManagementSystem.utils.CommonStaticUtils;
import com.tunan.inventoryManagementSystem.utils.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class PurchaseRecordServiceImpl implements PurchaseRecordService {

    //降序
    public static final int ORDER_BY_RECORD_TIME_DESC = -1;

    //升序
    public static final int ORDER_BY_RECORD_TIME_ASC = 1;

    private final CommonDao commonDao;

    private final PurchaseRecordDao purchaseRecordDao;

    private final CommonBeanUtils commonBeanUtils;

    private final ProviderDao providerDao;

    private final PigInventoryDao pigInventoryDao;

    public PurchaseRecordServiceImpl(CommonDao commonDao, PurchaseRecordDao purchaseRecordDao, CommonBeanUtils commonBeanUtils, ProviderDao providerDao, PigInventoryDao pigInventoryDao) {
        this.commonDao = commonDao;
        this.purchaseRecordDao = purchaseRecordDao;
        this.commonBeanUtils = commonBeanUtils;
        this.providerDao = providerDao;
        this.pigInventoryDao = pigInventoryDao;
    }

    @Override
    public int getCounts() {
        return purchaseRecordDao.count();
    }

    /**
     * @Description: 分页查询，按照时间升序
     * @Author: CaiGou
     * @Date: 2023/4/21 14:55
     * @Param:
     * @Return:
     **/
    @Override
    public List<PurchaseRecordVO> pageQueryForPurchaseRecordASC(Integer pageNum, Integer pageSize, LoginUser loginUser) {

        if (pageNum == null || pageSize == null){
            return null;
        }

        int offset = 0;
        if (pageNum > 1){
            offset = (pageNum -1)*pageSize;
        }

        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());

        List<PurchaseRecordEntity> purchaseRecordEntityList;
        if (roleId == Role.ADMINISTRATOR_ROLE_ID){
            //如果是管理员查询的话，那么我们就直接将前pageSize条数据给上去了。
            purchaseRecordEntityList = purchaseRecordDao.pageQuery(offset, pageSize, ORDER_BY_RECORD_TIME_ASC);

        }else {
            purchaseRecordEntityList = purchaseRecordDao.pageQueryLimitToRole(offset,pageSize,ORDER_BY_RECORD_TIME_ASC,loginUser.getUserVO().getWorkerId());
        }

        //将entity变成VO
        return commonBeanUtils.changeToPurchaseRecordVOS(purchaseRecordEntityList);

    }

    /**
     * @Description: 分页查询，默认排序方式，按照时间降序
     * @Author: CaiGou
     * @Date: 2023/4/21 14:56
     * @Param:
     * @Return:
     **/
    @Override
    public List<PurchaseRecordVO> pageQueryForPurchaseRecord(Integer pageNum, Integer pageSize, LoginUser loginUser) {

        if (pageNum == null || pageSize == null){
            return null;
        }

        int offset = 0;
        if (pageNum > 1){
            offset = (pageNum -1)*pageSize;
        }

        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());

        List<PurchaseRecordEntity> purchaseRecordEntityList;
        if (roleId == Role.ADMINISTRATOR_ROLE_ID){
            //如果是管理员查询的话，那么我们就直接将前pageSize条数据给上去了。
            purchaseRecordEntityList = purchaseRecordDao.pageQuery(offset, pageSize, ORDER_BY_RECORD_TIME_DESC);

        }else {
            purchaseRecordEntityList = purchaseRecordDao.pageQueryLimitToRole(offset,pageSize,ORDER_BY_RECORD_TIME_DESC,loginUser.getUserVO().getWorkerId());
        }

        //将entity变成VO
        return commonBeanUtils.changeToPurchaseRecordVOS(purchaseRecordEntityList);
    }


    /**
     * @Description: 条件查询
     * @Author: CaiGou
     * @Date: 2023/4/21 15:46
     * @Param:
     * @Return:
     **/
    @Override
    public List<PurchaseRecordVO> getConditionalRecords(String recordNumber,    String enterpriseName,
                                                        String pigFunction,     String pigType,
                                                        String startDate,       String deadTime,
                                                        Integer pageNum,        Integer pageSize, LoginUser loginUser
    ) {

        int offset = 0;
        if (pageNum > 1){
            offset = (pageNum-1)*pageSize;
        }

        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());
        Long workerId = loginUser.getUserVO().getWorkerId();

        LocalDateTime startDT = TimeUtils.dateTimeOf(startDate);
        LocalDateTime deadDT = TimeUtils.dateTimeOf(deadTime);

        List<PurchaseRecordEntity> queryResult;

        //尝试根据订单号查询订单信息，如果订单号为空会返回null
        PurchaseRecordEntity oneRecord = purchaseRecordDao.getPurchaseRecordByRecordNumber(recordNumber,roleId,workerId);

        if(oneRecord != null){
            //如果查询结果不为空，那么就返回这一条结果
            queryResult = new ArrayList<>();
            queryResult.add(oneRecord);
            return commonBeanUtils.changeToPurchaseRecordVOS(queryResult);
        }

        if (enterpriseName != null){
            //如果企业名字不为空，那么先根据企业名字查询所有有关该企业的销售订单信息，然后在后端再利用其他条件进行筛选
            queryResult = getPurchaseRecordByEnterpriseName(enterpriseName,roleId,workerId);
            return dealPurchaseRecordList(queryResult, startDT, deadDT, pigFunction, pigType, pageNum, pageSize);
        }

        //如果能进行到这里说明订单号和企业名字都为空，尝试根据其他条件进行查询
        if (startDT != null && deadDT != null){
            queryResult = purchaseRecordDao.getPurchaseRecordsByTime(startDT,deadDT,roleId,workerId);
            return dealPurchaseRecordList(queryResult,pigFunction,pigType,pageNum,pageSize);
        }else {
            queryResult = purchaseRecordDao.pageQueryByBaseConditionLimitToRole(commonBeanUtils.getFunctionIdByFunction(pigFunction),
                                                                     commonBeanUtils.getTypeIdByType(pigType),
                                                                     offset,pageSize, roleId, workerId);
            return commonBeanUtils.changeToPurchaseRecordVOS(queryResult);
        }


    }


    /**
     * @Description: 添加记录
     * @Author: CaiGou
     * @Date: 2023/4/21 15:46
     * @Param:
     * @Return:
     **/
    @Override
    @Transactional
    public boolean addPurchaseRecord(PurchaseRecordVO purchaseRecordVO, LoginUser loginUser) {

        boolean isSuccess = false;

        if (purchaseRecordVO == null){
            return false;
        }

        PurchaseRecordEntity purchaseRecordEntity = commonBeanUtils.changeToPurchaseRecordEntity(purchaseRecordVO);
        //将接受的前端信息转换为Entity类，但是这个类里面还有部分信息是空缺的需要补充
        //添加订单号
        purchaseRecordEntity.setRecordNumber(CommonStaticUtils.createPurchaseOrderNumber());
        //添加订单的操作员id,如果是管理员，操作员id那里默认填-1，但是先判断VO类中的id是否为空
        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());
        if (roleId == Role.ADMINISTRATOR_ROLE_ID || roleId == Role.MANAGER_ROLE_ID ){

            purchaseRecordEntity.setPurchaserId(-1L);
        }else {

            purchaseRecordEntity.setPurchaserId(loginUser.getUserVO().getWorkerId());
        }

        //添加创建时间
        LocalDateTime now = LocalDateTime.now();
        purchaseRecordEntity.setCreateDatetime(now);
        purchaseRecordEntity.setUpdateDatetime(now);
        //默认为订单未被删除，未被标记为退单
        purchaseRecordEntity.setIsDelete(0);
        purchaseRecordEntity.setIsReturn(0);
        //还有就是销售的期望价格，我们要从数据库中查询市场价格
        //TODO 但是市场价格是随时波动的，等以后学了爬虫了，再去动态获取官网的市场价格然后填充到数据库中，保证数据与市场一致，这里我们的数据是死的
        float marketSellingPrice = commonDao.getMarketPurchasePrice(purchaseRecordEntity.getPigFunctionId(), purchaseRecordEntity.getPigTypeId());
        float expectPrice;
        if ( purchaseRecordVO.getPigFunction().equals("肉猪") ){
            expectPrice = purchaseRecordEntity.getPounds()*marketSellingPrice;
        }else {
            expectPrice = purchaseRecordEntity.getPurchaseCount()*marketSellingPrice;
        }
        purchaseRecordEntity.setExpectedPurchasePrice(expectPrice);


        try {
            int influencedRows = purchaseRecordDao.addPurchaseRecord(purchaseRecordEntity);
            if (influencedRows == 1){
                // 说明订单添加成功了,此时我们还需要修改库存信息
                if (pigInventoryDao.modifyInventoryCount
                                (purchaseRecordEntity.getPurchaseCount(),purchaseRecordEntity.getPounds(),
                                 purchaseRecordEntity.getPigFunctionId(),purchaseRecordEntity.getPigTypeId()) == 1
                ){

                    isSuccess = true;

                }else {
                    throw new DatabaseUpdateException();
                }
            }

        }catch (DatabaseUpdateException e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return isSuccess;
    }

    /**
     * @Description:
     * @Author: CaiGou 删除记录
     * @Date: 2023/4/21 15:46
     **/
    @Override
    public boolean delPurchaseRecord(Long id) {
        //TODO 还没有检查权限
        if (id == null){
            return false;
        }

        return purchaseRecordDao.delPurchaseRecordByID(id) == 1;
    }

    /**
     * @Description: 删除记录
     * @Author: CaiGou
     * @Date: 2023/4/21 15:46
     **/
    @Override
    public boolean delPurchaseRecord(String recordNumber) {
        //TODO 还没有检查权限
        if (recordNumber == null){
            return false;
        }
        if (recordNumber.equals("")){
            return false;
        }

        return purchaseRecordDao.delPurchaseRecordByRecordNumber(recordNumber) == 1;
    }

    /**
     * @Description: 更新信息
     * @Author: CaiGou
     * @Date: 2023/4/21 15:47
     **/
    @Override
    @Transactional
    public boolean updatePurchaseRecord(PurchaseRecordVO purchaseRecordVO, LoginUser loginUser) {
        //TODO 不确定是否要给员工修改订单信息的权限，现在任然没有给其修改的权限
        boolean isNeedToChangeTypeOrFunction = false;
        if(purchaseRecordVO == null){
            return false;
        }
        if (purchaseRecordVO.getId() == null && purchaseRecordVO.getRecordNumber() == null){
            return false;
        }
        /* TODO 不允许修改订单号的采购员或者销售员的ID，因为在添加时订单时，订单的业务员ID应该由token解析出当前用户
            的身份，并从数据库中查询其对应的ID
        */
        PurchaseRecordEntity newRecord = commonBeanUtils.changeToPurchaseRecordEntity(purchaseRecordVO);
        if (newRecord.getPigFunctionId() == null || newRecord.getPigTypeId() == null){
            return false;
        }
        PurchaseRecordEntity oldRecord = purchaseRecordDao.getPurchaseRecordById(newRecord.getId());

        //将当前的时间设置到updateDatetime上
        newRecord.setUpdateDatetime(LocalDateTime.now());

        float expectedSellingPrice;
        Integer changeOfCount = null;
        Float changeOfPounds = null;
        //如果改变了猪的种类或者功能那么就要将之前添加的pigInventory进行减量，再对新的进行增量
        if (
                newRecord.getPigFunctionId() == null || newRecord.getPigTypeId() != null ||
                        (newRecord.getPigFunctionId().intValue() == oldRecord.getPigFunctionId().intValue() && newRecord.getPigTypeId().intValue() == oldRecord.getPigTypeId().intValue() )
        ){
            //没变，计算修改的值。

            if (newRecord.getPurchaseCount() != null){
                changeOfCount = newRecord.getPurchaseCount() - oldRecord.getPurchaseCount();
            }
            if (newRecord.getPounds() != null){
                changeOfPounds = newRecord.getPounds() - oldRecord.getPounds();
            }
            //重新计算expectedPrice
            float marketSellingPrice = commonDao.getMarketSellingPrice(oldRecord.getPigFunctionId(), oldRecord.getPigTypeId());
            if ( purchaseRecordVO.getPigFunction().equals("肉猪") ){
                expectedSellingPrice = newRecord.getPounds() * marketSellingPrice;
            }else {
                expectedSellingPrice = newRecord.getPurchaseCount() * marketSellingPrice;
            }
            newRecord.setExpectedPurchasePrice(expectedSellingPrice);
        }else {
            isNeedToChangeTypeOrFunction = true;
        }


        //数据修正与修改
        try {
            if (isNeedToChangeTypeOrFunction) {
                //如果修改了猪的品种或者功能，那么就要修改pigInventory
                int a = pigInventoryDao.modifyInventoryCount(-oldRecord.getPurchaseCount(), -oldRecord.getPounds(), oldRecord.getPigFunctionId(), oldRecord.getPigTypeId());
                int b = purchaseRecordDao.updatePurchaseRecord(newRecord);
                int c = pigInventoryDao.modifyInventoryCount(newRecord.getPurchaseCount(), newRecord.getPounds(), newRecord.getPigFunctionId(), newRecord.getPigTypeId());
                if (a != 1 || b != 1 || c != 1){
                    throw new DatabaseUpdateException();
                }
            }else {
                //对销售记录进行更改
                int a = purchaseRecordDao.updatePurchaseRecord(newRecord);
                //对pigInventory进行修改
                int b = pigInventoryDao.modifyInventoryCount(changeOfCount, changeOfPounds, oldRecord.getPigFunctionId(), oldRecord.getPigTypeId());
                if (a != 1 || b != 1){
                    throw new DatabaseUpdateException();
                }
            }
            return true;
        }catch (DatabaseUpdateException e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

    }

    /**
     * @Description: 过滤数据
     * @Author: CaiGou
     * @Date: 2023/4/21 15:47
     * @Param:
     * @Return:
     **/
    private List<PurchaseRecordVO> dealPurchaseRecordList(List<PurchaseRecordEntity> recordEntityList,
                                                          LocalDateTime startDT, LocalDateTime deadDT,
                                                          String pigFunction, String pigType,
                                                          Integer pageNum, Integer pageSize
    ){
        //需要注意的是mybatis底层默认返回的是ArrayList。如果要将ArrayList变成LinkList去对查询的数据筛选时进行删除操作，需要创建一个新的LinkList
        //但是这里我们是通用方法，不管传入的List实例是linklist还是arraylist都创建一个新的linklist去接受过滤后的结果
        if (recordEntityList == null){
            return null;
        }

        List<PurchaseRecordVO> recordVOS = commonBeanUtils.changeToPurchaseRecordVOS(recordEntityList);

        List<PurchaseRecordVO> filteredList = new LinkedList<>();

        if (startDT == null && deadDT == null && pigFunction == null && pigType == null){
            return pageBreakPurchaseRecordS(recordVOS,pageNum,pageSize);
        }

        for (PurchaseRecordVO item : recordVOS) {
            if (startDT != null && item.getCreateDatetime().compareTo(startDT) < 0 ) {
                continue;
            }
            if(deadDT != null && item.getCreateDatetime().compareTo(deadDT) > 0) {
                continue;
            }
            if (!item.getPigFunction().equals(pigFunction)) {
                continue;
            }
            if (!item.getPigType().equals(pigType)) {
                continue;
            }

            filteredList.add(item);
        }

        return pageBreakPurchaseRecordS(filteredList,pageNum,pageSize);
    }


    /**
     * @Description: 过滤数据
     * @Author: CaiGou
     * @Date: 2023/5/8 13:53
     * @Param:
     * @Return:
     **/
    private List<PurchaseRecordVO> dealPurchaseRecordList(List<PurchaseRecordEntity> recordEntityList,
                                                          String pigFunction,        String pigType,
                                                          Integer pageNum,           Integer pageSize
    ){
        //需要注意的是mybatis底层默认返回的是ArrayList。如果要将ArrayList变成LinkList去对查询的数据筛选时进行删除操作，需要创建一个新的LinkList
        //但是这里我们是通用方法，不管传入的List实例是linklist还是arraylist都创建一个新的linklist去接受过滤后的结果
        if (recordEntityList == null){
            return null;
        }

        List<PurchaseRecordVO> recordVOS = commonBeanUtils.changeToPurchaseRecordVOS(recordEntityList);

        List<PurchaseRecordVO> filteredList = new LinkedList<>();

        if (pigFunction == null && pigType == null){
            //如果筛选条件都为空的话，直接对传入的List进行分页
            return pageBreakPurchaseRecordS(recordVOS,pageNum,pageSize);
        }
        for (PurchaseRecordVO item : recordVOS) {
            if (!item.getPigFunction().equals(pigFunction)) {
                continue;
            }
            if (!item.getPigType().equals(pigType)) {
                continue;
            }

            filteredList.add(item);
        }

        return pageBreakPurchaseRecordS(filteredList,pageNum,pageSize);
    }

    /**
     * @Description: 后端分页方法
     * @Author: CaiGou
     * @Date: 2023/5/8 13:45
     **/
    private List<PurchaseRecordVO> pageBreakPurchaseRecordS(List<PurchaseRecordVO> filteredList, Integer pageNum, Integer pageSize){
        int filteredListSize = filteredList.size();
        int filteredRecordSize = (pageNum-1)*pageSize; //减一是因为我们的集合是以0下标未开始的
        if(filteredListSize > filteredRecordSize){
            List<PurchaseRecordVO> returnList = new LinkedList<>();
            int number = filteredListSize - filteredRecordSize;
            if (number > pageSize){
                number = pageSize;
            }
            Iterator<PurchaseRecordVO> iterator = filteredList.iterator();
            int count=0;
            int numberOfCycles = filteredRecordSize + number;
            PurchaseRecordVO next;
            for(int i=0; i<numberOfCycles; i++){

                next = iterator.next();
                if(count >= filteredRecordSize){
                    returnList.add(next);
                }
                count++;
            }
            return returnList;
        }
        return null;
    }

    public List<PurchaseRecordEntity> getPurchaseRecordByEnterpriseName(String enterpriseName, Integer roleId, Long purchaserId){

        if (enterpriseName == null){
            return null;
        }

        Long id = providerDao.getIdByEnterpriseName(enterpriseName);
        if (id == null){
            return null;
        }

        return purchaseRecordDao.getPurchaseRecordByProviderId(id,roleId,purchaserId);

    }
}
