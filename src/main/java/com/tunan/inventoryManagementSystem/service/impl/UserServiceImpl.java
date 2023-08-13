package com.tunan.inventoryManagementSystem.service.impl;

import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.domin.Result;
import com.tunan.inventoryManagementSystem.domin.SuccessCode;
import com.tunan.inventoryManagementSystem.entity.VO.UserVO;
import com.tunan.inventoryManagementSystem.exception.PasswordException;
import com.tunan.inventoryManagementSystem.service.UserService;
import com.tunan.inventoryManagementSystem.utils.JwtUtils;
import com.tunan.inventoryManagementSystem.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;

    private final RedisUtils redisUtils;

    public UserServiceImpl(AuthenticationManager authenticationManager, RedisUtils redisUtils) {
        this.authenticationManager = authenticationManager;
        this.redisUtils = redisUtils;
    }

    @Override
    public Result<Map<String,String>> loginVerification(UserVO userVO) {
        /*登录认证这里，我们可以查看认证流程图，此时我们应该要将前端传来的数据封装成AuthenticationToken对象，并通过AuthenticationManager
        调用其authenticate方法，将封装的数据对象传入，后续会将其与从数据库查询出来的数据进行比对
        */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userVO.getUsername(),userVO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //此时如果密码认证成功，会返回一个填充了用户信息和权限的Authentication对象，此时我们需要生成JWT令牌
        if(Objects.isNull(authenticate)){
            //认证失败，抛出密码异常，会被全局异常控制器捕获并返回异常信息给前端。
            throw new PasswordException();
        }
        //认证成功，封装JWT
        //从返回的authentication对象中获取UserDetail的实例
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();//principal是凭证,保存了用户信息。
        String userId = loginUser.getUserVO().getId().toString();
        String Jwt = JwtUtils.createJWT(userId);//设置的默认过期时间为30分钟。
        Map<String,String> map = new HashMap<>();
        map.put("token",Jwt);
        //将loginUser信息存储在redis中，默认存储时间是30分钟
        redisUtils.saveObject("login:"+userId,loginUser,40);
        return new Result<>(SuccessCode.SUCCESS_CODE,SuccessCode.SUCCESS_MESS,map);
    }

    @Override
    public Map<String,String> refreshJWT(String token) {
        //TODO 重新生成JWT并返回给前端
        Claims claims = JwtUtils.parseJWT(token);
        String userId = claims.getSubject();

        String jwt = JwtUtils.createJWT(userId);
        LoginUser loginUser = redisUtils.getObject("login" + userId, LoginUser.class);
        redisUtils.saveObject("login"+userId,loginUser,40);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return map;
    }

    @Override
    public LoginUser getLoginUser() {

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return (LoginUser) authentication.getPrincipal();
    }
}
