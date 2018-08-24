package com.fastsoft.advancedpreference;

import android.content.SharedPreferences;

import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.SameTypeConverter;
import com.fastsoft.advancedpreference.models.PreferenceModel;

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

    MockitoSession sesion;
    @Before
    public void init(){
        sesion = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
        config=new PreferenceConfig.Builder()
                .replaceBinder(new SameTypeConverter())
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
    public void getPreferenceModelTest(){
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