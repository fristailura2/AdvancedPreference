package com.fastsoft.advancedpreference;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.exceptions.NoSuchBindingStrategy;
import com.fastsoft.advancedpreference.models.PreferenceModel;
import com.fastsoft.advancedpreference.strateges.BindingStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

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

    public <T extends PreferenceModel>boolean validate(@NonNull Class<T> preferenceModelClass){
        return new PreferenceModelValidator((Class<PreferenceModel>) preferenceModelClass).validate();
    }
    public <T extends PreferenceModel>T getPreferenceModel(@NonNull Class<T> preferenceModelClass){
        return (T) createPreferenceModelClass(preferenceModelClass);

    }
    private List<Method> getAllMethodsWithAnnotation(Class<? extends PreferenceModel> preferenceModelClass,Class<? extends Annotation> annotationClass){
        List<Method> res=new ArrayList<>();
        for (Method method:annotationClass.getMethods()) {
            if(method.getAnnotation(annotationClass)!=null)
                res.add(method);
        }
        return res;
    }
    private PreferenceModel createPreferenceModelClass(Class<? extends PreferenceModel> preferenceModelInterface)throws IllegalArgumentException{
        if(preferenceModelInterface.isInterface()) {
            PreferenceModel preferenceModel=(PreferenceModel) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{preferenceModelInterface}, this);
            return preferenceModel;
        }
        throw new IllegalArgumentException("preferenceModelInterface is'nt an interface");
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
            throw new NoSuchBindingStrategy("cant find BindingsStrategy for "+method.getReturnType());
        }
        return res;
    }

}
