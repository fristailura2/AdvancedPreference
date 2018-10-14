package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

public class EnumConverter extends PreferenceConverter<Enum,String>{
    @Override
    protected String convertFromFirstToClass(@NonNull Enum from, @NonNull Class<? extends String> classToConvert) {
        return (String) convert(from,classToConvert);
    }

    @Override
    protected Enum convertFromSecondToClass(@NonNull String from, @NonNull Class<? extends Enum> classToConvert) {
        return (Enum) convert(from,classToConvert);
    }
    public Object convert(@NonNull Object from,@NonNull Class classToConvert){
        if(from.getClass().isEnum()){
            return ((Enum)from).name();
        }else{
            for(Enum possible:((Class<Enum>)classToConvert).getEnumConstants())
                if(possible.name().equals(from))
                    return possible;
        }
        return null;
    }

    @Override
    public boolean isConvertible(@NonNull Class<?> first, @NonNull Class<?> second) {
        return (!first.equals(second))&&
                (first.isEnum()||second.isEnum())&&
                (String.class.equals(first)||String.class.equals(second));
    }
}
