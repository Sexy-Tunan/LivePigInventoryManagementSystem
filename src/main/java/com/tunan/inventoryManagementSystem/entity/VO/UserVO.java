package com.tunan.inventoryManagementSystem.entity.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private Long id;

    private String username;

    private String password;

    private String role;

    private Long workerId;

    private String nickName;

    private String trueName;

}
