package com.fastsoft.advancedpreference.converters;

import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.ReflectionUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by ura on 11-Aug-18.
 */

public abstract class PreferenceConverter<T extends Object,V extends Object> implements Comparable<PreferenceConverter>{

    @Override
    public int compareTo(@NonNull PreferenceConverter other) {
        return 0;
    }
    public V convertFromFirstTo(@NonNull T from,@NonNull Class<? extends V> classToConvert)throws IllegalArgumentException{
        if(!isConvertible(from.getClass(),classToConvert))
            throw new IllegalArgumentException(String.format("can not convert from %s to %s",from.getClass().getSimpleName(),classToConvert.getSimpleName()));
        return convertFromFirstToClass(from,classToConvert);
    }
    public T convertFromSecondTo(@NonNull V from,@NonNull Class<? extends T> classToConvert)throws IllegalArgumentException{
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
        private O fist;
        private P second;

        public Pair(O fist,P second){
            this.fist=fist;
            this.second=second;
        }

        public O getFist() {
            return fist;
        }

        public void setFist(O fist) {
            this.fist = fist;
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

            if (!getFist().equals(pair.getFist())) return false;
            return getSecond().equals(pair.getSecond());

        }

        @Override
        public int hashCode() {
            int result = getFist().hashCode();
            result = 31 * result + getSecond().hashCode();
            return result;
        }
    }
}
