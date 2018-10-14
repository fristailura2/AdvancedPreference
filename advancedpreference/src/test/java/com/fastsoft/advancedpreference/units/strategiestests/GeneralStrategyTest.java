package com.fastsoft.advancedpreference.units.strategiestests;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;

import com.fastsoft.advancedpreference.converters.newBaseConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.GeneralStrategy;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ura on 23-Aug-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class GeneralStrategyTest {
    @Mock
    PreferenceHelper preferenceHelper;
    @Mock
    newBaseConverter preferenceConverter;
    @Mock
    TestablePreferenceModel testablePreferenceModel;

    GeneralStrategy strategy;
    Set<newBaseConverter> preferenceConverters=new TreeSet<>();
    MockitoSession session;

    @Before
    public void init(){
        preferenceConverters.clear();
        session= Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.WARN)
                .startMocking();
        strategy=new GeneralStrategy(preferenceHelper,preferenceConverters);
    }
    @After
    public void finalization(){
        session.finishMocking();
    }
    @Test
    public void bindTest() throws NoSuchMethodException, NoSuchConverterException {
        Method testMethod=TestablePreferenceModel.class.getMethod("getPreference");
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);

        String testVal="testVal";

        doReturn(true).when(preferenceConverter).isConvertible(any(),any());
        doReturn(testVal).when(preferenceHelper).get(testAnnotation.key());
        when(preferenceConverter.convert(any(String.class),any())).then((mock)->mock.getArgument(0));
        preferenceConverters.add(preferenceConverter);

        Assert.assertEquals(strategy.bind(testMethod,null,testAnnotation, null),testVal);

        verify(preferenceHelper).get(eq(testAnnotation.key()));
        verify(preferenceConverter).convert(any(String.class),any());
    }
    @Test(expected = NoSuchConverterException.class)
    public void noRightConverterTest() throws NoSuchMethodException, NoSuchConverterException {
        Method testMethod=TestablePreferenceModel.class.getMethod("getPreference");
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);

        String testVal="testVal";

        doReturn(false).when(preferenceConverter).isConvertible(any(),any());
        doReturn(testVal).when(preferenceHelper).get(testAnnotation.key());
        preferenceConverters.add(preferenceConverter);

        strategy.bind(testablePreferenceModel.getClass().getMethod("getPreference"),null,testAnnotation, null);
    }
    interface TestablePreferenceModel extends PreferenceModel {
        @PreferenceOperation(key = "key")
        String getPreference();
    }
}
