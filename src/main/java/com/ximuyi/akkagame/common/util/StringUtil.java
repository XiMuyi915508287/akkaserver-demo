package com.ximuyi.akkagame.common.util;

import java.util.Collection;
import java.util.function.Function;

public class StringUtil {

    public static <T> String join(Collection<T> sourceList, String connector)
    {
        return join(sourceList, connector, null);
    }

    /**
     * 使用 connector 对 sourceList 进行连接，对每个sourceList 使用 functor 获取需要连接的数值
     * @param sourceList	源数组
     * @param connector		连接符
     * @param functor		连接算子
     * @return 				连接成功后的字符串
     */
    public static <T> String join(Collection<T> sourceList, String connector, Function<T, String> functor)
    {
        if(sourceList.isEmpty()){
            return "";
        }

        String result = "";
        String concat = "";
        for(T elem : sourceList){
            if(functor != null){
                result = result + concat + functor.apply(elem);
            }else{
                result = result + concat + String.valueOf(elem);
            }
            concat = connector;
        }

        return result;
    }
}
