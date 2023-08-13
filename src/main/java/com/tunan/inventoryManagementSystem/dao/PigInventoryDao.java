package com.tunan.inventoryManagementSystem.dao;

import com.tunan.inventoryManagementSystem.entity.PigInventoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface PigInventoryDao {

    List<PigInventoryEntity> pageQuery(Integer offset, Integer pageSize);

    List<PigInventoryEntity> pageQueryByCondition(Integer functionId, Integer typeId, Integer offset, Integer pageSize);

    List<PigInventoryEntity> pageQueryWarningInventory(Integer offset, Integer pageSize);

    List<PigInventoryEntity> pageQueryWarningInventoryByCondition(Integer functionId, Integer typeId, Integer offset, Integer pageSize);

    PigInventoryEntity getPigInventoryEntityById(Integer id);

    int modifyInventoryDeadLine(Integer id, Integer warningInventoryLine, LocalDateTime updateDatetime);

    //TODO 还没有写sql语句
    int modifyInventoryCount(Integer changeOfCount, Float changeOfPounds, Integer functionId, Integer typeId);

    int deleteInventory(Integer id);

    int modifyIsDeleteToZero(Integer id);

    int addInventoryPattern(PigInventoryEntity pigInventoryEntity);

    int count();

    //判断记录是否存在，（注意被删除了，只是is——delete字段被置为1了，这种情况也是存在的意思）
    PigInventoryEntity judgeExistenceOrDelete(Integer id);

}
