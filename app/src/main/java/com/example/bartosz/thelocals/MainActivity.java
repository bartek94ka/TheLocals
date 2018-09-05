package com.example.bartosz.thelocals;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bartosz.thelocals.Listeners.IAttractionListPassListener;
import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Listeners.IMapPassListener;
import com.example.bartosz.thelocals.Listeners.IWelcomePageListener;
import com.example.bartosz.thelocals.Managers.UserManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        IAttractionPassListener, IComapnyPassListener, IMapPassListener, IWelcomePageListener, IAttractionListPassListener {


    private UserManager userManager;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        userManager = new UserManager();

        CheckForPermissions();
    }

    @Override
    public void onBackPressed() {

        int index = fragmentManager.getBackStackEntryCount() - 1;
        List<Fragment> fragmentList = fragmentManager.getFragments();
        Fragment currentFragment = getVisibleFragment();
        try{
            SetMarkerOnMap setMarkerOnMap = (SetMarkerOnMap) fragmentList.get(index);
            MarkerOptions markerOptions = setMarkerOnMap.GetMarkerOption();
            Bundle args = new Bundle();
            args.putString("latitude", String.valueOf(markerOptions.getPosition().latitude));
            args.putString("longitude", String.valueOf(markerOptions.getPosition().longitude));
            currentFragment.setArguments(args);

            //Fragment newAttraction = fragmentManager.popBackStack();
            fragment.setArguments(args);
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(getVisibleFragment().getId(), newAttraction);
            //fragmentTransaction.commit();
        }catch(Exception ex){

        }

        /*
        FragmentManager.BackStackEntry backEntry = (FragmentManager.BackStackEntry) fragmentManager.getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        */

        if(fragment.getId() == 1){

            //fragment.setArguments();
        }
        //firstFragment.setData("yourString");
        //finish();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        } else if (id == R.id.nav_logout) {
            LogoutUserAndChangeActivity();
        } else if (id == R.id.nav_newAttraction){
            fragment = new NewAttraction();
        } else if (id == R.id.nav_addCompany) {
            fragment = new AddCompany();
        } else if (id == R.id.nav_userSettings) {
            fragment = new UserSettings();
        } else if (id == R.id.nav_companyList){
            fragment = new CompanyList();
        } else if (id == R.id.nav_attractionSuggesstedList){
            fragment = new AttractionSuggesstedList();
        }

        if(fragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(getVisibleFragment().getId(), fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void CheckForPermissions(){
        int permission = getApplication().getBaseContext().checkSelfPermission(Manifest.permission.MAPS_RECEIVE);
        if( permission == PackageManager.PERMISSION_DENIED){

        }
        else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.MAPS_RECEIVE)){
                Toast.makeText(this, "Udostępnienie lokalizacji jest potrzebne, aby korzystać funkcji aplikacji", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.MAPS_RECEIVE}, 1);
        }
    }

    private void UseDefaultFragment(){
        //fragment = new NewAttraction();
        //fragment = new SetMarkerOnMap();
        //fragment = new AttractionDetails();
        //fragment = new AddCompany();
        //fragment = new CompanyAttractionSuggesstedList();
        fragment = new Welcome();
        //fragment = new CompanyDetails();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.commit();
    }

    private void LogoutUserAndChangeActivity(){
        //TODO check if user was register by service - without FB

//        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        userManager.LogoutUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null && FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    public Fragment getVisibleFragment(){
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
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
    public void PassAttractionListToAttractionLists(AttractionList attractionList) {
        Fragment companyAttractionSuggesstedList = new CompanyAttractionSuggesstedList();
        Bundle args = new Bundle();
        args.putSerializable("attractionList", attractionList.CompanyId);
        companyAttractionSuggesstedList.setArguments(args);
        FragmentTransaction fragmentTransaction = (fragmentManager.beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), companyAttractionSuggesstedList);
        //fragment = companyAttractionSuggesstedList;
        /*
        getSupportFragmentManager().beginTransaction().
                replace(fragment.getId(), selectedAttractionsOnMap).
                commit();
        */
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
        fragmentTransaction.replace(fragment.getId(), attractionDetails);
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
        //fragment = comapnyAttractionSuggestedList;
        //
        // fragmentTransaction.addToBackStack("")
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
}
