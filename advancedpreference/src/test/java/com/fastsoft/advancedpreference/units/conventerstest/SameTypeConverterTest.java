package com.fastsoft.advancedpreference.units.conventerstest;

import com.fastsoft.advancedpreference.converters.SameTypeConverter;

import static  org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.TreeSet;

/**
 * Created by ura on 21-Aug-18.
 */
@RunWith(JUnit4.class)
public class SameTypeConverterTest {
    SameTypeConverter sameTypeConverter =new SameTypeConverter();
    @Test(expected = IllegalArgumentException.class)
    public void illegalConvertTest(){
        sameTypeConverter.convert(new Integer(1),String.class);
    }
    @Test
    public void convertTest(){
        sameTypeConverter.convert(1,Integer.class);
        sameTypeConverter.convert(new TreeSet<>(),TreeSet.class);
        sameTypeConverter.convert("",String.class);
        sameTypeConverter.convert(1f,Float.class);
        sameTypeConverter.convert(34d,Double.class);
    }
    @Test
    public void canConvertTest(){
        assertTrue(sameTypeConverter.isConvertible(String.class,String.class));
        assertFalse(sameTypeConverter.isConvertible(String.class,Integer.class));
        assertFalse(sameTypeConverter.isConvertible(Integer.class,String.class));
    }
}
