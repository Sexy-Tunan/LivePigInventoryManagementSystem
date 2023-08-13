package com.tunan.inventoryManagementSystem.utils;


import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonStaticUtils {


    private static final String PHONE_NUMBER_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    private static final String IDENTITY_NUMBER_18_REGEX = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    private static final String IDENTITY_NUMBER_15_REGEX = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";


    /**
     * @Description: 根据当前系统时间得到时间戳------》生成订单号
     * @Author: CaiGou
     * @Date: 2023/4/19 16:15
     * @Param:
     * @Return:
     **/
    public static String createSellingOrderNumber(){

        Long timesTamp = TimeUtils.getTimestamp(LocalDateTime.now());

        String middlePart = Long.toString(timesTamp);

        return "SR" + middlePart;
    }

    public static String createPurchaseOrderNumber(){

        Long timesTamp = TimeUtils.getTimestamp(LocalDateTime.now());

        String middlePart = Long.toString(timesTamp);

        return "CG" + middlePart;
    }

    /**
     * @Description: 校验手机号码
     * @Author: CaiGou
     * @Date: 2023/4/22 17:18
     * @Param:
     * @Return:
     **/
    public static boolean verifyPhoneNumber(String phoneNumber){
        Pattern compile = Pattern.compile(PHONE_NUMBER_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * @Description: 校验身份证号码
     * @Author: CaiGou
     * @Date: 2023/4/22 17:18
     * @Param:
     * @Return:
     **/
    public static boolean verifyIdentityNumber(String identityNumber){
        Pattern compile1 = Pattern.compile(IDENTITY_NUMBER_15_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = compile1.matcher(identityNumber);
        if (matcher1.matches()){
            return true;
        }

        Pattern compile2 = Pattern.compile(IDENTITY_NUMBER_18_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = compile2.matcher(identityNumber);

        return matcher2.matches();

    }



}
