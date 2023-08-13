package com.tunan.inventoryManagementSystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用来存储，各类生猪生猪库存信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PigInventoryEntity {

    //数据库主键id
    private Integer id;

    //猪的功能
    private Integer functionId;

    //猪的种类
    private Integer typeId;

    //库存数量
    private Integer inventoryCount;

    //库存重量
    private Float inventoryPounds;

    //库存警告线
    private Integer warningInventoryLine;

    //库存创建时间
    private LocalDateTime createDatetime;

    //库存最近一次更新时间
    private LocalDateTime updateDatetime;

    //删除标志
    private Integer isDelete;

}
