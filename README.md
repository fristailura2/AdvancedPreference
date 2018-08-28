# AdvancedPreference

Library for simplify android Preference usage.

## Getting Started

### Setting up the dependency

To include AdvancedPreference to your project you can add Gradle compile dependency:
 
```
implementation "com.fastsoft.advancedpreference:advancedpreference:x.x.x"
```

where x.x.x version which you can find <a href='https://bintray.com/fristailura/AdvancedPreference/com.fastsoft.advancedpreference'>here</a>. AdvancedPreference situated in JCentral repository check if you are using it in your project before add it to your dependency.

### Hello World
To use this library you have to make 2 steps:

- Make interface wich extends from PreferenceModel and declarete set,get methods you want to use with PreferenceOperation anotation(key shoud be the same witch is in a SharedPreferences):
    
```Java
    interface ExamplePreferenceModel extends PreferenceModel {
        @PreferenceOperation(key = "testKey")
        void setPreference(String testVal);
    } 
```

- Create AdvancedPreferences instance:

```Java
    public ExamplePreferenceModel create(Context context){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        AdvancedPreferences advancedPreferences=new AdvancedPreferences(PreferenceConfig.getDefault(sharedPreferences));
        return advancedPreferences.getPreferenceModel(ExamplePreferenceModel.class);
    } 
```
