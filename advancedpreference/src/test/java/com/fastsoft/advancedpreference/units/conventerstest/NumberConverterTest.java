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
        assertEquals(1,((short)namberBinder.convert(1,Short.class)),0);
        assertEquals(2,((float)namberBinder.convert(2,Float.class)),0);
        assertEquals(3,((double)namberBinder.convert(3,Double.class)),0);
        assertEquals(4,((BigInteger)namberBinder.convert(4,BigInteger.class)).intValue());
        assertEquals(5,((BigDecimal)namberBinder.convert(5,BigDecimal.class)).intValue());
    }
}