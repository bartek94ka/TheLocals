package com.example.bartosz.thelocals.Providers;

import android.content.Context;
import android.test.mock.MockContext;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.firebase.FirebaseApp;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

public class AttractionListsProviderTests {
    /*
    @Override
    protected void setUp() throws Exception {

        final SharedPreferencesMockContext mockContext = new SharedPreferencesMockContext(getContext());
        MockApplication mockApplication = new MockApplication(){
            @Override
            public Context getApplicationContext() {
                Log.d("tests", "Im here");
                return mockContext;
            }
        };


        context = mockContext;
        setApplication(mockApplication);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();

        super.setUp();
    }
*/
    @Test
    public void GetAttractionListForCompany_ShouldReturnNotNull(){
        //Arragne
        String companyId = "1aa0f3bb-160c-46de-89c3-4f84acf23d1b";
        //Context appContext = ;
        MockContext context = new MockContext();

        FirebaseApp.initializeApp(context);
        AttractionListsProvider attractionListsProvider = new AttractionListsProvider();

        //Act
        AttractionList result = attractionListsProvider.GetAttractionListById("0430c296-a245-4e27-956c-09dd5dba0953").getResult();
//        ArrayList<com.example.bartosz.thelocals.Models.AttractionList> result = attractionListsProvider.GetAttractionListsForCompany().getResult();

        //Assert
        assertNotNull(result);
    }
}
