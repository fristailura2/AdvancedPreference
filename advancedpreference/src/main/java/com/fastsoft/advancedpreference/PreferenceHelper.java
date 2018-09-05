package com.fastsoft.advancedpreference;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by ura on 12-Aug-18.
 */

public class PreferenceHelper {
    private SharedPreferences sharedPreferences;

    public PreferenceHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
    public void setShaderPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void put(Object val, String key) throws IllegalArgumentException{
        SharedPreferences.Editor editor =sharedPreferences.edit();
        putInternal(val,key,editor);
        editor.apply();
    }
    private void putInternal(Object val, String key,SharedPreferences.Editor editor){
        Class<? extends Object> valClass = val.getClass();
        if(valClass.equals(String.class)){
            editor.putString(key, (String) val);
        }else if(valClass.equals(Integer.class)){
            editor.putInt(key, (Integer) val);
        }else if(valClass.equals(Long.class)){
            editor.putLong(key, (Long) val);
        }else if(Set.class.isAssignableFrom(val.getClass())){
            editor.putStringSet(key, (Set) val);
        }else if(valClass.equals(Boolean.class)){
            editor.putBoolean(key, (Boolean) val);
        }else if(valClass.equals(Float.class)){
            editor.putFloat(key,(Float) val);
        }else{
            throw new IllegalArgumentException("ca'nt put val because it is'nt supported type");
        }
    }
    public void putAll(Map<String,?> vals){
        SharedPreferences.Editor editor =sharedPreferences.edit();
        for (String key:vals.keySet()) {
            putInternal(vals.get(key),key,editor);
        }
        editor.apply();
    }
    public Object get(String key) throws IllegalArgumentException{
        Object res;
        Map<String,?> all=sharedPreferences.getAll();
        res=all.get(key);
        return res;
    }
    public Observable<String> getPreferenceObservable(){
        PreferenceOnSubscribe onSubscribe=new PreferenceOnSubscribe(sharedPreferences);
        return Observable.create(onSubscribe).doOnDispose(()->onSubscribe.close());
    }
    public Class<?> getPreferenceType(String key){
        return sharedPreferences.getAll().get(key).getClass();
    }
    private class PreferenceOnSubscribe implements ObservableOnSubscribe<String> {
        private SharedPreferences sharedPreferences;
        private ObservableEmitter<String> emitter;
        private SharedPreferences.OnSharedPreferenceChangeListener listener;



        public PreferenceOnSubscribe(SharedPreferences sharedPreferences1){
            this.sharedPreferences = sharedPreferences1;
            listener= (sharedPreferences, key) -> {
                if(emitter.isDisposed()){
                    close();
                }else{
                    emitter.onNext(key);
                }

            };
        }
        @Override
        public void subscribe(@NonNull ObservableEmitter<String> emitter) {
            try {
                this.emitter = emitter;
                sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            }catch (Exception e){
                emitter.onError(e);
            }
        }
        public void close(){
            try {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
                if(!emitter.isDisposed())
                    emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        }
    }
}
