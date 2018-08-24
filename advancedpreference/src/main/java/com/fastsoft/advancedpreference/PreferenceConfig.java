package com.fastsoft.advancedpreference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.converters.CollectionsConverter;
import com.fastsoft.advancedpreference.converters.NumberConverter;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.converters.SameTypeConverter;
import com.fastsoft.advancedpreference.converters.StringNumberConverter;
import com.fastsoft.advancedpreference.strateges.BindingStrategy;
import com.fastsoft.advancedpreference.strateges.CompletableStrategy;
import com.fastsoft.advancedpreference.strateges.GeneralStrategy;
import com.fastsoft.advancedpreference.strateges.ObservableStrategy;
import com.fastsoft.advancedpreference.strateges.VoidStrategy;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ura on 11-Aug-18.
 */

public class PreferenceConfig {
    private Set<PreferenceConverter> preferenceConverters;
    private PreferenceHelper preferenceHelper;
    private Set<BindingStrategy> bindingStrategies =new TreeSet<>();

    public static PreferenceConfig getDefault(SharedPreferences sharedPreferences){
        Builder builder=new Builder();
        builder.preferenceConverters =provideDefaultBinders();
        builder.preferenceHelper =new PreferenceHelper(sharedPreferences);
        builder.sharedPreferences=sharedPreferences;
        builder.bindingStrategies=provideDefaultStrategys(builder.sharedPreferences,builder.preferenceConverters,builder.preferenceHelper);
        return builder.build();
    }
    private static Set<BindingStrategy> provideDefaultStrategys(SharedPreferences sharedPreferences, Set<PreferenceConverter> preferenceConverters, PreferenceHelper preferenceHelper){
        return new TreeSet<>(Arrays.asList(
                new VoidStrategy(preferenceHelper, preferenceConverters),
                new GeneralStrategy(preferenceHelper, preferenceConverters),
                new CompletableStrategy(preferenceHelper, preferenceConverters),
                new ObservableStrategy(preferenceHelper, preferenceConverters)
        ));
    }
    private static Set<PreferenceConverter> provideDefaultBinders(){
        return new TreeSet<>(Arrays.asList(
                new CollectionsConverter(),
                new NumberConverter(),
                new StringNumberConverter(),
                new SameTypeConverter()
        ));
    }

    protected PreferenceConfig(Set<PreferenceConverter> preferenceConverters, SharedPreferences sharedPreferences, PreferenceHelper preferenceHelper, Set<BindingStrategy> bindingStrategies) {
        this.preferenceConverters = preferenceConverters;
        this.preferenceHelper = preferenceHelper;
        this.bindingStrategies=bindingStrategies;

    }

    public Set<BindingStrategy> getBindingStrategies() {
        return bindingStrategies;
    }

    public void setBindingStrategies(Set<BindingStrategy> bindingStrategies) {
        this.bindingStrategies = bindingStrategies;
    }

    public Set<PreferenceConverter> getPreferenceConverters() {
        return preferenceConverters;
    }

    public void setPreferenceConverters(Set<PreferenceConverter> preferenceConverters) {
        this.preferenceConverters = preferenceConverters;
    }


    public PreferenceHelper getPreferenceHelper() {
        return preferenceHelper;
    }

    public static class Builder{
        private Set<PreferenceConverter> preferenceConverters =new TreeSet<>();
        private Set<BindingStrategy> bindingStrategies =new TreeSet<>();
        private SharedPreferences sharedPreferences;
        private PreferenceHelper preferenceHelper;
        
        public Builder replaceBinder(@NonNull PreferenceConverter binder){
            preferenceConverters.add(binder);
            return this;
        }
        public Builder replaceStrategy(@NonNull BindingStrategy strategy){
            bindingStrategies.add(strategy);
            return this;
        }
        public Builder setPreferences(@NonNull SharedPreferences sharedPreferences){
            this.sharedPreferences=sharedPreferences;
            return this;
        }
        public Builder removeBinder(@NonNull Class<? extends PreferenceConverter> converterClass){
            for (PreferenceConverter preferenceConverter:preferenceConverters) {
                if(preferenceConverter.getClass().equals(converterClass)) {
                    preferenceConverters.remove(preferenceConverter);
                    break;
                }
            }
            return this;
        }

        public Builder setPreferenceHelper(@NonNull PreferenceHelper preferenceHelper){
            this.preferenceHelper = preferenceHelper;
            return this;
        }
        public  PreferenceConfig build(){
            if(preferenceHelper ==null)
                preferenceHelper =new PreferenceHelper(sharedPreferences);
            return new PreferenceConfig(preferenceConverters,sharedPreferences, preferenceHelper,bindingStrategies);
        }
    }
}
