package com.fastsoft.advancedpreference.integrationtests;

import android.content.Context;

import android.content.SharedPreferences;

import com.fastsoft.advancedpreference.AdvancedPreferences;
import com.fastsoft.advancedpreference.PreferenceConfig;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.exceptions.IllegalMethodException;
import com.fastsoft.advancedpreference.models.PreferenceModel;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Set;
import java.util.TreeSet;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.internal.operators.completable.CompletableConcatIterable;

@RunWith(JUnit4.class)
public class SimpleCaseTest {
    SharedPreferences sharedPreferences;
    AdvancedPreferences advancedPreferences;

    final static String TEST_PREFIX="simple_test_";
    final static String SOME_BOOL_KEY="someBool";
    final static String SOME_STRING_KEY="someString";
    final static String SOME_STRING_SET_KEY="someStringSet";
    final static String SOME_INT_KEY="someInt";

    final static Boolean SOME_BOOL_VAL=false;
    final static String SOME_STRING_VAL="someString";
    final static Set<String> SOME_STRING_SET_VAL=new TreeSet<String>(Arrays.asList("test1","test2","test3"));
    final static Integer SOME_INT_VAL=1000;
    @Before
    public void init() {
        sharedPreferences=new SharedPreferenceMap();

        sharedPreferences.edit()
                .putBoolean(getTestKey(SOME_BOOL_KEY),SOME_BOOL_VAL)
                .putString(getTestKey(SOME_STRING_KEY),SOME_STRING_VAL)
                .putInt(getTestKey(SOME_INT_KEY),SOME_INT_VAL)
                .putStringSet(getTestKey(SOME_STRING_SET_KEY),SOME_STRING_SET_VAL)
                .commit();
        advancedPreferences=new AdvancedPreferences(PreferenceConfig.getDefault(sharedPreferences));
    }
    private String getTestKey(String key){
        return TEST_PREFIX+key;
    }
    @After
    public void finalize(){
        List<String> toDeleteKeys=new ArrayList<>();
        sharedPreferences.getAll().forEach((key,val)->{
            if(key.startsWith(TEST_PREFIX))
                toDeleteKeys.add(key);
        });
        toDeleteKeys.forEach((key)-> sharedPreferences.edit()
                .remove(key)
                .commit());
    }
    @Test
    public void validTestCase() throws IllegalMethodException {
        TestModel testModel=advancedPreferences.getPreferenceModel(TestModel.class);
        assertEquals(testModel.getSomeBool(),SOME_BOOL_VAL);
        assertEquals(testModel.getSomeInt(),SOME_INT_VAL);
        assertEquals(testModel.getSomeString(),SOME_STRING_VAL);
        assertEquals(testModel.getSomeStringSet(),SOME_STRING_SET_VAL);

        testModel.setSomeBool(!SOME_BOOL_VAL);
        testModel.setSomeString(SOME_STRING_VAL+"_new");

        TreeSet<String> newSet = new TreeSet<>(SOME_STRING_SET_VAL);
        newSet.add("new");
        testModel.setSomeStringSet(newSet);
        testModel.setSomeInt(SOME_INT_VAL+100);

        assertEquals(testModel.getSomeBool(),!SOME_BOOL_VAL);
        assertEquals(testModel.getSomeInt().intValue(),SOME_INT_VAL+100);
        assertEquals(testModel.getSomeString(),SOME_STRING_VAL+"_new");
        assertEquals(testModel.getSomeStringSet(),newSet);
    }
    @Test()
    public void validReactiveTestCase() throws IllegalMethodException {

        ReactiveTestModel testModel=advancedPreferences.getPreferenceModel(ReactiveTestModel.class);
        testModel.getSomeBool().subscribe((val)->assertEquals(SOME_BOOL_VAL,val),(throwable)->assertTrue(false));
        testModel.getSomeInt().subscribe((val)->assertEquals(SOME_INT_VAL,val),(throwable)->assertTrue(false));
        testModel.getSomeString().subscribe((val)->assertEquals(SOME_STRING_VAL,val),(throwable)->assertTrue(false));
        testModel.getSomeStringSet().subscribe((val)->assertEquals(SOME_STRING_SET_VAL,val),(throwable)->assertTrue(false));

        Completable updateAll =Completable.mergeArray(
                testModel.setSomeBool(SOME_BOOL_VAL),
                testModel.setSomeString(SOME_STRING_VAL),
                testModel.setSomeStringSet(SOME_STRING_SET_VAL),
                testModel.setSomeInt(SOME_INT_VAL));

        updateAll.blockingAwait();
    }
    public interface TestModel extends PreferenceModel{
        @PreferenceOperation(key = TEST_PREFIX+SOME_INT_KEY)
        Integer getSomeInt();
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_KEY)
        String getSomeString();
        @PreferenceOperation(key = TEST_PREFIX+SOME_BOOL_KEY)
        Boolean getSomeBool();
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_SET_KEY)
        TreeSet<String> getSomeStringSet();
        @PreferenceOperation(key = TEST_PREFIX+SOME_INT_KEY)
        void setSomeInt(Integer someVal);
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_KEY)
        void setSomeString(String someVal);
        @PreferenceOperation(key = TEST_PREFIX+SOME_BOOL_KEY)
        void setSomeBool(Boolean someVal);
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_SET_KEY)
        void setSomeStringSet(Set<String> someVal);
    }
    public interface ReactiveTestModel extends PreferenceModel{
        @PreferenceOperation(key = TEST_PREFIX+SOME_INT_KEY)
        Observable<Integer> getSomeInt();
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_KEY)
        Observable<String> getSomeString();
        @PreferenceOperation(key = TEST_PREFIX+SOME_BOOL_KEY)
        Observable<Boolean> getSomeBool();
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_SET_KEY)
        Observable<TreeSet<String>> getSomeStringSet();
        @PreferenceOperation(key = TEST_PREFIX+SOME_INT_KEY)
        Completable setSomeInt(Integer someVal);
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_KEY)
        Completable setSomeString(String someVal);
        @PreferenceOperation(key = TEST_PREFIX+SOME_BOOL_KEY)
        Completable setSomeBool(Boolean someVal);
        @PreferenceOperation(key = TEST_PREFIX+SOME_STRING_SET_KEY)
        Completable setSomeStringSet(Set<String> someVal);
    }
}
