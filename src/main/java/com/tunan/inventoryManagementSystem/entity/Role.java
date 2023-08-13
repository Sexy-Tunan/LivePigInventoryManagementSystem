package com.tunan.inventoryManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private Integer id;

    private String name;

    public static final int ADMINISTRATOR_ROLE_ID = 1;

    public static final int MANAGER_ROLE_ID = 2;

    public static final int PURCHASER_ROLE_ID = 3;

    public static final int SALESMAN_ROLE_ID = 4;
}
