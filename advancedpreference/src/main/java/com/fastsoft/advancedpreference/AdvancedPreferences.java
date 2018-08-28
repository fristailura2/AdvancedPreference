package com.fastsoft.advancedpreference;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.exceptions.IllegalMethodException;
import com.fastsoft.advancedpreference.exceptions.NoSuchBindingStrategyException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.BindingStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ura on 11-Aug-18.
 */

public class AdvancedPreferences implements InvocationHandler{
    private PreferenceConfig preferenceConfig;

    public AdvancedPreferences(@NonNull PreferenceConfig config){
        preferenceConfig=config;
    }

    public PreferenceConfig getPreferenceConfig() {
        return preferenceConfig;
    }

    public void setPreferenceConfig(PreferenceConfig preferenceConfig) {
        this.preferenceConfig = preferenceConfig;
    }

    public <T extends PreferenceModel>void validate(@NonNull Class<T> preferenceModelClass) throws IllegalMethodException,IllegalArgumentException {
        new PreferenceModelValidator(preferenceModelClass).validate();
    }
    public <T extends PreferenceModel>T getPreferenceModel(@NonNull Class<T> preferenceModelClass) throws IllegalMethodException,IllegalArgumentException {
        validate(preferenceModelClass);
        return (T) createPreferenceModelClass(preferenceModelClass);

    }
    private PreferenceModel createPreferenceModelClass(Class<? extends PreferenceModel> preferenceModelInterface)throws IllegalArgumentException{
        PreferenceModel preferenceModel=(PreferenceModel) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{preferenceModelInterface}, this);
        return preferenceModel;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PreferenceOperation methodPrefAnnot = method.getAnnotation(PreferenceOperation.class);
        Object res=null;
        if(methodPrefAnnot!=null) {
            for (BindingStrategy strategy : preferenceConfig.getBindingStrategies()) {
                if (strategy.canWorkWith(method.getReturnType()))
                    res= strategy.bind(method,args[0],methodPrefAnnot);
            }
        }
        if(res==null){
            throw new NoSuchBindingStrategyException("cant find BindingsStrategy for "+method.getReturnType());
        }
        return res;
    }

}
