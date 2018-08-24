package com.fastsoft.advancedpreference.conventerstest;

import com.fastsoft.advancedpreference.converters.CollectionsConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

/**
 * Created by ura on 23-Aug-18.
 */
@RunWith(JUnit4.class)
public class CollectionConverterTest {
    CollectionsConverter collectionsBinder =new CollectionsConverter();
    final List<String> list=Arrays.asList("2","3","4");
    @Test
    public void illegalConvertTest() throws IllegalAccessException {
        for (Method method:CollectionsConverter.class.getMethods()) {
            if(method.getName().equals("convertFromFirstTo")) {
                try {
                    method.invoke(collectionsBinder, list, String.class);
                }catch (InvocationTargetException e){
                    assertEquals(e.getCause().getClass(),IllegalArgumentException.class);
                }

            }
        }
    }
    @Test
    public void convertToInterfaceTest() throws IllegalAccessException {
        for (Method method:CollectionsConverter.class.getMethods()) {
            if(method.getName().equals("convertFromFirstTo")) {
                try {
                    method.invoke(collectionsBinder,list, Set.class);
                }catch (InvocationTargetException e){
                    assertEquals(e.getCause().getClass(),IllegalArgumentException.class);
                }
            }
        }
    }
    @Test
    public void convertTest(){
        assertEquals(list,new ArrayList<>(collectionsBinder.convertFromFirstTo(list,TreeSet.class)));
        assertEquals(list,new ArrayList<>(collectionsBinder.convertFromFirstTo(list,LinkedList.class)));
        assertEquals(list,new ArrayList<>(collectionsBinder.convertFromFirstTo(list,HashSet.class)));
        assertEquals(list,new ArrayList<>(collectionsBinder.convertFromFirstTo(list,Vector.class)));
    }
}
