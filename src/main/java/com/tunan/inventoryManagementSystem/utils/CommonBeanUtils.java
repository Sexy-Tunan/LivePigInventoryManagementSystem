package com.tunan.inventoryManagementSystem.utils;

import com.tunan.inventoryManagementSystem.dao.*;
import com.tunan.inventoryManagementSystem.entity.*;
import com.tunan.inventoryManagementSystem.entity.VO.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CommonBeanUtils {

    private final CommonDao commonDao;

    private final RedisUtils redisUtils;

    private final CustomerDao customerDao;

    private final ProviderDao providerDao;

    private final SalesmanDao salesmanDao;

    private final PurchaserDao purchaserDao;

    private final SellingRecordDao sellingRecordDao;

    private final PurchaseRecordDao purchaseRecordDao;


    private static final Integer DEFAULT_LIVE_TIME_MINUTE = 30;

    private static boolean PIG_TYPE_IS_NEED_QUERY = true;
    private static boolean PIG_FUNCTION_IS_NEED_QUERY = true;
    private static boolean ROLE_IS_NEED_QUERY = true;
    private static boolean PROVIDER_IS_NEED_QUERY = true;
    private static boolean CUSTOMER_IS_NEED_QUERY = true;
    private static boolean SALESMAN_IS_NEED_QUERY = true;
    private static boolean PURCHASER_IS_NEED_QUERY = true;


    private static final ScheduledExecutorService TIMER_TASKS = Executors.newScheduledThreadPool(6);
    //构造器注入bean对象


    public CommonBeanUtils(CommonDao commonDao, RedisUtils redisUtils, CustomerDao customerDao, ProviderDao providerDao, SalesmanDao salesmanDao, PurchaserDao purchaserDao, SellingRecordDao sellingRecordDao, PurchaseRecordDao purchaseRecordDao) {
        this.commonDao = commonDao;
        this.redisUtils = redisUtils;
        this.customerDao = customerDao;
        this.providerDao = providerDao;
        this.salesmanDao = salesmanDao;
        this.purchaserDao = purchaserDao;
        this.sellingRecordDao = sellingRecordDao;
        this.purchaseRecordDao = purchaseRecordDao;
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/5/3 21:35
     **/
    private void saveAllRolesToRedis(){
        List<Role> allRole = commonDao.getAllRole();
        for (Role item : allRole){
            redisUtils.saveObject("role:"+item.getId(),item.getName());
            redisUtils.saveObject("roleId:"+item.getName(),item.getId());
        }

        ROLE_IS_NEED_QUERY = false;

        serRoleQueryTrueDefaultTimeLater();
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/5/3 21:38
     **/
    public String getRoleNameById(Integer id){
        if (id == null){
            return null;
        }
        if (ROLE_IS_NEED_QUERY){
            saveAllRolesToRedis();
        }
        return redisUtils.getObject("role:"+id,String.class);
    }

    public Integer getRoleIdByName(String name){
        if (name == null){
            return null;
        }
        if (ROLE_IS_NEED_QUERY){
            saveAllRolesToRedis();
        }

        return redisUtils.getObject("roleId:"+name,Integer.class);
    }

    /**
     * @Description: 从数据库中查询将所有的PigType保存到redis对象中
     * @Author: CaiGou
     * @Date: 2023/4/24 20:37
     **/
    private void saveAllPigTypeToRedis(){

        List<PigType> allPigType = commonDao.getAllPigType();
        for (PigType item: allPigType){
            redisUtils.saveObject("type:" + item.getId(),item.getType());
            redisUtils.saveObject("typeId:"+item.getType(),item.getId().toString());
        }

        PIG_TYPE_IS_NEED_QUERY = false;

        setTypeQueryTrueDefaultTimeLater();

    }

    /**
     * @Description: 根据typeId 查找pigType
     * @Author: CaiGou
     * @Date: 2023/4/24 20:37
     * @Param:
     * @Return:
     **/
    public String getTypeById(Integer id){

        if (id == null){
            return null;
        }
        if (PIG_TYPE_IS_NEED_QUERY){
            //判断是否需要重新查询数据并保存到redis中
            saveAllPigTypeToRedis();

        }
        //然后从redis中查询并返回
        return redisUtils.getObject("type:" + id, String.class);

    }

    /**
     * @Description: 根据type 查找typeId
     * @Author: CaiGou
     * @Date: 2023/4/24 20:38
     * @Param:
     * @Return:
     **/
    public Integer getTypeIdByType(String type){
        if (type == null){
            return null;
        }
        if (PIG_TYPE_IS_NEED_QUERY){
            saveAllPigTypeToRedis();
        }

        //然后再重新从redis中查询
        return redisUtils.getObject("typeId:" + type, Integer.class);
    }

    /*------------------------------------------------------------------*/
    /*------------------------------------------------------------------*/

    /**
     * @Description: 从数据库中查询保存所有的pigFunction到redis中，并设定存活时间为30分钟
     * @Author: CaiGou
     * @Date: 2023/4/24 20:38
     * @Param:
     * @Return:
     **/
    private void saveAllPigFunctionToRedis(){

        List<PigFunction> allPigFunction = commonDao.getAllPigFunction();
        for (PigFunction item : allPigFunction){
            redisUtils.saveObject("function:"+item.getId(), item.getFunction());
            redisUtils.saveObject("functionId:" + item.getFunction(),item.getId().toString());
        }
        PIG_FUNCTION_IS_NEED_QUERY = false;

        setFunctionQueryTrueDefaultTimeLater();
    }

    /**
     * @Description: 根据functionId 查找Function
     * @Author: CaiGou
     * @Date: 2023/4/24 20:39
     * @Param:
     * @Return:
     **/
    public String getFunctionById(Integer id){
        if (id == null){
            return null;
        }

        if (PIG_FUNCTION_IS_NEED_QUERY){
            saveAllPigFunctionToRedis();
        }

        return redisUtils.getObject("function:" + id, String.class);
    }


    /**
     * @Description: 根据Function查找functionId
     * @Author: CaiGou
     * @Date: 2023/4/24 20:39
     * @Param:
     * @Return:
     **/
    public Integer getFunctionIdByFunction(String function){
        if (function == null){
            return null;
        }
        if (PIG_FUNCTION_IS_NEED_QUERY){

            saveAllPigFunctionToRedis();
        }

        return redisUtils.getObject("functionId:" + function, Integer.class);
    }

    /*--------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------*/

    private void saveAllSalesmanToRedis(){
        List<SalesmanEntity> allSalesman = salesmanDao.getAllSalesman();
        for (SalesmanEntity item : allSalesman){
            redisUtils.saveObject("salesman:" + item.getId(), item ,DEFAULT_LIVE_TIME_MINUTE);
        }

        SALESMAN_IS_NEED_QUERY = false;

        //TODO
    }

    public String getSalesmanNameById(Long id){
        if (id == null){
            return null;
        }
        if (SALESMAN_IS_NEED_QUERY){
            saveAllSalesmanToRedis();
        }

        SalesmanEntity salesmanEntity = redisUtils.getObject("salesman:" + id, SalesmanEntity.class);
        if (salesmanEntity != null){
            return salesmanEntity.getName();
        }else {
            return null;
        }
    }

    public SalesmanEntity getSalesmanById(Long id){
        if (id == null){
            return null;
        }
        if (!SALESMAN_IS_NEED_QUERY){
            return redisUtils.getObject("salesman:" + id, SalesmanEntity.class);
        }else {
            return salesmanDao.getSalesmanById(id);
        }

    }



    private void saveAllPurchaserToRedis(){
        List<PurchaserEntity> allPurchaser = purchaserDao.getAllPurchaser();
        for (PurchaserEntity item : allPurchaser){
            redisUtils.saveObject("purchaser:"+item.getId(),item,DEFAULT_LIVE_TIME_MINUTE);
        }
        PURCHASER_IS_NEED_QUERY = false;
    }

    public String getPurchaserNameById(Long id){
        if (id == null){
            return null;
        }
        if (PURCHASER_IS_NEED_QUERY){
            saveAllPurchaserToRedis();
        }
        PurchaserEntity purchaserEntity = redisUtils.getObject("purchaser:" + id, PurchaserEntity.class);
        if (purchaserEntity != null){
            return purchaserEntity.getName();
        }else {
            return null;
        }
    }

    public PurchaserEntity getPurchaserById(Long id){
        if (id == null){
            return null;
        }
        if (!PURCHASER_IS_NEED_QUERY){
            return redisUtils.getObject("purchaser:" + id, PurchaserEntity.class);
        }else {
            return purchaserDao.getPurchaserById(id);
        }
    }


    /**
     * @Description: 从数据库中查询，保存所有经销商的名字和id到redis中
     * @Author: CaiGou
     * @Date: 2023/4/24 20:40
     * @Param:
     * @Return:
     **/
    private void saveAllCustomerNameAndIDToRedis(){
        List<CustomerVO> allCustomer = customerDao.getAllCustomer();
        for (CustomerVO item : allCustomer){
            redisUtils.saveObject("customer:" + item.getId(), item.getEnterpriseName(),DEFAULT_LIVE_TIME_MINUTE);
            redisUtils.saveObject("customerId:" + item.getEnterpriseName(),item.getId().toString(),DEFAULT_LIVE_TIME_MINUTE);
        }

        CUSTOMER_IS_NEED_QUERY = false;

        setCustomerQueryTrueDefaultTimeLater();
    }

    public String getCustomerNameById(Long id){
        if (id == null){
            return null;
        }
        if (CUSTOMER_IS_NEED_QUERY){
            saveAllCustomerNameAndIDToRedis();
        }

        return redisUtils.getObject("customer:" + id, String.class);
    }

    public Long getCustomerIdByName(String enterpriseName){
        if (enterpriseName == null){
            return null;
        }
        if (CUSTOMER_IS_NEED_QUERY){
            saveAllCustomerNameAndIDToRedis();
        }

        return redisUtils.getObject("customerId:" + enterpriseName, Long.class);
    }


    /**
     * @Description: 从数据库中查询,将所有的providerId和name存到redis中
     * @Author: CaiGou
     * @Date: 2023/4/24 21:47
     * @Param:
     * @Return:
     **/
    public void saveAllProviderNameAndID(){

        List<ProviderVO> allProvider = providerDao.getAllProvider();
        for (ProviderVO item : allProvider){
            redisUtils.saveObject("provider:"+item.getId(), item.getEnterpriseName(),DEFAULT_LIVE_TIME_MINUTE);
            redisUtils.saveObject("providerId:" + item.getEnterpriseName(), item.getId().toString(),DEFAULT_LIVE_TIME_MINUTE);
        }
        PROVIDER_IS_NEED_QUERY = false;

        setProviderQueryTrueDefaultTimeLater();
    }

    public String getProviderNameById(Long id){
        if (id == null){
            return null;
        }

        if (PROVIDER_IS_NEED_QUERY){
            saveAllProviderNameAndID();
        }
        return redisUtils.getObject("provider:"+id,String.class);
    }


    public Long getProviderIdByName(String enterpriseName){
        if (enterpriseName == null){
            return null;
        }

        if (PROVIDER_IS_NEED_QUERY){
            saveAllProviderNameAndID();
        }
        return redisUtils.getObject("providerId:" + enterpriseName,Long.class);
    }

    public UserVO changeToUserVO(UserEntity UE){
        return new UserVO(UE.getId(),   UE.getUsername(),   UE.getPassword(),
                          getRoleNameById(UE.getRoleId()),  UE.getWorkerId(),
                          UE.getNickName(),                 UE.getTrueName());
    }
    public SellingRecordEntity changeToSellingRecordEntity(SellingRecordVO SRV){
        if (SRV == null){
            return null;
        }
        Long salesmanId = null;
        if (SRV.getId() != null){
            salesmanId = sellingRecordDao.getWorkerIdById(SRV.getId());
        }
        return new SellingRecordEntity( SRV.getId(), SRV.getRecordNumber(), getFunctionIdByFunction(SRV.getPigFunction()),
                                        getTypeIdByType(SRV.getPigType()),  getCustomerIdByName(SRV.getEnterpriseName()),
                                        SRV.getSellingCount(),              SRV.getPounds(),
                                        SRV.getActualSellingPrice(),        salesmanId);
    }

    public PurchaseRecordEntity changeToPurchaseRecordEntity(PurchaseRecordVO PRV){
        if (PRV == null){
            return null;
        }
        Long purchaserId = null;
        if (PRV.getId() != null){
            purchaserId = purchaseRecordDao.getWorkerIdById(PRV.getId());
        }
        return new PurchaseRecordEntity(PRV.getId(), PRV.getRecordNumber(), getFunctionIdByFunction(PRV.getPigFunction()),
                getTypeIdByType(PRV.getPigType()),  getProviderIdByName(PRV.getEnterpriseName()),
                PRV.getPurchaseCount(),              PRV.getPounds(),
                PRV.getActualPurchasePrice(),        purchaserId);
    }

    public SellingRecordVO changeToSellingRecordVO(SellingRecordEntity SRE){
        if (SRE == null){
            return null;
        }
        return new SellingRecordVO
                (SRE.getId(),   SRE.getRecordNumber(),  getFunctionById(SRE.getPigFunctionId()),
                getTypeById(SRE.getPigTypeId()),        getCustomerNameById(SRE.getCustomerId()),
                SRE.getSellingCount(),                  SRE.getPounds(),
                SRE.getExpectedSellingPrice(),          SRE.getActualSellingPrice(),
                SRE.getCreateDatetime(),                getSalesmanNameById(SRE.getSalesmanId()));
    }

    public PurchaseRecordVO changeToPurchaseRecordVO(PurchaseRecordEntity PRE){
        if (PRE == null){
            return null;
        }
        return new PurchaseRecordVO
                (PRE.getId(),   PRE.getRecordNumber(),  getFunctionById(PRE.getPigFunctionId()),
                getTypeById(PRE.getPigTypeId()),        getProviderNameById(PRE.getProviderId()),
                PRE.getPurchaseCount(),                  PRE.getPounds(),
                PRE.getExpectedPurchasePrice(),          PRE.getActualPurchasePrice(),
                PRE.getCreateDatetime(),                getPurchaserNameById(PRE.getPurchaserId()));
    }


    public List<SellingRecordVO> changeToSellingRecordVOS(List<SellingRecordEntity> SREList){
        if (SREList == null){
            return null;
        }
        ArrayList<SellingRecordVO> sellingRecordVOS = new ArrayList<>();
        for (SellingRecordEntity item : SREList){
            SellingRecordVO sellingRecordVO = changeToSellingRecordVO(item);
            if (sellingRecordVO != null){
                sellingRecordVOS.add(sellingRecordVO);
            }
        }
        return sellingRecordVOS;
    }

    public List<PurchaseRecordVO> changeToPurchaseRecordVOS(List<PurchaseRecordEntity> PREList){
        if (PREList == null){
            return null;
        }
        ArrayList<PurchaseRecordVO> purchaseRecordVOS = new ArrayList<>();
        for (PurchaseRecordEntity item : PREList){
            PurchaseRecordVO purchaseRecordVO = changeToPurchaseRecordVO(item);
            if (purchaseRecordVO != null){
                purchaseRecordVOS.add(purchaseRecordVO);
            }
        }
        return purchaseRecordVOS;
    }

    /**
     * @Description: 将PurchaserVO转变成Entity类
     * @Author: CaiGou
     * @Date: 2023/5/4 19:27
     **/
    public PurchaserEntity changeToPurchaserEntity(PurchaserVO PVO){
        if (PVO == null){
            return null;
        }

        return new PurchaserEntity( PVO.getId(),    getRoleIdByName(PVO.getRole()),
                                    PVO.getName(),  PVO.getPhoneNumber(),
                                    PVO.getIdentityNumber(),    PVO.getAddress(),
                                    PVO.getOnBoardingDatetime(),null,null
                                );

    }

    public PurchaserVO changeToPurchaserVO(PurchaserEntity PE){
        if (PE == null){
            return null;
        }
        return new PurchaserVO( PE.getId(),     getRoleNameById(PE.getRoleId()),
                                PE.getName(),   PE.getPhoneNumber(),
                                PE.getIdentityNumber(),
                                PE.getAddress(),PE.getOnBoardingDatetime()
                            );
    }

    public List<PurchaserVO> changeToPurchaserVOS(List<PurchaserEntity> PES){
        if (PES == null){
            return null;
        }
        List<PurchaserVO> purchaserVOS = new LinkedList<>();
        for (PurchaserEntity item : PES){
            purchaserVOS.add(changeToPurchaserVO(item));
        }
        return purchaserVOS;
    }

    public SalesmanEntity changeToSalesmanEntity(SalesmanVO SVO){

        if (SVO == null){
            return null;
        }

        return new SalesmanEntity(  SVO.getId(),    getRoleIdByName(SVO.getRole()),
                                    SVO.getName(),  SVO.getPhoneNumber(),
                                    SVO.getIdentityNumber(),    SVO.getAddress(),
                                    SVO.getOnBoardingDatetime(),null,null
                                );

    }


    public SalesmanVO changeToSalesmanVO(SalesmanEntity SE){
        if (SE == null){
            return null;
        }

        return new SalesmanVO(  SE.getId(),     getRoleNameById(SE.getRoleId()),
                                SE.getName(),   SE.getPhoneNumber(),
                                SE.getIdentityNumber(),
                                SE.getAddress(),SE.getOnBoardingDatetime()
                        );

    }

    public List<SalesmanVO> changeToSalesmanVOS(List<SalesmanEntity> SES){
        if (SES == null){
            return null;
        }
        List<SalesmanVO> salesmanVOS = new LinkedList<>();
        for (SalesmanEntity item : SES){
            salesmanVOS.add(changeToSalesmanVO(item));
        }
        return salesmanVOS;
    }

    public PigInventoryEntity changeToPigInventoryEntity(PigInventoryVO PIVO){
        if (PIVO == null){
            return null;
        }

        return new PigInventoryEntity(PIVO.getId(), getFunctionIdByFunction(PIVO.getFunction()), getTypeIdByType(PIVO.getType()),
                                      PIVO.getInventoryCount(), PIVO.getInventoryPounds(), PIVO.getWarningInventoryLine(),
                                      null,PIVO.getUpdateDatetime(),null
                                    );
    }

    public PigInventoryVO changeToPigInventoryVO(PigInventoryEntity PIE){
        if (PIE == null){
            return null;
        }

        return new PigInventoryVO(PIE.getId(),  getFunctionById(PIE.getFunctionId()),
                                  getTypeById(PIE.getTypeId()), PIE.getInventoryCount(),
                                  PIE.getInventoryPounds(),     PIE.getWarningInventoryLine(),
                                  PIE.getUpdateDatetime()
                                );
    }

    public List<PigInventoryVO> changeToPigInventoryVOS(List<PigInventoryEntity> PIES){
        if (PIES == null){
            return null;
        }
        List<PigInventoryVO> newedList = new LinkedList<>();
        for (PigInventoryEntity item : PIES){
            newedList.add(changeToPigInventoryVO(item));
        }

        return newedList;
    }


    public CustomerEntity changeToCustomerEntity(CustomerVO CVO){
        if (CVO == null){
            return null;
        }
        return new CustomerEntity(CVO.getId(),CVO.getEnterpriseName(),CVO.getAddress(),CVO.getContact(),CVO.getIdentityNumber(),CVO.getPhoneNumber(),CVO.getCreateDatetime(),null,null);
    }

    public ProviderEntity changeToProviderEntity(ProviderVO PVO){
        if (PVO == null){
            return null;
        }
        return new ProviderEntity(PVO.getId(), PVO.getEnterpriseName(), PVO.getAddress(), PVO.getContact(), PVO.getIdentityNumber(), PVO.getPhoneNumber(), PVO.getCreateDatetime(),null,null);
    }


    private void setCustomerQueryTrueDefaultTimeLater(){
        TIMER_TASKS.schedule(() -> {
            CUSTOMER_IS_NEED_QUERY = true;
        },DEFAULT_LIVE_TIME_MINUTE, TimeUnit.MINUTES);
    }

    private void setProviderQueryTrueDefaultTimeLater(){
        TIMER_TASKS.schedule(() -> {
            PROVIDER_IS_NEED_QUERY = true;
        },DEFAULT_LIVE_TIME_MINUTE, TimeUnit.MINUTES);
    }

    private void setTypeQueryTrueDefaultTimeLater(){
        TIMER_TASKS.schedule(() -> {
            PIG_TYPE_IS_NEED_QUERY = true;
        },DEFAULT_LIVE_TIME_MINUTE, TimeUnit.MINUTES);
    }

    private void setFunctionQueryTrueDefaultTimeLater(){
        TIMER_TASKS.schedule(() -> {
            PIG_FUNCTION_IS_NEED_QUERY = true;
        },DEFAULT_LIVE_TIME_MINUTE, TimeUnit.MINUTES);
    }

    private void serRoleQueryTrueDefaultTimeLater(){
        TIMER_TASKS.schedule(() -> {
            ROLE_IS_NEED_QUERY = true;
        },DEFAULT_LIVE_TIME_MINUTE, TimeUnit.MINUTES);
    }

}
