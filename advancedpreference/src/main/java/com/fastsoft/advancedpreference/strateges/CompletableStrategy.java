package com.fastsoft.advancedpreference.strateges;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.reflect.Method;
import java.util.Set;

import io.reactivex.Completable;

/**
 * Created by ura on 18-Aug-18.
 */

public class CompletableStrategy extends BaseBindingStrategy<Completable>{


    public CompletableStrategy(@NonNull PreferenceHelper preferenceHelper,@NonNull Set<PreferenceConverter> preferenceConverters) {
        super(preferenceHelper, preferenceConverters);
    }

    @Override
    protected Completable bindPrivate(Method method, Object arg, PreferenceOperation methodPrefAnnotation) {
        Objects.throwIfNullParam(arg,"methodPrefAnnotation");

        Class<?> convertToClass;
        if(getPreferenceHelper().getPreferenceType(methodPrefAnnotation.key())==null)
            convertToClass=arg.getClass();
        else
            convertToClass = getPreferenceHelper().getPreferenceType(methodPrefAnnotation.key());

        Completable res=Completable.fromAction(()->{
            PreferenceConverter rightConverter=null;
            for (PreferenceConverter preferenceConverter:getPreferenceConverters()) {
                if(preferenceConverter.isConvertible(arg.getClass(),convertToClass)){
                    rightConverter=preferenceConverter;
                    break;
                }
            }
            if(rightConverter==null)
                throw new NoSuchConverterException(String.format("can not find converter to convert from %s to %s",arg.getClass().getSimpleName(),convertToClass.getSimpleName()));
                getPreferenceHelper().put(rightConverter.convertFromFirstTo(arg,convertToClass),methodPrefAnnotation.key());
        });
        return res;
    }

    @Override
    public boolean canWorkWith(Class<?> arg) {
        return arg.equals(Completable.class);
    }
}
