package com.fastsoft.advancedpreference.converters;



import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * Created by ura on 23-Aug-18.
 */

public class CollectionsConverter extends newBaseConverter {
    @Override
    public boolean isConvertible(Class<?> first, Class<?> second) {
        return (Collection.class.isAssignableFrom(first)&&Collection.class.isAssignableFrom(second));
    }

    @Override
    Object convertPrivate(Object from, Class<?> toClass) {
        Collection res=null;
        try {
            if(toClass.isInterface())
                throw new IllegalArgumentException("class to convert should be not a interface");
            Constructor<?>[] constructors=toClass.getConstructors();
            for (Constructor constructor:constructors) {
                if(constructor.getTypeParameters().length==0) {
                    res = (Collection) toClass.newInstance();
                    break;
                }
            }
            if(res!=null)
                res.addAll((Collection) from);
            else
                throw new IllegalArgumentException("No empty constructor in given collection");
        }catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        return res;
    }
}
