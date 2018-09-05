package com.fastsoft.advancedpreference.utils;

public class Objects {
    public static  <T>T requireNonNull(T object,String message){
        throwIfNull(object,message);
        return object;
    }
    public static  <T>T requireNonParam(T object,String paramName){
        throwIfNull(object,String.format("Param \"%s\" should not be null",paramName));
        return object;
    }
    public static boolean isNull(Object object){
        return object==null;
    }
    public static void throwIfNull(Object object,String message){
        if(isNull(object))
            throw new NullPointerException(message);
    }
    public static void throwIfNullParam(Object object,String paramName){
        if(isNull(object))
            throw new NullPointerException(String.format("Param \"%s\" should not be null",paramName));
    }
    public static void throwIfNotNullParam(Object object,String paramName){
        if(!isNull(object))
            throw new IllegalArgumentException(String.format("Param \"%s\" should be null",paramName));
    }
}
