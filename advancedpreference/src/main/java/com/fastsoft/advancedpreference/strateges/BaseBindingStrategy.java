package com.fastsoft.advancedpreference.strateges;

import com.fastsoft.advancedpreference.PreferenceHelper;
import com.fastsoft.advancedpreference.converters.PreferenceConverter;

import java.util.Set;

/**
 * Created by ura on 18-Aug-18.
 */

public abstract class BaseBindingStrategy<T> implements BindingStrategy<T> {
    private PreferenceHelper preferenceHelper;
    private Set<PreferenceConverter> preferenceConverters;

    public BaseBindingStrategy(PreferenceHelper preferenceHelper, Set<PreferenceConverter> preferenceConverters) {
        this.preferenceHelper = preferenceHelper;
        this.preferenceConverters = preferenceConverters;
    }

    public void setPreferenceHelper(PreferenceHelper preferenceHelper){
        this.preferenceHelper = preferenceHelper;
    }
    public void setPreferenceConverters(Set<PreferenceConverter> preferenceConverters){
        this.preferenceConverters = preferenceConverters;
    }
    public PreferenceHelper getPreferenceHelper(){
        return preferenceHelper;
    }
    public Set<PreferenceConverter> getPreferenceConverters(){
        return preferenceConverters;
    }
}

