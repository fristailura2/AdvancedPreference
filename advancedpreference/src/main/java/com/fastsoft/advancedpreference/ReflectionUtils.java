package com.fastsoft.advancedpreference;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ura on 20-Aug-18.
 */

public class ReflectionUtils {
    public static Class<?> getMethodGenericReturnType(Method method,int genericIndex){
        return (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[genericIndex];
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
}
