package com.fastsoft.advancedpreference.units.strategiestests;

/**
 * Created by ura on 23-Aug-18.
 */

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;

import com.fastsoft.advancedpreference.converters.newBaseConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.VoidStrategy;

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

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class VoidStrategyTest {
    @Mock
    PreferenceHelper preferenceHelper;
    @Mock
    newBaseConverter preferenceConverter;
    @Mock
    TestablePreferenceModel testablePreferenceModel;

    VoidStrategy strategy;
    Set<newBaseConverter> preferenceConverters=new TreeSet<>();
    MockitoSession session;

    @Before
    public void init(){
        preferenceConverters.clear();
        session= Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.WARN)
                .startMocking();
        strategy=new VoidStrategy(preferenceHelper,preferenceConverters);
    }
    @After
    public void finalization(){
        session.finishMocking();
    }
    @Test
    public void bindTest() throws NoSuchMethodException, NoSuchConverterException {
        Method testMethod=TestablePreferenceModel.class.getMethod("setPreference",String.class);
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);

        String testVal="testVal";
        doReturn(true).when(preferenceConverter).isConvertible(any(),any());
        when(preferenceConverter.convert(any(String.class),any())).then((mock)->mock.getArgument(0));
        preferenceConverters.add(preferenceConverter);

        assertNull(strategy.bind(testablePreferenceModel.getClass().getMethod("setPreference",String.class),testVal,testAnnotation, null));

        verify(preferenceHelper).put(eq(testVal),eq(testAnnotation.key()));
        verify(preferenceConverter).convert(any(String.class),any());
    }
    @Test(expected = NoSuchConverterException.class)
    public void noRightConverterTest() throws NoSuchMethodException, NoSuchConverterException {
        String testVal="testVal";

        Method testMethod=TestablePreferenceModel.class.getMethod("setPreference",String.class);
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);
        preferenceConverters.add(preferenceConverter);

        doReturn(false).when(preferenceConverter).isConvertible(any(),any());

        strategy.bind(testablePreferenceModel.getClass().getMethod("setPreference",String.class),testVal,testAnnotation, null);
    }
    interface TestablePreferenceModel extends PreferenceModel {
        @PreferenceOperation(key = "key")
        void setPreference(String val);
    }
}
