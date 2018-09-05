package com.fastsoft.advancedpreference.units;

import com.fastsoft.advancedpreference.PreferenceModelValidator;
import com.fastsoft.advancedpreference.anotations.PreferenceOperation;
import com.fastsoft.advancedpreference.exceptions.IllegalMethodException;
import com.fastsoft.advancedpreference.models.PreferenceModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

@RunWith(JUnit4.class)
public class ValidatorTest implements PreferenceModel{
    PreferenceModelValidator preferenceModelValidator;

    @Before
    public void init() {
        preferenceModelValidator= new PreferenceModelValidator(TestPreferenceModel.class);
    }

    @Test(expected = IllegalMethodException.class)
    public void testVoidNoParamCase() throws NoSuchMethodException, IllegalMethodException {
        validateMethod("methodToValidateVoidNoParam");
    }
    @Test(expected = IllegalMethodException.class)
    public void testFewParamsCase() throws NoSuchMethodException, IllegalMethodException {
        validateMethod("methodToValidateFewParams");
    }
    @Test(expected = IllegalMethodException.class)
    public void testNoAnnotationCase() throws NoSuchMethodException, IllegalMethodException {
        validateMethod("methodToValidateNoAnnotation");
    }
    public void testOfValidMethods() throws NoSuchMethodException, IllegalMethodException {
        validateMethod("methodToValidateValid1");
        validateMethod("methodToValidateValid2");
    }
    private void validateMethod(String name) throws IllegalMethodException {
        for (Method method:TestPreferenceModel.class.getMethods()) {
            if(method.getName().equals(name)){
                preferenceModelValidator.validateMethod(method);
                break;
            }
        }
    }
    interface TestPreferenceModel extends PreferenceModel{
        @PreferenceOperation(key = "test")
        void methodToValidateVoidNoParam();
        @PreferenceOperation(key = "test")
        void methodToValidateFewParams(String a,String b);
        String methodToValidateNoAnnotation();

        @PreferenceOperation(key = "test")
        void methodToValidateValid1(String a);
        @PreferenceOperation(key = "test")
        String methodToValidateValid2();

    }
}
