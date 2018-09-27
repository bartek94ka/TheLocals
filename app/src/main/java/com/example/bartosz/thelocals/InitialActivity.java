package com.example.bartosz.thelocals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.bartosz.thelocals.Listeners.IAttractionListDetailsPassListener;
import com.example.bartosz.thelocals.Listeners.IAttractionListPassListener;
import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Listeners.IGuidePassListener;
import com.example.bartosz.thelocals.Listeners.IMapPassListener;
import com.example.bartosz.thelocals.Listeners.IWelcomePageListener;
import com.example.bartosz.thelocals.Managers.UserManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class InitialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        IAttractionPassListener, IComapnyPassListener, IMapPassListener, IWelcomePageListener, IAttractionListPassListener,
        IGuidePassListener, IAttractionListDetailsPassListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        UseDefaultFragment();

        CheckIfUserIsLogged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_welcome) {
            fragment = new Welcome();
        } else if (id == R.id.nav_companyList){
            fragment = new CompanyList();
        } else if (id == R.id.nav_attractionSuggesstedList){
            fragment = new AttractionSuggesstedList();
            //dorzucić przekazywanie parametru regionu
        } else if (id == R.id.nav_guideList){
            fragment = new GuideList();
        } else if (id == R.id.nav_attractionList){
            fragment = new com.example.bartosz.thelocals.AttractionList();
            //dorzucić przekazywanie parametru regionu
        } else if (id == R.id.nav_login){
            fragment = new Login();
        } else if (id == R.id.nav_register){
            fragment = new Register();
        }

        if(fragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(getVisibleFragment().getId(), fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SetActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void UseDefaultFragment(){
        fragment = new Welcome();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.commit();
    }

    private Fragment getVisibleFragment(){
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    private void CheckIfUserIsLogged(){
        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    if(email != null && firebaseAuth.getCurrentUser() != null)
                    {
                        Intent intent = new Intent( InitialActivity.this, MainActivity.class);
                        startActivity(intent);
                        InitialActivity.this.finish();
                    }
                }
            }
        };
    }

    @Override
    public void PassAttractionList(ArrayList<Attraction> attractions) {
        Fragment selectedAttractionsOnMap = new SelectedAttractionsOnMap();
        Bundle args = new Bundle();
        args.putSerializable("attractions", attractions);
        selectedAttractionsOnMap.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), selectedAttractionsOnMap);
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListToAttractionLists(com.example.bartosz.thelocals.Models.AttractionList attractionList) {
        Fragment companyAttractionSuggesstedList = new CompanyAttractionSuggesstedList();
        Bundle args = new Bundle();
        args.putSerializable("attractionList", attractionList.CompanyId);
        companyAttractionSuggesstedList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), companyAttractionSuggesstedList);
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionIdToAttractionDetails(String attractionId, String provinceName) {
        Fragment attractionDetails = new AttractionDetails();
        Bundle args = new Bundle();
        args.putString("attractionId", attractionId);
        args.putString("provinceName", provinceName);
        attractionDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), attractionDetails);
        fragmentTransaction.commit();
    }

    @Override
    public void PassComapnyIdToComapnyAttractionSugesstedList(String id) {
        Fragment comapnyAttractionSuggestedList = new CompanyAttractionSuggesstedList();
        Bundle args = new Bundle();
        args.putString("companyId", id);
        comapnyAttractionSuggestedList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), comapnyAttractionSuggestedList);
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListIdToCompanyAttractionList(String id, String provinceName) {
        Fragment comapnyAttractionList = new CompanyAttractionList();
        Bundle args = new Bundle();
        args.putString("attractionListId", id);
        args.putString("provinceName", provinceName);
        comapnyAttractionList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        //fragmentTransaction.addToBackStack(null);
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), comapnyAttractionList);
        fragment = comapnyAttractionList;
        //
        // fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListIdToCompanyAttractionListDetails(String id) {
        Fragment comapnyAttractionListDetails = new CompanyAttractionListDetails();
        Bundle args = new Bundle();
        args.putString("attractionListId", id);
        comapnyAttractionListDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        //fragmentTransaction.addToBackStack(null);
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), comapnyAttractionListDetails);
        fragment = comapnyAttractionListDetails;
        //
        // fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit();
    }

    @Override
    public void PassCompanyIdToCompanyDetails(String id) {
        Fragment companyDetails = new CompanyDetails();
        Bundle args = new Bundle();
        args.putString("companyId", id);
        companyDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), companyDetails);
        fragmentTransaction.commit();
    }

    @Override
    public void PassCompanyIdToCompanyEdit(String id) {
        Fragment editCompany = new EditCompany();
        Bundle args = new Bundle();
        args.putString("companyId", id);
        editCompany.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), editCompany);
        fragmentTransaction.commit();
    }

    @Override
    public void OpenSetMarkerOnMapFragment() {
        Fragment setMarkerOnMap = new SetMarkerOnMap();
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), setMarkerOnMap);
        fragmentTransaction.commit();
    }

    @Override
    public void GoToWelcomePage() {
        Fragment welcome = new Welcome();
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), welcome);
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListIdToAttractionListDetails(String attractionListId) {
        Fragment attractionListDetails = new AttractionListDetails();
        Bundle args = new Bundle();
        args.putString("attractionListId", attractionListId);
        attractionListDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), attractionListDetails);
        fragmentTransaction.commit();
    }



    @Override
    public void PassAttractionListIdToGuideTripDetails(String id) {
        Fragment guideTripDetails = new GuideTripDetails();
        Bundle args = new Bundle();
        args.putString("attractionListId", id);
        guideTripDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), guideTripDetails);
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListIdToGuideTripList(String id) {
        Fragment guideTripList = new GuideTripList();
        Bundle args = new Bundle();
        args.putString("guideId", id);
        guideTripList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), guideTripList);
        fragmentTransaction.commit();
    }

    @Override
    public void PassGuideIdToGuideEdit(String id) {
        Fragment editGuide = new EditGuide();
        Bundle args = new Bundle();
        args.putString("guideId", id);
        editGuide.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), editGuide);
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListIdToCompanyAttraction(String id, String provinceName){
        Fragment comapnyAttractionList = new CompanyAttractionList();
        Bundle args = new Bundle();
        args.putString("attractionListId", id);
        args.putString("provinceName", provinceName);
        comapnyAttractionList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), comapnyAttractionList);
        fragment = comapnyAttractionList;
        fragmentTransaction.commit();
    }

    @Override
    public void PassGuideIdToGuideTripList(String id){
        Fragment guideTripList = new GuideTripList();
        Bundle args = new Bundle();
        args.putString("guideId", id);
        guideTripList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), guideTripList);
        fragmentTransaction.commit();
    }

    @Override
    public void PassGuideIdToGuideDetails(String id){
        Fragment guideDetails = new GuideDetails();
        Bundle args = new Bundle();
        args.putString("guideId", id);
        guideDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), guideDetails);
        fragmentTransaction.commit();
    }

    @Override
    public void PassCompanyIdToComapnyDetails(String id) {
        Fragment companyDetails = new CompanyDetails();
        Bundle args = new Bundle();
        args.putString("companyId", id);
        companyDetails.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(fragment.getId(), companyDetails);
        fragmentTransaction.commit();
    }
}
