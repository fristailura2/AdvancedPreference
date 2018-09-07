package com.fastsoft.advancedpreference.strateges;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Set;

import io.reactivex.Completable;

/**
 * Created by ura on 20-Aug-18.
 */

public class GeneralStrategy extends BaseBindingStrategy{
    public GeneralStrategy(PreferenceHelper preferenceHelper, Set<PreferenceConverter> set) {
        super(preferenceHelper, set);
    }

    @Override
    public Object bindPrivate(Method method,Object arg,PreferenceOperation methodPrefAnnotation) throws NoSuchConverterException {
        Objects.throwIfNotNullParam(arg,"arg");

        Object prefVal = getPreferenceHelper().get(methodPrefAnnotation.key());
        if(prefVal==null)
            return null;

        Set<PreferenceConverter> binders = getPreferenceConverters();

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

        for (PreferenceConverter binder:binders) {
            if(binder.isConvertible(fromClass,toClass)){
                return binder.convertFromFirstTo(prefVal,toClass);
            }
        }
        throw new NoSuchConverterException(String.format("no binder to convert from %s to %s",prefVal.getClass(),method.getReturnType()));
    }

    @Override
    public boolean canWorkWith(Class arg) {
        return !(arg.equals(Completable.class)||arg.equals(io.reactivex.Observable.class)||arg.equals(void.class));
    }
}
