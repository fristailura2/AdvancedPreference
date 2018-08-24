package com.fastsoft.advancedpreference.strategiestests;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.CompletableStrategy;

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

import io.reactivex.Completable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by ura on 23-Aug-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompitableStrategyTest {
    @Mock
    PreferenceHelper preferenceHelper;
    @Mock
    PreferenceConverter preferenceConverter;
    @Mock
    TestablePreferenceModel testablePreferenceModel;

    CompletableStrategy strategy;
    Set<PreferenceConverter> preferenceConverters=new TreeSet<>();
    MockitoSession session;
    @Before
    public void init(){
        preferenceConverters.clear();
        session=Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
        strategy=new CompletableStrategy(preferenceHelper,preferenceConverters);
    }
    @After
    public void finalization(){
        session.finishMocking();
    }
    @Test
    public void bindTest() throws NoSuchMethodException {
        Method testMethod=TestablePreferenceModel.class.getMethod("setPreference",String.class);
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);
        String testVal="val";

        preferenceConverters.add(preferenceConverter);
        doReturn(true).when(preferenceConverter).isConvertible(any(),any());
        doReturn(testVal).when(preferenceConverter).convertFromFirstTo(any(),any());
        doReturn(testVal.getClass()).when(preferenceHelper).getPreferenceType(testAnnotation.key());

        Completable binder=strategy.bind(testMethod,testVal,testAnnotation);
        Throwable error = binder.blockingGet();
        assertNull(error);

        verify(preferenceConverter).convertFromFirstTo(testVal,String.class);
        verify(preferenceHelper).put(testVal,testAnnotation.key());
    }
    @Test
    public void noRightConverterTest() throws NoSuchMethodException {
        Method testMethod=TestablePreferenceModel.class.getMethod("setPreference",String.class);
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);
        String testVal="val";

        preferenceConverters.add(preferenceConverter);
        doReturn(false).when(preferenceConverter).isConvertible(any(),any());
        doReturn(testVal.getClass()).when(preferenceHelper).getPreferenceType(testAnnotation.key());

        Completable binder=strategy.bind(testMethod,testVal,testAnnotation);
        Throwable error = binder.blockingGet();
        assertEquals(error.getClass(), NoSuchConverterException.class);

    }
    interface TestablePreferenceModel extends PreferenceModel {
        @PreferenceOperation(key = "key")
        Completable setPreference(String val);
    }


}
