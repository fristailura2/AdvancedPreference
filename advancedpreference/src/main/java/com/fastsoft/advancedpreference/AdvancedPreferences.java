package com.fastsoft.advancedpreference;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.anotations.DefVal;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.exceptions.IllegalMethodException;
import com.fastsoft.advancedpreference.exceptions.NoSuchBindingStrategyException;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.BindingStrategy;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by ura on 11-Aug-18.
 */

public class AdvancedPreferences implements InvocationHandler{
    private PreferenceConfig preferenceConfig;

    public AdvancedPreferences(@NonNull PreferenceConfig config){
        Objects.throwIfNullParam(config,"config");

        preferenceConfig=config;
    }

    public PreferenceConfig getPreferenceConfig() {
        return preferenceConfig;
    }

    public void setPreferenceConfig(PreferenceConfig preferenceConfig) {
        Objects.throwIfNullParam(preferenceConfig,"preferenceConfig");

        this.preferenceConfig = preferenceConfig;
    }

    public <T extends PreferenceModel>void validate(@NonNull Class<T> preferenceModelClass) throws IllegalMethodException,IllegalArgumentException {
        Objects.throwIfNullParam(preferenceModelClass,"preferenceModelClass");

        new PreferenceModelValidator(preferenceModelClass).validate();
    }
    public <T extends PreferenceModel>T getPreferenceModel(@NonNull Class<T> preferenceModelClass) throws IllegalMethodException,IllegalArgumentException {
        Objects.throwIfNullParam(preferenceModelClass,"preferenceModelClass");

        validate(preferenceModelClass);
        return (T) createPreferenceModelClass(preferenceModelClass);

    }
    private PreferenceModel createPreferenceModelClass(Class<? extends PreferenceModel> preferenceModelInterface)throws IllegalArgumentException{
        PreferenceModel preferenceModel=(PreferenceModel) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{preferenceModelInterface}, this);
        return preferenceModel;
    }

    private Object getDefVal(Method method, Object[] args){
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        for (Annotation[] paramAnnotations :paramsAnnotations) {
            for (Annotation annotation : Arrays.asList(paramAnnotations)) {
                if(annotation.annotationType().equals(DefVal.class))
                    return args[Arrays.asList(paramsAnnotations).indexOf(paramAnnotations)];
            }
        }
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PreferenceOperation methodPrefAnnot = method.getAnnotation(PreferenceOperation.class);
        Object res=null;
        Objects.throwIfNull(methodPrefAnnot,"any public or package private method should be annotated with "+PreferenceOperation.class.getSimpleName());

        for (BindingStrategy strategy : preferenceConfig.getBindingStrategies()) {
            if (strategy.canWorkWith(method.getReturnType())) {
                boolean checkRes=true;
                if(method.getParameterTypes().length!=0) {
                    checkRes=false;
                    for (Annotation paramAnnotation : method.getParameterAnnotations()[0]) {
                        if (paramAnnotation.annotationType().equals(DefVal.class)) {
                            checkRes = true;
                            break;
                        }
                    }
                }

                res = strategy.bind(method, (checkRes ? null : args[0]), methodPrefAnnot,getDefVal(method,args));
                return res;
            }
        }
        throw new NoSuchBindingStrategyException("cant find BindingsStrategy for "+method.getReturnType());
    }

}
