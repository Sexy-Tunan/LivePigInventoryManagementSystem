package com.tunan.inventoryManagementSystem.exception;

public class ExpiredTokenException extends RuntimeException{
    public String message;
    public Integer code;

    public ExpiredTokenException(){}

    public ExpiredTokenException(Integer code, String message){
        super(message);
        this.message = message;
        this.code = code;
    }

    public String getMessage(){
        return message;
    }

    public Integer getCode(){
        return code;
    }
}
