package com.tunan.inventoryManagementSystem.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 后端返回前端的实体
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private Integer total;
    private T data;


    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, Integer total) {
        this.code = code;
        this.total = total;
        this.msg = msg;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }





}
