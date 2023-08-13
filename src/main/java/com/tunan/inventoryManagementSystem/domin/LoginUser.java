package com.tunan.inventoryManagementSystem.domin;

import com.alibaba.fastjson.annotation.JSONField;
import com.tunan.inventoryManagementSystem.entity.VO.UserVO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class LoginUser implements UserDetails {

    private UserVO userVO;

    private List<String> permissions;

    //这个authorities保存着所有的授权信息对象。
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;


    public LoginUser() {
    }

    public LoginUser(UserVO userVO, List<String> permissions) {
        this.userVO = userVO;
        this.permissions = permissions;
    }

    //获取权限方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if( authorities != null ){
            return authorities;
        }
        //如果还未授权，那么就将permission中将权限转换成GrantedAuthority对象集返回
        authorities = new ArrayList<>();
        for(String permission : permissions){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userVO.getPassword();
    }

    @Override
    public String getUsername() {
       return userVO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
