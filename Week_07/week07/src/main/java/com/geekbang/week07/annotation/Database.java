package com.geekbang.week07.annotation;

public @interface Database {
    boolean readOnly() default true;
}
