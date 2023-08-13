package com.tunan.inventoryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PigFunction {

    private Integer id;

    private String function;

    public static final int PIGLET = 1;

    public static final int BREEDING_PIG = 2;

    public static final int MEAT_PIG = 3;

    public static final int FEMALE_PIG = 4;
}
