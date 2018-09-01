package com.example.bartosz.thelocals.Providers;

import com.example.bartosz.thelocals.AttractionList;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
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
        AttractionListsProvider attractionListsProvider = new AttractionListsProvider(companyId);

        //Act
        ArrayList<com.example.bartosz.thelocals.Models.AttractionList> result = attractionListsProvider.GetAttractionListsForCompany().getResult();

        //Assert
        assertNotNull(result);
    }
}
