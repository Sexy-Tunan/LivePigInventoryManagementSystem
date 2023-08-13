package com.tunan.inventoryManagementSystem.exception;

public class PasswordException extends RuntimeException {

    public String message;
    public Integer code;

    public PasswordException(){
    }

    public PasswordException(String message, Integer code){
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
