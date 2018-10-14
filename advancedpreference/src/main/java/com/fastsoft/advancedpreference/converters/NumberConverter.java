package com.fastsoft.advancedpreference.converters;



import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ura on 20-Aug-18.
 */

public class NumberConverter extends newBaseConverter {


    @Override
    public boolean isConvertible(Class<?> first, Class<?> second) {
        return Number.class.isAssignableFrom(first)&&Number.class.isAssignableFrom(second)&&(!first.equals(second));
    }

    @Override
    Object convertPrivate(Object from1, Class<?> toClass) {
        Number from= (Number) from1;
        Number number=convertPrimitives(from,(Class<Number>) toClass);
        if(number==null){
            if(toClass.equals(BigInteger.class)){
                number=BigInteger.valueOf(convertPrimitives(from,Long.class));
            }else if(toClass.equals(BigDecimal.class)){
                number=BigDecimal.valueOf(convertPrimitives(from,Long.class));
            }else if(toClass.equals(AtomicInteger.class)){
                number=new AtomicInteger(convertPrimitives(from,Integer.class));
            }else if(toClass.equals(AtomicLong.class)){
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
