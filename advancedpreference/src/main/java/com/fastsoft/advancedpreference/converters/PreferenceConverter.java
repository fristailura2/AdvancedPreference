package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.utils.Objects;
import com.fastsoft.advancedpreference.utils.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ura on 11-Aug-18.
 */

public abstract class PreferenceConverter<T extends Object,V extends Object> implements Comparable<PreferenceConverter>{

    @Override
    public int hashCode() {
        Type[] params = ReflectionUtils.getParentGenericParams(this.getClass());
        int res=0;
        for (Type param:params) {
            res+=param.hashCode();
        }
        return res;
    }

    @Override
    public int compareTo(@NonNull PreferenceConverter o) {
        return this.hashCode()-o.hashCode();
    }
    public V convertFromFirstTo(@NonNull T from,@NonNull Class<? extends V> classToConvert)throws IllegalArgumentException{
        Objects.throwIfNullParam(from,"from");
        Objects.throwIfNullParam(from,"classToConvert");

        if(!isConvertible(from.getClass(),classToConvert))
            throw new IllegalArgumentException(String.format("can not convert from %s to %s",from.getClass().getSimpleName(),classToConvert.getSimpleName()));
        return convertFromFirstToClass(from,classToConvert);
    }
    public T convertFromSecondTo(@NonNull V from,@NonNull Class<? extends T> classToConvert)throws IllegalArgumentException{
        Objects.throwIfNullParam(from,"from");
        Objects.throwIfNullParam(from,"classToConvert");

        if(!isConvertible(from.getClass(),classToConvert))
            throw new IllegalArgumentException(String.format("can not convert from %s to %s",((Class)ReflectionUtils.getParentGenericParams(this.getClass())[1]).getSimpleName(),classToConvert.getSimpleName()));
        return convertFromSecondToClass(from,classToConvert);
    }


    protected abstract V convertFromFirstToClass(@NonNull T from,@NonNull Class<? extends V> classToConvert);
    protected abstract T convertFromSecondToClass(@NonNull V from,@NonNull Class<? extends T> classToConvert);

    public boolean isConvertible(@NonNull Class<?> first,@NonNull Class<?> second){
        return new Pair<Class<?>,Class<?>>(first,second).equals(getGenericClases());
    }
    private Pair<Class<?>,Class<?>> getGenericClases(){
        ParameterizedType superClass = (ParameterizedType)this.getClass().getGenericSuperclass();
        return new Pair(superClass.getActualTypeArguments()[0],superClass.getActualTypeArguments()[0]);
    }

    private class Pair<O,P>{
        private O first;
        private P second;

        public Pair(O first, P second){
            this.first = first;
            this.second=second;
        }

        public O getFirst() {
            return first;
        }

        public void setFirst(O first) {
            this.first = first;
        }

        public P getSecond() {
            return second;
        }

        public void setSecond(P second) {
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;

            if (!getFirst().equals(pair.getFirst())) return false;
            return getSecond().equals(pair.getSecond());

        }

        @Override
        public int hashCode() {
            int result = getFirst().hashCode();
            result = 31 * result + getSecond().hashCode();
            return result;
        }
    }
}
