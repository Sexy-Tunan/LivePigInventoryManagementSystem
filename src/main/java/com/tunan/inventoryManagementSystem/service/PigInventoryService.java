package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.entity.VO.PigInventoryVO;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PigInventoryService {
    //获取库存记录总条数
    int getCounts();
    //分页查询
    List<PigInventoryVO> pageQuery(Integer pageNum, Integer pageSize);
    //条件分页查询
    List<PigInventoryVO> pageQueryByCondition(String function, String type, Integer pageNum, Integer pageSize);
    //分页查询库存数量低于库存警告线的库存记录
    List<PigInventoryVO> pageQueryWarningInventory(Integer pageNum, Integer pageSize);
    //按条件分页查询库存数量低于库存警告线的库存记录
    List<PigInventoryVO> pageQueryWarningInventoryByCondition(String function, String type, Integer pageNum, Integer pageSize);
    //修改库存记录的库存警告线
    boolean modifyInventoryWarningLine(@NotNull Integer id, Integer warningInventoryLine);
    //添加库存记录样式
    boolean addInventoryPattern(String function, String type);
    //删除库存记录
    boolean deleteInventory(@NotNull Integer id);
}
