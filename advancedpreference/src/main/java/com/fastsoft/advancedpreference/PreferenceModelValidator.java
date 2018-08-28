package com.fastsoft.advancedpreference;

import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.exceptions.IllegalMethodException;
import com.fastsoft.advancedpreference.models.PreferenceModel;

import java.lang.reflect.Method;

/**
 * Created by ura on 20-Aug-18.
 */

public class PreferenceModelValidator {
    private Class<? extends PreferenceModel> preferenceModelClass;

    public PreferenceModelValidator(Class<? extends PreferenceModel> preferenceModelClass) {
        this.preferenceModelClass = preferenceModelClass;
    }

    public void validateMethods() throws IllegalMethodException {
        for (Method method:preferenceModelClass.getMethods()) {
            validateMethod(method);
        }
    }
    public void validateMethod(Method method) throws IllegalMethodException {
        if(method.getParameterTypes().length>1)
            throw new IllegalMethodException(String.format("The method %s cant have more then one param.",method.getName()));
        if(method.getAnnotation(PreferenceOperation.class)==null)
            throw new IllegalMethodException(String.format("The method %s cant have more then one param.",method.getName()));
        if(!(method.getParameterTypes().length != 0 || !method.getReturnType().equals(Void.TYPE)))
            throw new IllegalMethodException(String.format("The void method %s cant be without param.",method.getName()));
    }
    public void validate() throws IllegalMethodException,IllegalArgumentException {
        if(preferenceModelClass.isInterface()){
            validateMethods();
        }else
            throw new IllegalArgumentException(String.format("%s is'nt an interface",preferenceModelClass.getSimpleName()));

    }
}
