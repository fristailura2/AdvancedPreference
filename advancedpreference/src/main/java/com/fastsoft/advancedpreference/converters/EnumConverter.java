package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

public class EnumConverter extends PreferenceConverter<Enum,String>{
    @Override
    protected String convertFromFirstToClass(@NonNull Enum from, @NonNull Class<? extends String> classToConvert) {
        return from.name();
    }

    @Override
    protected Enum convertFromSecondToClass(@NonNull String from, @NonNull Class<? extends Enum> classToConvert) {
        for(Enum possible:classToConvert.getEnumConstants())
            if(possible.name().equals(from))
                return possible;
        return null;
    }

    @Override
    public boolean isConvertible(@NonNull Class<?> first, @NonNull Class<?> second) {
        return (!first.equals(second))&&
                (first.isEnum()||second.isEnum())&&
                (String.class.equals(first)||String.class.equals(second));
    }
}
