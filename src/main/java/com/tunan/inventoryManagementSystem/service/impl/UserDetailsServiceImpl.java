package com.tunan.inventoryManagementSystem.service.impl;

import com.tunan.inventoryManagementSystem.dao.UserDao;
import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.entity.UserEntity;
import com.tunan.inventoryManagementSystem.entity.VO.UserVO;
import com.tunan.inventoryManagementSystem.exception.UsernameException;
import com.tunan.inventoryManagementSystem.utils.CommonBeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDao userDao;

    private final CommonBeanUtils commonBeanUtils;

    public UserDetailsServiceImpl(UserDao userDao, CommonBeanUtils commonBeanUtils) {
        this.userDao = userDao;
        this.commonBeanUtils = commonBeanUtils;
    }

    /**
     *
     * @param username 登录时所使用的用户名
     * @return 返回的UserDetails会将我们从数据查询出来的用户名密码和权限进行封装，后续进行与前端传来的用户名密码进行比对
     */
    @Override
    public UserDetails loadUserByUsername(String username)  {
        //从数据库中查询出用户信息
        UserEntity userEntity = userDao.getUserByUsername(username);
        //此时要进行判断，用户是否查询出来了
        if(Objects.isNull(userEntity)){
            throw new UsernameException();
        }
        UserVO user = commonBeanUtils.changeToUserVO(userEntity);
        //此时如果不为空，那么我们需要获取其菜单授权信息
        List<String> authorizesList = userDao.getUserPermissions(userEntity.getRoleId());
//        System.out.println(userVO);
        return new LoginUser(user,authorizesList);
    }
}
