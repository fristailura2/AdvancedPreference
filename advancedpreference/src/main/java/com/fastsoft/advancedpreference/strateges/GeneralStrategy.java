package com.fastsoft.advancedpreference.strateges;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;

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
    public Object bind(@NonNull  Method method,@Nullable Object arg,@NonNull PreferenceOperation methodPrefAnnotation) throws NoSuchConverterException {
        Object prefVal = getPreferenceHelper().get(methodPrefAnnotation.key());
        Set<PreferenceConverter> binders = getPreferenceConverters();
        for (PreferenceConverter binder:binders) {
            if(binder.isConvertible(prefVal.getClass(),method.getReturnType())){
                return binder.convertFromFirstTo(prefVal,method.getReturnType());
            }
        }
        throw new NoSuchConverterException(String.format("no binder to convert from %s to %s",prefVal.getClass(),method.getReturnType()));
    }

    @Override
    public boolean canWorkWith(Class arg) {
        return !(arg.equals(Completable.class)||arg.equals(Observable.class)||arg.equals(Void.class));
    }
}