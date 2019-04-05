package com.cyh.netty.util;

import com.cyh.netty.constant.ConstantValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

    public static void print(String message){
        System.out.println("["+returnDateStr()+"] "+message);
    }

    public static String returnDateStr(){
        return new SimpleDateFormat(ConstantValue.DATE_FORMAT).format(new Date());
    }
}
