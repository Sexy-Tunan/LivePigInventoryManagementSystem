package com.tunan.inventoryManagementSystem.filter;


import com.tunan.inventoryManagementSystem.domin.ErrorCode;
import com.tunan.inventoryManagementSystem.domin.LoginUser;
import com.tunan.inventoryManagementSystem.exception.ExpiredTokenException;
import com.tunan.inventoryManagementSystem.exception.IllegalTokenException;
import com.tunan.inventoryManagementSystem.utils.JwtUtils;
import com.tunan.inventoryManagementSystem.utils.RedisUtils;
import io.jsonwebtoken.Claims;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

//这个过滤器用来防范csrf攻击，检查token是否正确，检查请求源是否来自其他站点，
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisUtils redisUtils;

    public JwtAuthenticationTokenFilter(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //检查请求源referer是否
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行，因为这是一个过滤器执行链，我们只是添加了一个过滤器在SpringSecurity的过滤器链中来判断token是否合法。
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        Claims claims;
        try {
            claims = JwtUtils.parseJWT(token);
            userid = claims.getSubject();
            //解析token，查看是否过期，是的话抛出
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())){
                throw new ExpiredTokenException(ErrorCode.TOKEN_EXPIRATION,ErrorCode.TOKEN_EXPIRATION_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalTokenException(ErrorCode.ILLEGAL_TOKEN,ErrorCode.ILLEGAL_TOKEN_MEG);
        }

        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisUtils.getObject(redisKey,LoginUser.class);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //创建新的SecurityContext对象，避免多线程竞争。
//        SecurityContextHolder.createEmptyContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
