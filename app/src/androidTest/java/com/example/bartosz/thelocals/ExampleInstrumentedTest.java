package com.example.bartosz.thelocals;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;
import android.text.format.Time;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Providers.AttractionListsProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.bartosz.thelocals", appContext.getPackageName());
    }

    @Test
    public void GetAttractionList_ShouldReturnExpectedValues(){
        //Arragne
        String attractionListId = "6f7da9c0-19eb-44d9-815f-11e92cde6bcd";
        Context appContext = InstrumentationRegistry.getTargetContext();
        AttractionListsProvider attractionListsProvider = new AttractionListsProvider();

        //Act
        attractionListsProvider.GetAttractionListById(attractionListId).addOnCompleteListener(new OnCompleteListener<AttractionList>() {
            @Override
            public void onComplete(@NonNull Task<AttractionList> task) {
            //Assert
            assertEquals(true, task.isSuccessful());
            assertNotNull(task.getResult());
            assertEquals("Zamki Poznania", task.getResult().Name);
            assertEquals("Wielkopolskie", task.getResult().Province);
            assertEquals("6x3CSiBJI7WzqgsuxrHRar5qt0i2", task.getResult().UserId);
            }
        });
        try {
            Thread.sleep((long) 10000.0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
