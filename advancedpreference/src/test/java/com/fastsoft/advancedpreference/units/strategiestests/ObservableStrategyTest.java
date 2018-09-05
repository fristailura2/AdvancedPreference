package com.fastsoft.advancedpreference.units.strategiestests;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.ObservableStrategy;

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

import io.reactivex.Observable;
import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ura on 23-Aug-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class ObservableStrategyTest {
    @Mock
    PreferenceHelper preferenceHelper;
    @Mock
    PreferenceConverter preferenceConverter;
    @Mock
    CompitableStrategyTest.TestablePreferenceModel testablePreferenceModel;

    ObservableStrategy strategy;
    Set<PreferenceConverter> preferenceConverters=new TreeSet<>();
    MockitoSession session;

    int counter;
    @Before
    public void init(){
        counter=1;
        preferenceConverters.clear();
        session= Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.WARN)
                .startMocking();
        strategy=new ObservableStrategy(preferenceHelper,preferenceConverters);
    }
    @After
    public void finalization(){
        session.finishMocking();
    }

    @Test
    public void bindTest() throws NoSuchMethodException, NoSuchConverterException {
        Method testMethod=TestablePreferenceModel.class.getMethod("getPreference");
        PreferenceOperation testAnnotation=testMethod.getAnnotation(PreferenceOperation.class);


        preferenceConverters.add(preferenceConverter);
        doReturn(true).when(preferenceConverter).isConvertible(any(),any());
        when(preferenceConverter.convertFromSecondTo(any(String.class),any())).then((mock)->mock.getArgument(0));
        doReturn(String.class).when(preferenceHelper).getPreferenceType(testAnnotation.key());
        doAnswer((mock)->""+counter++).when(preferenceHelper).get(testAnnotation.key());

        when(preferenceHelper.getPreferenceObservable()).thenReturn(Observable.create((emitter)->{
            emitter.onNext(testAnnotation.key());
            emitter.onNext(testAnnotation.key());
            emitter.onNext(testAnnotation.key());
            emitter.onComplete();
        }));

        Observable<String> binder=strategy.bind(testMethod,null,testAnnotation);
        Single binder2=binder.reduce(new String(""),(s, s2) -> s+s2);
        assertEquals(binder2.blockingGet(),"246");

        verify(preferenceConverter,times(3)).convertFromSecondTo(any(String.class),eq(String.class));
        verify(preferenceHelper,atLeast(3)).get(eq(testAnnotation.key()));
    }

    interface TestablePreferenceModel extends PreferenceModel {
        @PreferenceOperation(key = "key")
        Observable<String> getPreference();
    }
}
