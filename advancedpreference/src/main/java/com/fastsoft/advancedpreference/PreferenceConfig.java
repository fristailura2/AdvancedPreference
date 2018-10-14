package com.fastsoft.advancedpreference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.fastsoft.advancedpreference.converters.CollectionsConverter;
import com.fastsoft.advancedpreference.converters.EnumConverter;
import com.fastsoft.advancedpreference.converters.NumberConverter;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;
import com.fastsoft.advancedpreference.converters.PrimitiveConverter;
import com.fastsoft.advancedpreference.converters.SameTypeConverter;
import com.fastsoft.advancedpreference.converters.StringNumberConverter;
import com.fastsoft.advancedpreference.strateges.BindingStrategy;
import com.fastsoft.advancedpreference.strateges.CompletableStrategy;
import com.fastsoft.advancedpreference.strateges.GeneralStrategy;
import com.fastsoft.advancedpreference.strateges.ObservableStrategy;
import com.fastsoft.advancedpreference.strateges.VoidStrategy;
import com.fastsoft.advancedpreference.utils.Objects;

import java.util.Arrays;
import java.util.Collection;
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
        builder.bindingStrategies= provideDefaultStrategies(builder.sharedPreferences,builder.preferenceConverters,builder.preferenceHelper);
        return builder.build();
    }
    private static Set<BindingStrategy> provideDefaultStrategies(SharedPreferences sharedPreferences, Set<PreferenceConverter> preferenceConverters, PreferenceHelper preferenceHelper){
        return new TreeSet<>(Arrays.asList(
                new VoidStrategy(preferenceHelper, preferenceConverters),
                new GeneralStrategy(preferenceHelper, preferenceConverters),
                new CompletableStrategy(preferenceHelper, preferenceConverters),
                new ObservableStrategy(preferenceHelper, preferenceConverters)
        ));
    }
    private static Set<PreferenceConverter> provideDefaultBinders(){
        NumberConverter numberConverter=new NumberConverter();
        return new TreeSet<>(Arrays.asList(
                new SameTypeConverter(),
                new EnumConverter(),
                new CollectionsConverter(),
                numberConverter,
                new StringNumberConverter(),
                new PrimitiveConverter(numberConverter)
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

        public Builder replaceConverters(@NonNull Collection<? extends PreferenceConverter> converters){
            Objects.throwIfNullParam(converters,"converters");

            preferenceConverters.addAll(converters);
            return this;
        }
        public Builder putConverter(@NonNull PreferenceConverter converter){
            Objects.throwIfNullParam(converter,"converter");

            preferenceConverters.add(converter);
            return this;
        }
        public Builder putStrategy(@NonNull BindingStrategy strategy){
            Objects.throwIfNullParam(strategy,"strategy");

            bindingStrategies.add(strategy);
            return this;
        }
        public Builder setPreferences(@NonNull SharedPreferences sharedPreferences){
            Objects.throwIfNullParam(sharedPreferences,"sharedPreferences");

            this.sharedPreferences=sharedPreferences;
            return this;
        }
        public Builder removeBinder(@NonNull Class<? extends PreferenceConverter> converterClass){
            Objects.throwIfNullParam(converterClass,"converterClass");

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
            Objects.throwIfNull(preferenceHelper,"No PreferenceHelper");
            Objects.throwIfNull(preferenceHelper,"No SharedPreferences");
            if(bindingStrategies.isEmpty())
                throw new IllegalStateException("No BindingStrategies. Cant work without");
            if(preferenceConverters.isEmpty())
                throw new IllegalStateException("No PreferenceConverters. Cant work without");

            if(Objects.isNull(preferenceHelper))
                preferenceHelper =new PreferenceHelper(sharedPreferences);
            return new PreferenceConfig(preferenceConverters,sharedPreferences, preferenceHelper,bindingStrategies);
        }
    }
}
