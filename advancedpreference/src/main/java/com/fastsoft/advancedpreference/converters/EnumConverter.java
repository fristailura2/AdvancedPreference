package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;



public class EnumConverter extends newBaseConverter{

    @Override
    public boolean isConvertible(@NonNull Class<?> first, @NonNull Class<?> second) {
        return (!first.equals(second))&&
                (first.isEnum()||second.isEnum())&&
                (String.class.equals(first)||String.class.equals(second));
    }

    @Override
    Object convertPrivate(Object from, Class<?> toClass) {
        if(from.getClass().isEnum()){
            return ((Enum)from).name();
        }else{
            for(Enum possible:((Class<Enum>)toClass).getEnumConstants())
                if(possible.name().equals(from))
                    return possible;
        }
        return null;
    }
}
