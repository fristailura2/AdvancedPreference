package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.utils.ReflectionUtils;

public class PrimitiveConverter extends PreferenceConverter {
    private NumberConverter numberConverter;

    public PrimitiveConverter(NumberConverter numberConverter) {
        this.numberConverter = numberConverter;
    }

    @Override
    protected Object convertFromFirstToClass(@NonNull Object from, @NonNull Class classToConvert) {
        return convert(from,classToConvert);
    }

    @Override
    protected Object convertFromSecondToClass(@NonNull Object from, @NonNull Class classToConvert) {
        return convert(from,classToConvert);
    }
    private Object convert(@NonNull Object from, @NonNull Class classToConvert){
        return numberConverter.convertFromFirstToClass((Number) from,
                classToConvert.isPrimitive()?
                ReflectionUtils.findWrapByPrimitive(classToConvert):classToConvert);
    }

    @Override
    public boolean isConvertible(@NonNull Class first, @NonNull Class second) {
        return  numberConverter.isConvertible(ReflectionUtils.findWrapByPrimitive(first),ReflectionUtils.findWrapByPrimitive(second))&&
                (first.isPrimitive()||second.isPrimitive());
    }
}
