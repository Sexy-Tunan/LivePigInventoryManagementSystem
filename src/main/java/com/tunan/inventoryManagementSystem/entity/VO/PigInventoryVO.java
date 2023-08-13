package com.tunan.inventoryManagementSystem.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PigInventoryVO {

    //数据库主键id
    @NotNull
    private Integer id;

    //猪的功能
    private String function;

    //猪的种类
    private String type;

    //库存数量
    private Integer inventoryCount;

    //库存重量
    private Float inventoryPounds;

    //库存警告线
    private Integer warningInventoryLine;

    //库存最近一次更新时间
    private LocalDateTime updateDatetime;

}
