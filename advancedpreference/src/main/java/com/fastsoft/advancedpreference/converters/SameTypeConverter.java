package com.fastsoft.advancedpreference.converters;



/**
 * Created by ura on 12-Aug-18.
 */

public class SameTypeConverter extends newBaseConverter {

    @Override
    public boolean isConvertible(Class<?> first, Class<?> second) {
        return first.equals(second);
    }

    @Override
    Object convertPrivate(Object from, Class<?> toClass) {
        return from;
    }
}
