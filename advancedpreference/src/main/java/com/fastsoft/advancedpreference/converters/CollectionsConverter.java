package com.fastsoft.advancedpreference.converters;

import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * Created by ura on 23-Aug-18.
 */

public class CollectionsConverter extends PreferenceConverter<Collection,Collection> {
    @Override
    protected Collection convertFromFirstToClass(Collection from, Class<? extends Collection> classToConvert) {
        return convert(from,classToConvert);
    }

    @Override
    protected Collection convertFromSecondToClass(Collection from, Class<? extends Collection> classToConvert) {
        return convert(from,classToConvert);
    }
    private Collection convert(Collection from, Class<? extends Collection> classToConvert){
        Collection res=null;
        try {
            if(classToConvert.isInterface())
                throw new IllegalArgumentException("class to convert should be not a interface");
            Constructor<?>[] constructors=classToConvert.getConstructors();
            for (Constructor constructor:constructors) {
                if(constructor.getTypeParameters().length==0) {
                    res = classToConvert.newInstance();
                    break;
                }
            }
            if(res!=null)
                res.addAll(from);
            else
                throw new IllegalArgumentException("No empty constructor in given collection");
        }catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        return res;
    }
    @Override
    public boolean isConvertible(Class<?> first, Class<?> second) {
        return Collection.class.isAssignableFrom(first)&&Collection.class.isAssignableFrom(second);
    }
}
