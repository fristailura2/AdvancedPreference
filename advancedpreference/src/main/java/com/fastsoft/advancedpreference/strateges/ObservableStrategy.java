package com.fastsoft.advancedpreference.strateges;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.converters.newBaseConverter;
import com.fastsoft.advancedpreference.utils.ReflectionUtils;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;

import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by ura on 18-Aug-18.
 */

public class ObservableStrategy extends BaseBindingStrategy<Observable>{

    public ObservableStrategy(PreferenceHelper preferenceHelper, Set<newBaseConverter> preferenceConverters) {
        super(preferenceHelper, preferenceConverters);
    }


    @Override
    public Observable bindPrivate(Method method, Object arg, PreferenceOperation methodPrefAnnotation, Object defVal) {
        Objects.throwIfNotNullParam(arg,"arg");

        Observable res=getPreferenceHelper().getPreferenceObservable()
                .filter((key)->getPreferenceHelper().get(methodPrefAnnotation.key())!=null||defVal!=null)
                .map((key)->{
                    Object val = getPreferenceHelper().get(methodPrefAnnotation.key());
                    if(val==null)
                        val=defVal;
                    return val;
                })
                .map((convertedVal)->{

                    Class<?> convertFromClass;
                    Class<?> convertToClass=null;

                    Type returnType = ReflectionUtils.getMethodGenericReturnType(method,0);
                    if (returnType instanceof Class)
                        convertToClass= (Class<?>) returnType;
                    if (returnType instanceof ParameterizedType)
                        convertToClass= (Class<?>) ((ParameterizedType) returnType).getRawType();

                    if(convertedVal.getClass().isInterface()&&
                            methodPrefAnnotation.concreteClass()!=Void.class&&
                            method.getReturnType().isAssignableFrom(methodPrefAnnotation.concreteClass())){
                        convertFromClass = methodPrefAnnotation.concreteClass();
                    }else{
                        if(convertedVal.getClass().isInterface())
                            throw new IllegalArgumentException("Interface as return type can not be used without concreteClass param in PreferenceOperation annotation");
                        convertFromClass = convertedVal.getClass();
                    }

                    for (newBaseConverter preferenceBinder: getPreferenceConverters()) {
                        if(preferenceBinder.isConvertible(convertFromClass,convertToClass)) {
                            return preferenceBinder.convert(convertedVal,convertFromClass);
                        }
                    }
                    throw new NoSuchConverterException(String.format("no binder to convert from %s to %s", ReflectionUtils.getMethodGenericReturnClass(method,0).getSimpleName(),convertFromClass.getSimpleName()));
                });
        return res;

    }


    @Override
    public boolean canWorkWith(Class<?> arg) {
        return arg.equals(Observable.class);
    }
}
