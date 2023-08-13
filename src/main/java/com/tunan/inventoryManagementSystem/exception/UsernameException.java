package com.tunan.inventoryManagementSystem.exception;

public class UsernameException extends RuntimeException{
    //抛出异常时，调用对应的handler进行处理
    public String message;
    public Integer code;

    public UsernameException(){
    }

    public UsernameException(String message, Integer code){
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
