package com.ximuyi.akkagame.common.util;

public class DoubleEntry<T0, T1> {
    public final T0 first;
    public final T1 second;

    public DoubleEntry(T0 first, T1 second) {
        this.first = first;
        this.second = second;
    }

    public DoubleEntry<T0, T1> clone(){
        return new DoubleEntry<>(first, second);
    }

    public boolean eitherNull(){
        return first == null || second == null;
    }
}
