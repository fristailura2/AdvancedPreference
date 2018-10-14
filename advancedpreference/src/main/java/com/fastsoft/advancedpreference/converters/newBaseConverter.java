package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.utils.Objects;

public abstract class newBaseConverter implements Comparable<newBaseConverter>{
    public abstract boolean isConvertible(Class<?> first, Class<?> second);
    abstract Object convertPrivate(Object from,Class<?> toClass);

    public Object convert(Object from, Class<?> toClass) {
        Objects.throwIfNullParam(from,"from");
        Objects.throwIfNullParam(from,"toClass");

        if(!isConvertible(from.getClass(),toClass))
            throw new IllegalArgumentException(String.format("can not convert from %s to %s",from.getClass().getSimpleName(),toClass.getSimpleName()));

        return convertPrivate(from,toClass);
    }

    @Override
    public int compareTo(@NonNull newBaseConverter o) {
        int delta = hashCode() - o.hashCode();
        return delta/(Math.abs(delta)==0?1:Math.abs(delta));
    }

}
