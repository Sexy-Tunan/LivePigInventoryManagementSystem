package com.tunan.inventoryManagementSystem.exception;

public class IllegalTokenException extends RuntimeException {
    public String message;
    public Integer code;

    public IllegalTokenException(){}

    public IllegalTokenException(Integer code, String message){
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
