package com.fastsoft.advancedpreference.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ura on 20-Aug-18.
 */

public class NumberConverter extends PreferenceConverter<Number,Number> {

    @Override
    public Number convertFromFirstToClass(Number from, Class<? extends Number> classToConvert) {
        return convert(from, classToConvert);
    }

    @Override
    public Number convertFromSecondToClass(Number from, Class<? extends Number> classToConvert) {
        return convert(from,classToConvert);
    }

    @Override
    public boolean isConvertible(Class<?> first, Class<?> second) {
        return Number.class.isAssignableFrom(first)&&Number.class.isAssignableFrom(second);
    }
    private Number convert(Number from, Class<? extends Number> classToConvert){
        Number number=convertPrimitives(from,classToConvert);
        if(number==null){
            if(classToConvert.equals(BigInteger.class)){
                number=BigInteger.valueOf(convertPrimitives(from,Long.class));
            }else if(classToConvert.equals(BigDecimal.class)){
                number=BigDecimal.valueOf(convertPrimitives(from,Long.class));
            }else if(classToConvert.equals(AtomicInteger.class)){
                number=new AtomicInteger(convertPrimitives(from,Integer.class));
            }else if(classToConvert.equals(AtomicLong.class)){
                number=new AtomicLong(convertPrimitives(from,Long.class));
            }
        }
        return number;
    }
    private <T>T convertPrimitives(Number from, Class<T> classToConvert){
        Number number=null;
        if(Byte.class.equals(classToConvert)) {
            number= from.byteValue();
        }
        if(Short.class.equals(classToConvert)) {
            number= from.shortValue();
        }
        if(Integer.class.equals(classToConvert)) {
            number= from.intValue();
        }
        if(Long.class.equals(classToConvert)) {
            number= from.longValue();
        }
        if(Float.class.equals(classToConvert)) {
            number= from.floatValue();
        }
        if(Double.class.equals(classToConvert)) {
            number= from.doubleValue();
        }
        return (T)number;
    }



}
