package com.fastsoft.advancedpreference.integrationtests;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
    private void fireChangeEvent(Map<String,Object> toAdd,Set<String> toRemove){
        Set<String> fullChanged=new TreeSet(toAdd.keySet());
        fullChanged.addAll(toRemove);

        for (String key:fullChanged) {
            for (OnSharedPreferenceChangeListener listener:onSharedPreferenceChangeListeners) {
                listener.onSharedPreferenceChanged(this,key);
            }
        }
    }
    class MapEditor implements Editor{
        private Map<String,Object> toAddPreferenceStorage;
        private Set<String> toRemoveKeys;
        
        public MapEditor(){
            toAddPreferenceStorage =new TreeMap<>();
            toRemoveKeys=new TreeSet<>();
        }
        @Override
        public Editor putString(String key, @Nullable String value) {
            toAddPreferenceStorage.put(key,value);
            return this;
        }
        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            toAddPreferenceStorage.put(key,values);
            return this;
        }
        @Override
        public Editor putInt(String key, int value) {
            toAddPreferenceStorage.put(key,value);
            return this;
        }
        @Override
        public Editor putLong(String key, long value) {
            toAddPreferenceStorage.put(key,value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            toAddPreferenceStorage.put(key,value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            toAddPreferenceStorage.put(key,value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            toRemoveKeys.add(key);
            return this;
        }

        @Override
        public Editor clear() {
            toRemoveKeys.addAll(preferenceStorage.keySet());
            return this;
        }

        @Override
        public boolean commit() {
            replaceAll();
            SharedPreferenceMap.this.preferenceStorage.putAll(toAddPreferenceStorage);

            fireChangeEvent(toAddPreferenceStorage,toRemoveKeys);
            return true;
        }

        @Override
        public void apply() {
            replaceAll();
            SharedPreferenceMap.this.preferenceStorage.putAll(toAddPreferenceStorage);

            fireChangeEvent(toAddPreferenceStorage,toRemoveKeys);
        }
        private void replaceAll(){
            for (String key:toRemoveKeys) {
                preferenceStorage.remove(key);
            }
        }
    } 
}
