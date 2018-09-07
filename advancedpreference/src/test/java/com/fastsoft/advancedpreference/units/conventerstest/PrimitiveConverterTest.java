package com.fastsoft.advancedpreference.units.conventerstest;

import com.fastsoft.advancedpreference.converters.NumberConverter;
import com.fastsoft.advancedpreference.converters.PrimitiveConverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PrimitiveConverterTest {
    NumberConverter numberConverter;
    PrimitiveConverter primitiveConverter;
    @Before
    public void init(){
        numberConverter=new NumberConverter();
        primitiveConverter=new PrimitiveConverter(numberConverter);
    }
    @Test
    public void legalConvert(){
        int testVal=1;
        Object result = primitiveConverter.convertFromFirstTo(testVal, long.class);
        result.equals(testVal);

        byte testVal2=1;
        result = primitiveConverter.convertFromFirstTo(testVal2, int.class);
        result.equals(testVal2);

        double testVal3=1d;
        result = primitiveConverter.convertFromFirstTo(testVal3, short.class);
        result.equals(testVal3);
    }
    @Test
    public void convertibleTest(){
        Assert.assertTrue(primitiveConverter.isConvertible(int.class,long.class));
        Assert.assertTrue(primitiveConverter.isConvertible(int.class,float.class));
        Assert.assertTrue(primitiveConverter.isConvertible(int.class,short.class));
        Assert.assertTrue(primitiveConverter.isConvertible(int.class,byte.class));
        Assert.assertTrue(primitiveConverter.isConvertible(int.class,double.class));
        Assert.assertFalse(primitiveConverter.isConvertible(int.class,int.class));
        Assert.assertFalse(primitiveConverter.isConvertible(char.class,int.class));
        Assert.assertFalse(primitiveConverter.isConvertible(int.class,char.class));
        Assert.assertFalse(primitiveConverter.isConvertible(void.class,char.class));
        Assert.assertFalse(primitiveConverter.isConvertible(Long.class,Integer.class));
    }
}
