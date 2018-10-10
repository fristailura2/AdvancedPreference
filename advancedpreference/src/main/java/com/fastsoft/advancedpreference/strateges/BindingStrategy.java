package com.fastsoft.advancedpreference.strateges;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by ura on 18-Aug-18.
 */

public interface BindingStrategy<T> {
    T bind(Method method, Object arg, PreferenceOperation methodPrefAnnotation, Object defVal) throws NoSuchConverterException;
    void setPreferenceHelper(PreferenceHelper preferenceHelper);
    void setPreferenceConverters(Set<PreferenceConverter> preferenceConverters);
    PreferenceHelper getPreferenceHelper();
    Set<PreferenceConverter> getPreferenceConverters();
    boolean canWorkWith(Class<?> arg);
}
