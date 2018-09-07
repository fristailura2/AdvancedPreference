package com.fastsoft.advancedpreference.units.conventerstest;

import com.fastsoft.advancedpreference.converters.NumberConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ura on 22-Aug-18.
 */

@RunWith(JUnit4.class)
public class NumberConverterTest {
    NumberConverter namberBinder =new NumberConverter();
    @Test
    public void illegalConvertTest() throws IllegalAccessException {
        for (Method method:NumberConverter.class.getMethods()) {
            if(method.getName().equals("convertFromFirstTo")) {
                try {
                    method.invoke(namberBinder, new Integer(1), String.class);
                }catch (InvocationTargetException e){
                    assertEquals(e.getCause().getClass(),IllegalArgumentException.class);
                }

            }
        }
    }
    @Test
    public void convertTest(){
        assertTrue(1==namberBinder.convertFromFirstTo(1,Short.class).intValue());
        assertTrue(2==namberBinder.convertFromFirstTo(2,Float.class).intValue());
        assertTrue(3==namberBinder.convertFromFirstTo(3,Double.class).intValue());
        assertTrue(4==namberBinder.convertFromFirstTo(4,BigInteger.class).intValue());
        assertTrue(5==namberBinder.convertFromFirstTo(5,BigDecimal.class).intValue());
    }
}