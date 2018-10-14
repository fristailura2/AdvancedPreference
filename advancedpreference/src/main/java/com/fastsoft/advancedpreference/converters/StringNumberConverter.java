package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;



import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ura on 20-Aug-18.
 */

public class StringNumberConverter extends newBaseConverter{


    @Override
    public boolean isConvertible(@NonNull Class<?> first, @NonNull Class<?> second) {
        return ((Number.class.isAssignableFrom(first)&&second.equals(String.class))||(
                Number.class.isAssignableFrom(second)&&first.equals(String.class)));

    }

    @Override
    Object convertPrivate(Object from, Class<?> toClass) {
        if(String.class.equals(from.getClass()))
            return convertFromString(from,toClass);
        else
            return convertFromNumb(from,toClass);

    }

    private String convertFromNumb(Object from, Class<?> toClass) {
        return from.toString();
    }

    private Number convertFromString(Object from1, Class<?> toClass){
        String from= (String) from1;
        Number number=null;
        Double doubleVal=Double.valueOf(from);
        if(Byte.class.equals(toClass)) {
            number=doubleVal.byteValue();
        }else if(Short.class.equals(toClass)) {
            number=doubleVal.shortValue();
        }else if(Integer.class.equals(toClass)) {
            number= doubleVal.intValue();
        }else if(Long.class.equals(toClass)) {
            number= doubleVal.longValue();
        }else if(Float.class.equals(toClass)) {
            number= doubleVal.floatValue();
        }else if(Double.class.equals(toClass)) {
            number=doubleVal;
        }else if(BigDecimal.class.equals(toClass)) {
            number=new BigDecimal(from);
        }else if(BigInteger.class.equals(toClass)) {
            number=new BigInteger(from);
        }else if(AtomicInteger.class.equals(toClass)) {
            number=new AtomicInteger(doubleVal.intValue());
        }else if(AtomicLong.class.equals(toClass)) {
            number=new AtomicLong(doubleVal.longValue());
        }
        return number;
    }
}
