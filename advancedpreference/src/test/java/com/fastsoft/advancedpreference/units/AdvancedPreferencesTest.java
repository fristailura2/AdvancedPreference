package com.fastsoft.advancedpreference.units;

import android.content.SharedPreferences;

import com.fastsoft.advancedpreference.AdvancedPreferences;
import com.fastsoft.advancedpreference.PreferenceConfig;
import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;

import com.fastsoft.advancedpreference.converters.SameTypeConverter;
import com.fastsoft.advancedpreference.converters.newBaseConverter;
import com.fastsoft.advancedpreference.exceptions.IllegalMethodException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.GeneralStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static org.junit.Assert.assertTrue;

/**
 * Created by ura on 12-Aug-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class AdvancedPreferencesTest {

    AdvancedPreferences advancedPreferences;
    PreferenceConfig config;
    @Mock
    SharedPreferences sharedPreferencesMock;
    @Mock
    PreferenceHelper preferenceHelperMock;
    Set<newBaseConverter> converters= new TreeSet<>(Arrays.asList(new SameTypeConverter()));
    MockitoSession sesion;
    @Before
    public void init(){
        sesion = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
        config=new PreferenceConfig.Builder()
                .replaceConverters(converters)
                .putStrategy(new GeneralStrategy(preferenceHelperMock,new TreeSet<>(converters)))
                .setPreferences(sharedPreferencesMock)
                .setPreferenceHelper(preferenceHelperMock)
                .build();
        advancedPreferences =new AdvancedPreferences(config);
    }
    @After
    public void finalization(){
        sesion.finishMocking();
    }

    @Test
    public void getPreferenceModelTest() throws IllegalMethodException {
        PreferenceModel preferenceModel= advancedPreferences.getPreferenceModel(TestablePreferenceModel.class);
        assertTrue(preferenceModel instanceof TestablePreferenceModel);
        assertTrue(Proxy.isProxyClass(preferenceModel.getClass()));
    }


}
interface TestablePreferenceModel extends PreferenceModel{
    @PreferenceOperation(key = "key")
    Observable<String> getPreference();
    @PreferenceOperation(key = "key")
    Completable setPreference(String val);
}