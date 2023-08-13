package com.tunan.inventoryManagementSystem.exception;

public class DatabaseUpdateException extends Exception{
    public String message;
    public Integer code;

    public DatabaseUpdateException(){}

    public DatabaseUpdateException(Integer code, String message){
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
