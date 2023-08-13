package com.tunan.inventoryManagementSystem.service.impl;

import com.tunan.inventoryManagementSystem.dao.CommonDao;
import com.tunan.inventoryManagementSystem.dao.CustomerDao;
import com.tunan.inventoryManagementSystem.dao.PigInventoryDao;
import com.tunan.inventoryManagementSystem.dao.SellingRecordDao;
import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.entity.Role;
import com.tunan.inventoryManagementSystem.entity.SellingRecordEntity;
import com.tunan.inventoryManagementSystem.entity.VO.SellingRecordVO;
import com.tunan.inventoryManagementSystem.exception.DatabaseUpdateException;
import com.tunan.inventoryManagementSystem.service.SellingRecordService;
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
public class SellingRecordServiceImpl implements SellingRecordService {

    //降序
    public static final int ORDER_BY_RECORD_TIME_DESC = -1;

    //升序
    public static final int ORDER_BY_RECORD_TIME_ASC = 1;

    private final CommonDao commonDao;

    private final SellingRecordDao sellingRecordDao;

    private final CommonBeanUtils commonBeanUtils;

    private final CustomerDao customerDao;

    private final PigInventoryDao pigInventoryDao;

    public SellingRecordServiceImpl(CommonDao commonDao, SellingRecordDao sellingRecordDao, CommonBeanUtils commonBeanUtils, CustomerDao customerDao, PigInventoryDao pigInventoryDao) {
        this.commonDao = commonDao;
        this.sellingRecordDao = sellingRecordDao;
        this.commonBeanUtils = commonBeanUtils;
        this.customerDao = customerDao;
        this.pigInventoryDao = pigInventoryDao;
    }

    @Override
    public int getCounts() {
        return sellingRecordDao.count();
    }

    /**
     * @Description: 分页查询销售记录
     * @Author: CaiGou
     * @Date: 2023/4/18 21:19
     **/
    @Override
    public List<SellingRecordVO> pageQueryForSellingRecordASC(Integer pageNum, Integer pageSize, LoginUser loginUser) {

        if (pageNum < 0 || pageSize <= 0){
            return null;
        }

        int offset = 0;
        if (pageNum > 1){
            offset = (pageNum - 1)*pageSize;
        }

        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());

        List<SellingRecordEntity> sellingRecordEntityList;
        if(roleId == Role.ADMINISTRATOR_ROLE_ID || roleId == Role.MANAGER_ROLE_ID){
            sellingRecordEntityList =  sellingRecordDao.pageQuery(offset, pageSize,ORDER_BY_RECORD_TIME_ASC);
        }else{
            sellingRecordEntityList =  sellingRecordDao.pageQueryLimitToRole(offset, pageSize, ORDER_BY_RECORD_TIME_ASC,loginUser.getUserVO().getWorkerId());
        }
        //接着将sellingRecordEntity变成sellingRecordVO,并返回
        return commonBeanUtils.changeToSellingRecordVOS(sellingRecordEntityList);
    }

    /**
     * @Description: 默认对查询的订单按照时间从新订单到旧订单排序
     * @Author: CaiGou
     * @Date: 2023/4/19 10:28
     * @Param:
     * @Return:
     **/
    @Override
    public List<SellingRecordVO> pageQueryForSellingRecord(Integer pageNum, Integer pageSize, LoginUser loginUser) {

        if (pageNum < 0 || pageSize <= 0){
            return null;
        }

        int offset = 0;
        if (pageNum > 1){
            offset = (pageNum - 1)*pageSize;
        }

        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());

        List<SellingRecordEntity> sellingRecordEntityList;
        if(roleId == Role.ADMINISTRATOR_ROLE_ID || roleId == Role.MANAGER_ROLE_ID){
            sellingRecordEntityList =  sellingRecordDao.pageQuery(offset, pageSize,ORDER_BY_RECORD_TIME_DESC);
        }else{
            sellingRecordEntityList =  sellingRecordDao.pageQueryLimitToRole(offset, pageSize, ORDER_BY_RECORD_TIME_DESC,loginUser.getUserVO().getWorkerId());
        }
        //接着将sellingRecordEntity变成sellingRecordVO,并返回
        return commonBeanUtils.changeToSellingRecordVOS(sellingRecordEntityList);
    }

    /**
     * @Description: 条件查询用户信息
     * @Author: CaiGou
     * @Date: 2023/4/19 10:12
     * @Param:
     * @Return:
     **/
    @Override
    public List<SellingRecordVO> getConditionalRecords(
            String recordNumber, String enterpriseName,
            String pigFunction, String pigType,
            String startDate, String deadTime,
            Integer pageNum, Integer pageSize,
            LoginUser loginUser
    ) {

        int offset = 0;
        if (pageNum > 1){
            offset = (pageNum-1)*pageSize;
        }

        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());
        Long workerId = loginUser.getUserVO().getWorkerId();

        LocalDateTime startDT = TimeUtils.dateTimeOf(startDate);
        LocalDateTime deadDT = TimeUtils.dateTimeOf(deadTime);

        List<SellingRecordEntity> queryResult;

        //尝试根据订单号查询订单信息，如果订单号为空会返回null
        SellingRecordEntity oneRecord = sellingRecordDao.getSellingRecordByRecordNumber(recordNumber,roleId,workerId);

        if(oneRecord != null){
            //如果查询结果不为空，那么就返回这一条结果
            queryResult = new ArrayList<>();
            queryResult.add(oneRecord);
            return commonBeanUtils.changeToSellingRecordVOS(queryResult);
        }

        if (enterpriseName != null){
            //如果企业名字不为空，那么先根据企业名字查询所有有关该企业的销售订单信息，然后在后端再利用其他条件进行筛选
            queryResult = getSellingRecordByEnterpriseName(enterpriseName,roleId,workerId);
            return dealSellingRecordList(queryResult, startDT, deadDT, pigFunction, pigType, pageNum, pageSize);
        }

        //如果能进行到这里说明订单号和企业名字都为空，尝试根据其他条件进行查询
        if (startDT != null && deadDT != null){
            queryResult = sellingRecordDao.getSellingRecordsByTime(startDT,deadDT,roleId,workerId);
            return dealSellingRecordList(queryResult, pigFunction, pigType, pageNum, pageSize);
        }else {
            queryResult = sellingRecordDao.pageQueryByBaseCondition(commonBeanUtils.getFunctionIdByFunction(pigFunction),
                                                                    commonBeanUtils.getTypeIdByType(pigType),
                                                                    offset,pageSize,roleId,workerId);
            return commonBeanUtils.changeToSellingRecordVOS(queryResult);
        }


    }

    /**
     * @Description: 添加订单
     * @Author: CaiGou
     * @Date: 2023/4/20 21:49
     * @Param:
     * @Return:
     **/
    @Override
    public boolean addSellingRecord(SellingRecordVO sellingRecordVO, LoginUser loginUser) {

        //TODO 如果是管理员添加了订单，那么操作员id那一块(即WorkerID) 应该填什么，指定规则喽，就是添加订单的时候，必须填写当前操作员的工号
        boolean isSuccess = false;

        if (sellingRecordVO == null){
            return false;
        }
        /*TODO 解析token，从数据库查询对应的业务员ID，例如销售员添加订单时，需要填写他的工号，但是我们也需要从数据库中查询
               其登录的用户其真实身份的ID，进行比对，以防销售员在填写自己的工号时错误了，月底拿不到这一单交易的绩效。
         */
        SellingRecordEntity sellingRecordEntity = commonBeanUtils.changeToSellingRecordEntity(sellingRecordVO);
        //将接受的前端信息转换为Entity类，但是这个类里面还有部分信息是空缺的需要补充
        //添加订单号
        sellingRecordEntity.setRecordNumber(CommonStaticUtils.createSellingOrderNumber());
        //添加订单的操作员id,如果是管理员，操作员id那里默认填-1，但是先判断VO类中的id是否为空
        Integer roleId = commonBeanUtils.getRoleIdByName(loginUser.getUserVO().getRole());
        if (roleId == Role.ADMINISTRATOR_ROLE_ID || roleId == Role.MANAGER_ROLE_ID ){

            sellingRecordEntity.setSalesmanId(-1L);
        }else {

            sellingRecordEntity.setSalesmanId(loginUser.getUserVO().getWorkerId());
        }

        //添加创建时间
        LocalDateTime now = LocalDateTime.now();
        sellingRecordEntity.setCreateDatetime(now);
        sellingRecordEntity.setUpdateDatetime(now);
        //默认为订单未被删除，未被标记为退单
        sellingRecordEntity.setIsDelete(0);
        sellingRecordEntity.setIsReturn(0);
        //还有就是销售的期望价格，我们要从数据库中查询市场价格
        //TODO 但是市场价格是随时波动的，等以后学了爬虫了，再去动态获取官网的市场价格然后填充到数据库中，保证数据与市场一致，这里我们的数据是死的
        float marketSellingPrice = commonDao.getMarketSellingPrice(sellingRecordEntity.getPigFunctionId(), sellingRecordEntity.getPigTypeId());
        float expectPrice;
        if ( sellingRecordVO.getPigFunction().equals("肉猪") ){
            expectPrice = sellingRecordEntity.getPounds() * marketSellingPrice;
        }else {
            expectPrice = sellingRecordEntity.getSellingCount() * marketSellingPrice;
        }
        sellingRecordEntity.setExpectedSellingPrice(expectPrice);

        //添加订单，并同时更新库存信息
        try{
            int influencedRows = sellingRecordDao.addSellingRecord(sellingRecordEntity);
            if (influencedRows == 1){
                //此时订单已经添加成功，我们同时还需要修改库存的信息
                if (pigInventoryDao.modifyInventoryCount
                        (sellingRecordEntity.getSellingCount(),sellingRecordEntity.getPounds(),
                         sellingRecordEntity.getPigFunctionId(),sellingRecordEntity.getPigTypeId()) == 1
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
     * @Description: 根据订单ID(数据库主键)删除订单；【说明：id与订单号码不一样】
     * @Author: CaiGou
     * @Date: 2023/4/20 21:48
     * @Param:
     * @Return:
     **/
    @Override
    public boolean delSellingRecord(Long id) {
        //TODO 还没有检查权限
        if (id == null){
            return false;
        }

        return sellingRecordDao.delSellingRecordById(id) == 1;
    }


    /**
     * @Description: 根据订单号码删除订单
     * @Author: CaiGou
     * @Date: 2023/4/20 21:47
     * @Param:
     * @Return:
     **/
    @Override
    public boolean delSellingRecord(String recordNumber) {
        //TODO 还没有检查权限
        if (recordNumber == null){
            return false;
        }
        if (recordNumber.equals("")){
            return false;
        }

        return sellingRecordDao.delSellingRecordByRecordNumber(recordNumber) == 1;
    }

    /**
     * @Description: 修改订单信息
     * @Author: CaiGou
     * @Date: 2023/4/20 22:49
     * @Param:
     * @Return:
     **/
    @Override
    @Transactional
    public boolean updateSellingRecord(SellingRecordVO sellingRecordVO, LoginUser loginUser) {
        //TODO 不确定是否要给员工修改订单信息的权限，现在任然没有给其修改的权限
        boolean isNeedToChangeTypeOrFunction = false;
        if(sellingRecordVO == null){
            return false;
        }
        if (sellingRecordVO.getId() == null && sellingRecordVO.getRecordNumber() == null){
            return false;
        }
        /* TODO 不允许修改订单号的采购员或者销售员的ID，因为在添加时订单时，订单的业务员ID应该由token解析出当前用户
            的身份，并从数据库中查询其对应的ID
        */
        SellingRecordEntity newRecord = commonBeanUtils.changeToSellingRecordEntity(sellingRecordVO);
        if (newRecord.getPigFunctionId() == null || newRecord.getPigTypeId() == null){
            return false;
        }
        SellingRecordEntity oldRecord = sellingRecordDao.getSellingRecordById(newRecord.getId());
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

            if (newRecord.getSellingCount() != null){
                changeOfCount = newRecord.getSellingCount() - oldRecord.getSellingCount();
            }
            if (newRecord.getPounds() != null){
                changeOfPounds = newRecord.getPounds() - oldRecord.getPounds();
            }
            //重新计算expectedPrice
            float marketSellingPrice = commonDao.getMarketSellingPrice(oldRecord.getPigFunctionId(), oldRecord.getPigTypeId());
            if ( sellingRecordVO.getPigFunction().equals("肉猪") ){
                expectedSellingPrice = newRecord.getPounds() * marketSellingPrice;
            }else {
                expectedSellingPrice = newRecord.getSellingCount() * marketSellingPrice;
            }
            newRecord.setExpectedSellingPrice(expectedSellingPrice);
        }else {
            isNeedToChangeTypeOrFunction = true;
        }


        //数据修正与修改
        try {
            if (isNeedToChangeTypeOrFunction) {
                //如果修改了猪的品种或者功能，那么就要修改pigInventory
                int a = pigInventoryDao.modifyInventoryCount(-oldRecord.getSellingCount(), -oldRecord.getPounds(), oldRecord.getPigFunctionId(), oldRecord.getPigTypeId());
                int b = sellingRecordDao.updateSellingRecord(newRecord);
                int c = pigInventoryDao.modifyInventoryCount(newRecord.getSellingCount(), newRecord.getPounds(), newRecord.getPigFunctionId(), newRecord.getPigTypeId());
                if (a != 1 || b != 1 || c != 1){
                    throw new DatabaseUpdateException();
                }
            }else {
                //对销售记录进行更改
                int a = sellingRecordDao.updateSellingRecord(newRecord);
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
     * @Description: 后端进行数据筛选
     * @Author: CaiGou
     * @Date: 2023/4/20 20:44
     * @Param:
     * @Return:
     **/
    private List<SellingRecordVO> dealSellingRecordList(List<SellingRecordEntity> recordEntityList,
                                                        LocalDateTime startDT,              LocalDateTime deadDT,
                                                        String pigFunction,                 String pigType,
                                                        Integer pageNum,                    Integer pageSize
    ){
        //需要注意的是mybatis底层默认返回的是ArrayList。如果要将ArrayList变成LinkList去对查询的数据筛选时进行删除操作，需要创建一个新的LinkList
        //但是这里我们是通用方法，不管传入的List实例是linklist还是arraylist都创建一个新的linklist去接受过滤后的结果
        if (recordEntityList == null){
            return null;
        }
        //不为空，先将Entity变成VO
        List<SellingRecordVO> recordVOS = commonBeanUtils.changeToSellingRecordVOS(recordEntityList);

        List<SellingRecordVO> filteredList = new LinkedList<>();


        for (SellingRecordVO item : recordVOS) {
            if (item.getCreateDatetime().compareTo(startDT) < 0) {
                continue;
            }
            if (item.getCreateDatetime().compareTo(deadDT) > 0) {
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

        return pageBreakSellingRecordS(filteredList,pageNum,pageSize);
    }

    private List<SellingRecordVO> dealSellingRecordList(List<SellingRecordEntity> recordEntityList,
                                                        String pigFunction,                 String pigType,
                                                        Integer pageNum,                    Integer pageSize
    ){
        if (recordEntityList == null){
            return null;
        }
        //不为空，先将Entity变成VO
        List<SellingRecordVO> recordVOS = commonBeanUtils.changeToSellingRecordVOS(recordEntityList);

        List<SellingRecordVO> filteredList = new LinkedList<>();

        if (pigFunction == null && pigType == null){
            //如果筛选条件都为空的话，直接对传入的List进行分页
            return pageBreakSellingRecordS(filteredList,pageNum,pageSize);
        }

        for (SellingRecordVO item : recordVOS) {
            if (!item.getPigFunction().equals(pigFunction)) {
                continue;
            }
            if (!item.getPigType().equals(pigType)) {
                continue;
            }

            filteredList.add(item);
        }


        return pageBreakSellingRecordS(filteredList,pageNum,pageSize);
    }

    private List<SellingRecordVO> pageBreakSellingRecordS(List<SellingRecordVO> filteredList, Integer pageNum, Integer pageSize){
        if (filteredList == null){
            return null;
        }
        if (pageNum == null || pageSize == null){
            return null;
        }

        int filteredListSize = filteredList.size();
        int filteredRecordSize = (pageNum-1)*pageSize; //减一是因为我们的集合是以0下标未开始的
        if(filteredListSize > filteredRecordSize){
            List<SellingRecordVO> returnList = new LinkedList<>();
            int number = filteredListSize - filteredRecordSize;
            if (number > pageSize){
                number = pageSize;
            }
            Iterator<SellingRecordVO> iterator = filteredList.iterator();
            int count=0;
            int numberOfCycles = filteredRecordSize + number;
            SellingRecordVO next;
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
    public List<SellingRecordEntity> getSellingRecordByEnterpriseName(String enterpriseName, Integer roleId, Long workerId){
        if (enterpriseName == null){
            return null;
        }
        Long id = customerDao.getIdByEnterpriseName(enterpriseName);
        if (id == null){
            return null;
        }

        return sellingRecordDao.getSellingRecordByCustomerId(id,roleId,workerId);
    }
}
