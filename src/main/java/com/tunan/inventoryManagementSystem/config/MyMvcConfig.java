package com.tunan.inventoryManagementSystem.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

//@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

//    /**
//     * attention:如果使用了拦截器，那么就不能使用配置webMvcConfigurer来实现跨域请求，而是使用filter来实行跨域请求
//     *      * @param registry
//     */
    //添加跨域请求：在SpringSecurity的配置类配置了
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 设置允许跨域的路径
//        registry.addMapping("/**")
//                // 设置允许跨域请求的域名
//                .allowedOrigins("*")
//                //当设置跨域请求是否允许携带Cookie等信息时，需要手动设置允许访问域名有哪些，而不能使用allowedOrigins("*")
//                .allowedOriginPatterns("http://47.120.42.199:8080","http://localhost:8080")
//                // 设置允许的请求方式
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                // 设置允许的header属性
//                .allowedHeaders("token")
//                // 跨域允许时间
//                .maxAge(3600);
//    }


}
