package com.fastsoft.advancedpreference.units.conventerstest;

import com.fastsoft.advancedpreference.converters.EnumConverter;

import org.junit.Test;
import static org.junit.Assert.*;

public class EnumConverterTest {
    EnumConverter enumConverter=new EnumConverter();
    @Test
    public void simpleTest(){
        assertEquals(TestEnum.test2.name(),enumConverter.convert(TestEnum.test2,String.class));
        assertEquals(TestEnum.test1,enumConverter.convert(TestEnum.test1.name(),TestEnum.class));
    }
    @Test
    public void convertibleTest(){
        assertTrue(enumConverter.isConvertible(TestEnum.test1.getClass(),String.class));
        assertTrue(enumConverter.isConvertible(String.class,TestEnum.test1.getClass()));
        assertFalse(enumConverter.isConvertible(TestEnum.test1.getClass(),TestEnum.test1.getClass()));
        assertFalse(enumConverter.isConvertible(String.class,String.class));
        assertFalse(enumConverter.isConvertible(TestEnum.test1.getClass(),Integer.class));
    }
    enum TestEnum {test1,test2,test3,test4}
}
