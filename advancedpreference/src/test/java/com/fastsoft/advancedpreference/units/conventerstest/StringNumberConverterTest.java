package com.fastsoft.advancedpreference.units.conventerstest;

import com.fastsoft.advancedpreference.converters.NumberConverter;
import com.fastsoft.advancedpreference.converters.StringNumberConverter;

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
public class StringNumberConverterTest {
    StringNumberConverter stringNumberBinder=new StringNumberConverter();
    private final String TEST_VAL="1353";
    private final String TEST_VAL_WITH_ZERO=TEST_VAL+".0";
    private final Integer SPECIMEN_VAL=1353;

    @Test
    public void sameConvertTest() throws IllegalAccessException {
        for (Method method:NumberConverter.class.getMethods()) {
            if(method.getName().equals("convertFromFirstTo")) {
                try {
                    method.invoke(stringNumberBinder, "100", String.class);
                }catch (InvocationTargetException e){
                    assertEquals(e.getCause().getClass(),IllegalArgumentException.class);
                }

            }
        }
    }
    @Test
    public void convertFromStringTest(){
        assertEquals(stringNumberBinder.convertFromFirstToClass(TEST_VAL,Integer.class),SPECIMEN_VAL);
        assertEquals(stringNumberBinder.convertFromFirstToClass(TEST_VAL,Float.class),Float.valueOf(SPECIMEN_VAL));
        assertEquals(stringNumberBinder.convertFromFirstToClass(TEST_VAL,Double.class),Double.valueOf(SPECIMEN_VAL));
        assertEquals(stringNumberBinder.convertFromFirstToClass(TEST_VAL,BigInteger.class),BigInteger.valueOf(SPECIMEN_VAL));
        assertEquals(stringNumberBinder.convertFromFirstToClass(TEST_VAL,BigDecimal.class),BigDecimal.valueOf(SPECIMEN_VAL));
    }
    @Test
    public void convertToStringTest(){
        assertEquals(stringNumberBinder.convertFromSecondToClass(SPECIMEN_VAL,String.class),TEST_VAL);
        assertEquals(stringNumberBinder.convertFromSecondToClass((float)SPECIMEN_VAL,String.class),TEST_VAL_WITH_ZERO);
        assertEquals(stringNumberBinder.convertFromSecondToClass((double)SPECIMEN_VAL,String.class),TEST_VAL_WITH_ZERO);
        assertEquals(stringNumberBinder.convertFromSecondToClass(BigInteger.valueOf(SPECIMEN_VAL),String.class),TEST_VAL);
        assertEquals(stringNumberBinder.convertFromSecondToClass(BigDecimal.valueOf(SPECIMEN_VAL),String.class),TEST_VAL);
    }
}
