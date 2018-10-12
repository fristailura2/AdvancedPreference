package com.fastsoft.advancedpreference.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ura on 20-Aug-18.
 */

public class ReflectionUtils {
    private static final List primitivesAndWrapped=Collections.unmodifiableList(Arrays.asList("Integer","Long","Short","Byte","Float","Double","Character","Void","Boolean","int","long","short","byte","float","double","char","void","boolean"));
    public static Class<?> getMethodGenericReturnClass(Method method, int genericIndex){
        return (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[genericIndex];
    }
    public static Type getMethodGenericReturnType(Method method,int genericIndex){
        return ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[genericIndex];
    }
    public static Type[] getParentGenericParams(Class<?> someClass){
        if(someClass.getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) someClass.getGenericSuperclass();
            return p.getActualTypeArguments();
        }
        return new Type[]{};
    }
    public static Type getMethodGenericParam(Method method,int genericIndex){
        Type[] res = method.getGenericParameterTypes();
        return res[genericIndex];
    }
    public static boolean isPrimitiveOrWrappedPrimitive(Class primitiveWrapClass){
        return primitivesAndWrapped.contains(primitiveWrapClass.getSimpleName());
    }
    public static Class findPrimitiveByWrap(Class primitiveWrapClass){
        Class res=primitiveWrapClass;
        switch (primitiveWrapClass.getSimpleName()){
            case "Integer":
                res=int.class;
                break;
            case "Long":
                res=long.class;
                break;
            case "Short":
                res=short.class;
                break;
            case "Byte":
                res=byte.class;
                break;
            case "Float":
                res=float.class;
                break;
            case "Double":
                res=double.class;
                break;
            case "Character":
                res=char.class;
                break;
            case "Void":
                res=void.class;
                break;
            case "Boolean":
                res=boolean.class;
                break;

        }
        return res;
    }
    public static Class findWrapByPrimitive(Class primitiveClass){
        Class res=primitiveClass;
        switch (primitiveClass.getSimpleName()){
            case "int":
                res=Integer.class;
                break;
            case "long":
                res=Long.class;
                break;
            case "short":
                res=Short.class;
                break;
            case "byte":
                res=Byte.class;
                break;
            case "float":
                res=Float.class;
                break;
            case "double":
                res=Double.class;
                break;
            case "char":
                res=Character.class;
                break;
            case "void":
                res=Void.class;
                break;
            case "boolean":
                res=Boolean.class;
                break;

        }
        return res;
    }

}
