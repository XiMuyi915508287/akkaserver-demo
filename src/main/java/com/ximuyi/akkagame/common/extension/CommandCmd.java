package com.ximuyi.akkagame.common.extension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)  //注意要增加运行时
public @interface CommandCmd {
    short value();
    boolean threadSafe() default false;
}
