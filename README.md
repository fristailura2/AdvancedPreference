# AdvancedPreference
Library for simplify android Preference usage.
## Getting Started
### Hello World
To use this library you have to make 2 steps:

- Make interface wich extends from PreferenceModel and declarete set,get methods you want to use with PreferenceOperation anotation(key shoud be the same witch is in a SharedPreferences):
    
```Java
    interface ExamplePreferenceModel extends PreferenceModel {
        @PreferenceOperation(key = "testKey")
        void setPreference(String testVal);
    } 
```

- Create AdvancedPreferences instance

```Java
    public AdvancedPreferences create(Context context){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        AdvancedPreferences advancedPreferences=new AdvancedPreferences(PreferenceConfig.getDefault(sharedPreferences));
        ExamplePreferenceModel preferenceModel=advancedPreferences.getPreferenceModel(ExamplePreferenceModel.class);
        preferenceModel.setPreference("testVal");
    } 
```
