package com.tunan.inventoryManagementSystem.service.impl;

import com.tunan.inventoryManagementSystem.dao.PigInventoryDao;
import com.tunan.inventoryManagementSystem.entity.PigInventoryEntity;
import com.tunan.inventoryManagementSystem.entity.VO.PigInventoryVO;
import com.tunan.inventoryManagementSystem.service.PigInventoryService;
import com.tunan.inventoryManagementSystem.utils.CommonBeanUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PigInventoryServiceImpl implements PigInventoryService {


    private final PigInventoryDao pigInventoryDao;

    private final CommonBeanUtils commonBeanUtils;

    public PigInventoryServiceImpl(PigInventoryDao pigInventoryDao, CommonBeanUtils commonBeanUtils) {
        this.pigInventoryDao = pigInventoryDao;
        this.commonBeanUtils = commonBeanUtils;
    }

    @Override
    public int getCounts() {
        return pigInventoryDao.count();
    }

    /**
     * @Description: 分页查询库存
     * @Author: CaiGou
     * @Date: 2023/5/10 15:13
     **/
    @Override
    public List<PigInventoryVO> pageQuery(Integer pageNum, Integer pageSize) {

        int offset = 0;
        if (pageNum != 1){
            offset = (pageNum - 1)*pageSize;
        }
        List<PigInventoryEntity> queryResults = pigInventoryDao.pageQuery(offset, pageSize);
        return commonBeanUtils.changeToPigInventoryVOS(queryResults);
    }

    /**
     * @Description:
     * @Author: CaiGou
     * @Date: 2023/5/10 15:57
     * @Param:
     * @Return:
     **/
    @Override
    public List<PigInventoryVO> pageQueryByCondition(String function, String type, Integer pageNum, Integer pageSize) {

        if (function == null && type == null){
            return null;
        }

        int offset = 0;
        if (pageNum != 1){
            offset = (pageNum - 1)*pageSize;
        }

        Integer functionId = commonBeanUtils.getFunctionIdByFunction(function);
        Integer typeId = commonBeanUtils.getTypeIdByType(type);

        List<PigInventoryEntity> queryResults = pigInventoryDao.pageQueryByCondition(functionId, typeId, offset, pageSize);

        return commonBeanUtils.changeToPigInventoryVOS(queryResults);
    }

    @Override
    public List<PigInventoryVO> pageQueryWarningInventory(Integer pageNum, Integer pageSize) {

        int offset = 0;
        if (pageNum != 1){
            offset = (pageNum - 1)*pageSize;
        }

        List<PigInventoryEntity> queryResults = pigInventoryDao.pageQueryWarningInventory(offset, pageSize);
        return commonBeanUtils.changeToPigInventoryVOS(queryResults);
    }

    @Override
    public List<PigInventoryVO> pageQueryWarningInventoryByCondition(String function, String type, Integer pageNum, Integer pageSize) {
        if (function == null && type == null){
            return null;
        }

        int offset = 0;
        if (pageNum != 1){
            offset = (pageNum - 1)*pageSize;
        }

        Integer functionId = commonBeanUtils.getFunctionIdByFunction(function);
        Integer typeId = commonBeanUtils.getTypeIdByType(type);

        List<PigInventoryEntity> queryResults = pigInventoryDao.pageQueryWarningInventoryByCondition(functionId, typeId, offset, pageSize);

        return commonBeanUtils.changeToPigInventoryVOS(queryResults);
    }

    /**
     * @Description: 修改库存的库存警告线
     * @Author: CaiGou
     * @Date: 2023/5/10 15:57
     **/
    @Override
    public boolean modifyInventoryWarningLine(@NotNull Integer id, Integer warningInventoryLine){

        if (warningInventoryLine == null){
            return false;
        }
        int isSuccess = pigInventoryDao.modifyInventoryDeadLine(id, warningInventoryLine, LocalDateTime.now());

        return isSuccess == 1;

    }

    /**
     * @Description: 添加库存的记录
     * @Author: CaiGou
     * @Date: 2023/5/10 15:57
     **/
    @Override
    public boolean addInventoryPattern(String function, String type) {
        Integer functionId = commonBeanUtils.getFunctionIdByFunction(function);
        Integer typeId = commonBeanUtils.getTypeIdByType(type);
        Integer id = Integer.valueOf(functionId.toString()+typeId);

        PigInventoryEntity judgeExistenceOrDelete = pigInventoryDao.judgeExistenceOrDelete(id);
        LocalDateTime now = LocalDateTime.now();

        int isSuccess;
        if (judgeExistenceOrDelete == null){
            //说明此条库存记录从来没有添加过
            PigInventoryEntity pigInventoryEntity = new PigInventoryEntity(id, functionId, typeId, 0, 0f, 0, now, now, 0);
            isSuccess = pigInventoryDao.addInventoryPattern(pigInventoryEntity);
        }else if (judgeExistenceOrDelete.getIsDelete() == 1){
            //说明，已经添加过了，只是is_delete字段被设置成 1 了，现在只需将delete字段改成0
            isSuccess = pigInventoryDao.modifyIsDeleteToZero(id);
            //TODO  将字段中isDelete字段重新设置为0，那么之前保留的数据是否需要清零，有待考察
        }else {
            //库存存在且 没有被删除标记设置为1
            //TODO 此处可以通过手动抛出异常，然后通过全局异常捕获类来捕获然后返回对应的错误信息。
            isSuccess = 0;
        }

        return isSuccess == 1;

    }

    /**
     * @Description: 删除某条库存记录
     * @Author: CaiGou
     * @Date: 2023/5/10 15:57
     * @Param:
     * @Return:
     **/
    @Override
    public boolean deleteInventory(@NotNull Integer id) {
        int isSuccess = pigInventoryDao.deleteInventory(id);

        return isSuccess == 1;
    }
}
