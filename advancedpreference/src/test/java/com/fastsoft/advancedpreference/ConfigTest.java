package com.fastsoft.advancedpreference;

import android.content.SharedPreferences;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigTest {
    @Mock
    SharedPreferences sharedPreferences;
    @Test
    public void defaultTest(){
        PreferenceConfig defaultConfig=PreferenceConfig.getDefault(sharedPreferences);
        assertFalse(defaultConfig.getBindingStrategies().isEmpty());
        assertFalse(defaultConfig.getBindingStrategies().isEmpty());
        assertFalse(defaultConfig.getPreferenceHelper()==null);
    }

}
