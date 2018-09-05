package com.fastsoft.advancedpreference.integrationtests;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SharedPreferenceMap implements SharedPreferences{
    private Map<String,Object> preferenceStorage=new TreeMap<>();
    private List<OnSharedPreferenceChangeListener> onSharedPreferenceChangeListeners=new LinkedList<>();
    
    @Override
    public Map<String, Object> getAll() {
        return preferenceStorage;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return preferenceStorage.containsKey(key)? (String) preferenceStorage.get(key) :defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return contains(key)? (Set<String>) preferenceStorage.get(key) :defValues;
    }

    @Override
    public int getInt(String key, int defValue) {
        return contains(key)?((Integer)preferenceStorage.get(key)) :defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        return contains(key)?((Long)preferenceStorage.get(key)):defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        return contains(key)?((Float)preferenceStorage.get(key)):defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return contains(key)?((Boolean)preferenceStorage.get(key)) :defValue;
    }

    @Override
    public boolean contains(String key) {
        return preferenceStorage.containsKey(key);
    }

    @Override
    public Editor edit() {
        return new MapEditor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        onSharedPreferenceChangeListeners.add(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        onSharedPreferenceChangeListeners.remove(listener);
    }
    private void fireChangeEvent(Map<String,Object> preferenceStorage){
        for (String key:preferenceStorage.keySet()) {
            for (OnSharedPreferenceChangeListener listener:onSharedPreferenceChangeListeners) {
                listener.onSharedPreferenceChanged(this,key);
            }
        }

    }
    class MapEditor implements Editor{
        private Map<String,Object> preferenceStorage;
        
        public MapEditor(){
            preferenceStorage=new TreeMap<>();
        }
        @Override
        public Editor putString(String key, @Nullable String value) {
            preferenceStorage.put(key,value);
            return this;
        }
        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            preferenceStorage.put(key,values);
            return this;
        }
        @Override
        public Editor putInt(String key, int value) {
            preferenceStorage.put(key,value);
            return this;
        }
        @Override
        public Editor putLong(String key, long value) {
            preferenceStorage.put(key,value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            preferenceStorage.put(key,value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            preferenceStorage.put(key,value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            preferenceStorage.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            preferenceStorage.clear();
            return this;
        }

        @Override
        public boolean commit() {
            SharedPreferenceMap.this.preferenceStorage.putAll(preferenceStorage);
            fireChangeEvent(preferenceStorage);
            return true;
        }

        @Override
        public void apply() {
            fireChangeEvent(preferenceStorage);
            SharedPreferenceMap.this.preferenceStorage.putAll(preferenceStorage);
        }
    } 
}
