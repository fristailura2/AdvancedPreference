package com.fastsoft.advancedpreference.strateges;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;

import com.fastsoft.advancedpreference.converters.newBaseConverter;
import com.fastsoft.advancedpreference.exceptions.NoSuchConverterException;
import com.fastsoft.advancedpreference.utils.Objects;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by ura on 20-Aug-18.
 */

public class VoidStrategy extends BaseBindingStrategy<Void>{
    public VoidStrategy(PreferenceHelper preferenceHelper, Set<newBaseConverter> preferenceConverters) {
        super(preferenceHelper, preferenceConverters);
    }

    @Override
    public Void bindPrivate(Method method, Object arg, PreferenceOperation methodPrefAnnotation, Object defVal) throws NoSuchConverterException {
        Objects.throwIfNullParam(arg,"arg");

        Class<?> fromClass = method.getParameterTypes()[0];

        Class<?> convertToClass;
        if(getPreferenceHelper().getPreferenceType(methodPrefAnnotation.key())==null)
            convertToClass=arg.getClass();
        else
            convertToClass = getPreferenceHelper().getPreferenceType(methodPrefAnnotation.key());

        newBaseConverter rightConverter=null;
        for (newBaseConverter converter: getPreferenceConverters()) {
            if(converter.isConvertible(fromClass,convertToClass)) {
                rightConverter=converter;
                break;
            }
        }
        if (rightConverter == null)
            throw new NoSuchConverterException(String.format("no binder to convert from %s to %s",fromClass.getSimpleName(),convertToClass));

        getPreferenceHelper().put(rightConverter.convert(arg, convertToClass), methodPrefAnnotation.key());
        return null;
    }

    @Override
    public boolean canWorkWith(Class<?> arg) {
        return void.class.equals(arg);
    }
}
