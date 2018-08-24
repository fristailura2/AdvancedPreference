package com.fastsoft.advancedpreference.converters;

/**
 * Created by ura on 12-Aug-18.
 */

public class SameTypeConverter extends PreferenceConverter<Object,Object> {

    @Override
    public Object convertFromFirstToClass(Object from, Class classToConvert) {
        return from;
    }

    @Override
    public Object convertFromSecondToClass(Object from, Class classToConvert) {
        return from;
    }

    @Override
    public boolean isConvertible(Class first, Class second) {
        return first.equals(second);
    }
}
