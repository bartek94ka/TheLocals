package com.example.bartosz.thelocals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Managers.UserManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAttractionPassListener, IComapnyPassListener{


    private UserManager userManager;
    private Fragment fragment;

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

        UseDefaultFragment();

        userManager = new UserManager();


    }

    @Override
    public void onBackPressed() {
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
        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            fragmentTransaction.replace(getVisibleFragment().getId(), fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void UseDefaultFragment(){
        fragment = new AddCompany();
        //fragment = new CompanyAttractionSuggesstedList();
        //fragment = new Welcome();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.commit();
    }

    private void LogoutUserAndChangeActivity(){
        //TODO check if user was register by service - without FB
        FirebaseAuth.getInstance().signOut();
        userManager.LogoutUser();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
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
        fragment = getVisibleFragment();
        getSupportFragmentManager().beginTransaction().
                replace(fragment.getId(), selectedAttractionsOnMap).
                commit();

    }

    @Override
    public void PassAttractionListToAttractionLists(AttractionList attractionList) {
        Fragment companyAttractionSuggesstedList = new CompanyAttractionSuggesstedList();
        Bundle args = new Bundle();
        args.putSerializable("attractionList", attractionList.CompanyId);
        companyAttractionSuggesstedList.setArguments(args);
        FragmentTransaction fragmentTransaction = (getSupportFragmentManager().beginTransaction());
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
    public void PassComapnyIdToComapnyAttractionSugesstedList(String id) {
        Fragment comapnyAttractionSuggestedList = new CompanyAttractionSuggesstedList();
        Bundle args = new Bundle();
        args.putString("companyId", id);
        comapnyAttractionSuggestedList.setArguments(args);
        FragmentTransaction fragmentTransaction = (getSupportFragmentManager().beginTransaction());
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), comapnyAttractionSuggestedList);
        //fragment = comapnyAttractionSuggestedList;
        //
        // fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit();
    }

    @Override
    public void PassAttractionListIdToCompanyAttractionList(String id) {
        Fragment comapnyAttractionList = new CompanyAttractionList();
        Bundle args = new Bundle();
        args.putString("attractionListId", id);
        comapnyAttractionList.setArguments(args);
        FragmentTransaction fragmentTransaction = (getSupportFragmentManager().beginTransaction());
        //fragmentTransaction.addToBackStack(null);
        fragment = getVisibleFragment();
        fragmentTransaction.replace(fragment.getId(), comapnyAttractionList);
        //fragmentTransaction.addToBackStack(null);
        fragment = comapnyAttractionList;
        //
        // fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit();
    }
}
