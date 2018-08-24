package com.fastsoft.advancedpreference.conventerstest;

import com.fastsoft.advancedpreference.converters.NumberConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
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
        namberBinder.convertFromFirstTo(new Integer(1),Integer.class);
        namberBinder.convertFromFirstTo(new Integer(2),Float.class);
        namberBinder.convertFromFirstTo(new Integer(3),Double.class);
        namberBinder.convertFromFirstTo(new Integer(4),BigInteger.class);
        namberBinder.convertFromFirstTo(new Integer(5),BigDecimal.class);
    }
}