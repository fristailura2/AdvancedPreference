package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ura on 20-Aug-18.
 */

public class StringNumberConverter extends PreferenceConverter<String,Number> {

    @Override
    public Number convertFromFirstToClass(String from, Class<? extends Number> classToConvert) {
        Number number=null;
        Double doubleVal=Double.valueOf(from);
        if(Byte.class.equals(classToConvert)) {
            number=doubleVal.byteValue();
        }else if(Short.class.equals(classToConvert)) {
            number=doubleVal.shortValue();
        }else if(Integer.class.equals(classToConvert)) {
            number= doubleVal.intValue();
        }else if(Long.class.equals(classToConvert)) {
            number= doubleVal.longValue();
        }else if(Float.class.equals(classToConvert)) {
            number= doubleVal.floatValue();
        }else if(Double.class.equals(classToConvert)) {
            number=doubleVal;
        }else if(BigDecimal.class.equals(classToConvert)) {
            number=new BigDecimal(from);
        }else if(BigInteger.class.equals(classToConvert)) {
            number=new BigInteger(from);
        }else if(AtomicInteger.class.equals(classToConvert)) {
            number=new AtomicInteger(doubleVal.intValue());
        }else if(AtomicLong.class.equals(classToConvert)) {
            number=new AtomicLong(doubleVal.longValue());
        }
        return number;
    }

    @Override
    public String convertFromSecondToClass(Number from, Class<? extends String> classToConvert) {
        return from.toString();
    }

    @Override
    public boolean isConvertible(@NonNull Class<?> first, @NonNull Class<?> second) {
        return ((Number.class.isAssignableFrom(first)&&second.equals(String.class))||(
                Number.class.isAssignableFrom(second)&&first.equals(String.class)));

    }
}
