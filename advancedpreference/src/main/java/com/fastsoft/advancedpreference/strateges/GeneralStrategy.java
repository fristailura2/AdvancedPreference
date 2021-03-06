package com.fastsoft.advancedpreference.strateges;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;

import com.fastsoft.advancedpreference.converters.newBaseConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.reflect.Method;
import java.util.Set;

import io.reactivex.Completable;

/**
 * Created by ura on 20-Aug-18.
 */

public class GeneralStrategy extends BaseBindingStrategy{
    public GeneralStrategy(PreferenceHelper preferenceHelper, Set<newBaseConverter> set) {
        super(preferenceHelper, set);
    }

    @Override
    public Object bindPrivate(Method method, Object arg, PreferenceOperation methodPrefAnnotation, Object defVal) throws NoSuchConverterException {
        Objects.throwIfNotNullParam(arg,"arg");

        Object prefVal = getPreferenceHelper().get(methodPrefAnnotation.key());
        if(prefVal==null)
            return defVal;

        Set<newBaseConverter> binders = getPreferenceConverters();

        Class<?> fromClass;
        Class<?> toClass=method.getReturnType();

        if(prefVal.getClass().isInterface()&&
                methodPrefAnnotation.concreteClass()!=Void.class&&
                method.getReturnType().isAssignableFrom(methodPrefAnnotation.concreteClass())){
            fromClass = methodPrefAnnotation.concreteClass();
        }else{
            if(prefVal.getClass().isInterface())
                throw new IllegalArgumentException("Interface as return type can not be used without concreteClass param in PreferenceOperation annotation");
            fromClass = prefVal.getClass();
        }

        for (newBaseConverter binder:binders) {
            if(binder.isConvertible(fromClass,toClass)){
                return binder.convert(prefVal,toClass);
            }
        }
        throw new NoSuchConverterException(String.format("no binder to convert from %s to %s",prefVal.getClass(),method.getReturnType()));
    }

    @Override
    public boolean canWorkWith(Class arg) {
        return !(arg.equals(Completable.class)||arg.equals(io.reactivex.Observable.class)||arg.equals(void.class));
    }
}
