package com.tunan.inventoryManagementSystem.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class TimeUtils {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);


    /**
     * @Description: 根据传入的字符串和日期格式返回对应的Date对象
     * @Author: CaiGou
     * @Date: 2023/4/19 15:00
     * @Param:
     * @Return:
     **/
    public static LocalDateTime dateTimeOf(String time, String format){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return LocalDateTime.parse(time, formatter);
    }

    /**
     * @Description: 根据传入的日期字符串装换成LocalDateTime对象，
     * @Author: CaiGou
     * @Date: 2023/4/19 15:15
     * @Param:
     * @Return:
     **/
    public static LocalDateTime dateTimeOf(String time){
        if(time == null || time.equals("")){
            return null;
        }
        return LocalDateTime.parse(time, DEFAULT_FORMATTER);
    }


    /**
     * @Description: 以默认的日期时间格式化器解析时间为字符串
     * @Author: CaiGou
     * @Date: 2023/4/19 15:50
     * @Param:
     * @Return:
     **/
    public static String parseLocalDateTime(LocalDateTime dateTime){

        if(dateTime == null){
            return null;
        }

        return dateTime.format(DEFAULT_FORMATTER);
    }


    /**
     * @Description: 将LocalDateTime转换为Instant对象，然后转换为时间戳
     * @Author: CaiGou
     * @Date: 2023/4/19 16:02
     * @Param:
     * @Return:
     **/
    public static Long getTimestamp(LocalDateTime dateTime){
        if (dateTime == null){
            return null;
        }

        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();

        return instant.toEpochMilli();

    }

}
