package com.tunan.inventoryManagementSystem.config;

import com.tunan.inventoryManagementSystem.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    public MySecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    //将AuthenticationManager注入容器，获得一个全局的Manager，在需要的时候注入获取就行。
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    //将密码加密方式改为为BCrypt(密码加密存储)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //屏蔽SpringSecurity的默认登录页
        http.removeConfigurer(DefaultLoginPageConfigurer.class);

        http
                //关闭csrf
                .csrf().disable().httpBasic().and()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers(
                        "/swagger*",
                        "/v2/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/user/login"
                ).permitAll()   //anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //添加token校验过滤器（自定义）
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
//        http.exceptionHandling()
//                //配置认证失败处理器
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();

    }


}
