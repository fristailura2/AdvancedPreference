package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;


import com.fastsoft.advancedpreference.utils.ReflectionUtils;

public class PrimitiveConverter extends newBaseConverter {
    private NumberConverter numberConverter;

    public PrimitiveConverter(NumberConverter numberConverter) {
        this.numberConverter = numberConverter;
    }

    @Override
    public boolean isConvertible(@NonNull Class first, @NonNull Class second) {
        if(!(ReflectionUtils.isPrimitiveOrWrappedPrimitive(first)&&
                ReflectionUtils.isPrimitiveOrWrappedPrimitive(second)))
            return false;
        Class wrappedFirst = ReflectionUtils.findWrapByPrimitive(first);
        Class wrappedSecond = ReflectionUtils.findWrapByPrimitive(second);

        return  (numberConverter.isConvertible(wrappedFirst,wrappedSecond)||wrappedFirst.equals(wrappedSecond));
    }

    @Override
    Object convertPrivate(Object from, Class<?> toClass) {
        if(ReflectionUtils.findWrapByPrimitive(from.getClass()).equals(ReflectionUtils.findWrapByPrimitive(toClass)))
            return from;
        return numberConverter.convert(from,
                toClass.isPrimitive()?
                        ReflectionUtils.findWrapByPrimitive(toClass):(Class<Number>)toClass);
    }
}
