package com.fastsoft.advancedpreference.strateges;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.ReflectionUtils;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by ura on 18-Aug-18.
 */

public abstract class BaseBindingStrategy<T> implements BindingStrategy<T>,Comparable {
    private PreferenceHelper preferenceHelper;
    private Set<PreferenceConverter> preferenceConverters;

    protected abstract T bindPrivate(Method method, Object arg, PreferenceOperation methodPrefAnnotation) throws NoSuchConverterException;

    @Override
    public T bind(Method method, Object arg, PreferenceOperation methodPrefAnnotation) throws NoSuchConverterException {
        Objects.throwIfNullParam(method,"method");
        Objects.throwIfNullParam(method,"methodPrefAnnotation");
        return bindPrivate(method,arg,methodPrefAnnotation);
    }

    public BaseBindingStrategy(PreferenceHelper preferenceHelper, Set<PreferenceConverter> preferenceConverters) {
        this.preferenceHelper = preferenceHelper;
        this.preferenceConverters = preferenceConverters;
    }

    public void setPreferenceHelper(PreferenceHelper preferenceHelper){
        this.preferenceHelper = preferenceHelper;
    }
    public void setPreferenceConverters(Set<PreferenceConverter> preferenceConverters){
        this.preferenceConverters = preferenceConverters;
    }
    public PreferenceHelper getPreferenceHelper(){
        return preferenceHelper;
    }
    public Set<PreferenceConverter> getPreferenceConverters(){
        return preferenceConverters;
    }

    @Override
    public boolean equals(Object o) {
       if(this.getClass().equals(o))
           return true;
       return false;
    }

    @Override
    public int hashCode() {
        Type[] params = ReflectionUtils.getParentGenericParams(this.getClass());
        int res=0;
        for (Type param:params) {
            res+=param.hashCode();
        }
        return res;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.hashCode()-o.hashCode();
    }
}

