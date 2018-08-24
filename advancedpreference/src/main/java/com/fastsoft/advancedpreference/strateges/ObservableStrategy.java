package com.fastsoft.advancedpreference.strateges;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.ReflectionUtils;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by ura on 18-Aug-18.
 */

public class ObservableStrategy extends BaseBindingStrategy<Observable>{

    public ObservableStrategy(PreferenceHelper preferenceHelper, Set<PreferenceConverter> preferenceConverters) {
        super(preferenceHelper, preferenceConverters);
    }


    @Override
    public Observable bind(Method method, Object arg, PreferenceOperation methodPrefAnnotation) {
        Observable res=getPreferenceHelper().getPreferenceObservable()
                .map((key)-> getPreferenceHelper().get(methodPrefAnnotation.key()))
                .map((convertedVal)->{
                    Class<?> convertFromClass = getPreferenceHelper().getPreferenceType(methodPrefAnnotation.key());
                    for (PreferenceConverter preferenceBinder: getPreferenceConverters()) {
                        if(preferenceBinder.isConvertible(convertFromClass,(Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0])) {
                            return preferenceBinder.convertFromSecondTo(getPreferenceHelper().get(methodPrefAnnotation.key()),convertFromClass);
                        }
                    }
                    throw new NoSuchConverterException(String.format("no binder to convert from %s to %s", ReflectionUtils.getMethodGenericReturnType(method,0).getSimpleName(),convertFromClass.getSimpleName()));
                });
        return res;

    }


    @Override
    public boolean canWorkWith(Class<?> arg) {
        return arg.equals(java.util.Observable.class);
    }
}
