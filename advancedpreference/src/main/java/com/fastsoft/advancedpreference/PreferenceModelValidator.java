package com.fastsoft.advancedpreference;

import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.models.PreferenceModel;

import java.lang.reflect.Method;

/**
 * Created by ura on 20-Aug-18.
 */

public class PreferenceModelValidator {
    private Class<PreferenceModel> preferenceModelClass;

    public PreferenceModelValidator(Class<PreferenceModel> preferenceModelClass) {
        this.preferenceModelClass = preferenceModelClass;
    }

    public boolean validateMethods(){
        for (Method method:preferenceModelClass.getMethods()) {
            if(!validateMethod(method))
                return false;
        }
        return true;
    }
    public boolean validateMethod(Method method){
        if(method.getParameterTypes().length>1)
            return false;
        if(method.getAnnotation(PreferenceOperation.class)==null)
            return false;
        return method.getParameterTypes().length != 0 || !method.getReturnType().equals(Void.TYPE);
    }
    public boolean validate(){
        if(preferenceModelClass.isInterface()){
            return validateMethods();
        }else
            return false;

    }
}
