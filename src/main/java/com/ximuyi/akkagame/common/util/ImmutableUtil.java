package com.ximuyi.akkagame.common.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ImmutableUtil {

    public static <K,V> Map<K, V> add(Map<K,V> map, K key, V value){
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        builder.putAll(map);
        builder.put(key, value);
        return builder.build();
    }
}
