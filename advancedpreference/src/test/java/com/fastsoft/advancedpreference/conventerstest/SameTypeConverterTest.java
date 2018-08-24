package com.fastsoft.advancedpreference.conventerstest;

import com.fastsoft.advancedpreference.converters.SameTypeConverter;

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
        sameTypeConverter.convertFromFirstTo(new Integer(1),String.class);
    }
    @Test
    public void convertTest(){
        sameTypeConverter.convertFromFirstTo(1,Integer.class);
        sameTypeConverter.convertFromFirstTo(new TreeSet<>(),TreeSet.class);
        sameTypeConverter.convertFromFirstTo("",String.class);
        sameTypeConverter.convertFromFirstTo(1f,Float.class);
        sameTypeConverter.convertFromFirstTo(34d,Double.class);
    }
}
