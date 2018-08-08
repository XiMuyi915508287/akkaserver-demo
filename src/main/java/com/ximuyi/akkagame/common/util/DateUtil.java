package com.ximuyi.akkagame.common.util;

public class DateUtil {

    public static int getCurrentSecond(){
        return (int)(System.currentTimeMillis() / 1000);
    }
}
