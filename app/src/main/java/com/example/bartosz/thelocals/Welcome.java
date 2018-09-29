package com.example.bartosz.thelocals;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class Welcome extends Fragment {

    private View view;
    private LinearLayout selectProvinceLayout;
    private Spinner spinnerProvince;
    private String provinceName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public Welcome(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeVeribles();
        SetHeaderForActivity();
        SetPropertiesFromArguments();
        SetAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseAuth != null){
            firebaseAuth.addAuthStateListener(authStateListener);
        }

    }

    @Override
    public void onStop(){
        super.onStop();
        if(firebaseAuth != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void SetHeaderForActivity(){
        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        String name = taskInfo.get(0).topActivity.getClassName();
        if(name.contains("InitialActivity")){
            ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_home));
            selectProvinceLayout.setVisibility(View.VISIBLE);
            SetHandlers();
            firebaseAuth = FirebaseAuth.getInstance();
        }
        else if(name.contains("MainActivity")) {
            ((MainActivity) getActivity()).SetActionBarTitle(getString(R.string.fragment_home));
            selectProvinceLayout.setVisibility(View.GONE);
        }
    }

    private void InitializeVeribles(){
        selectProvinceLayout = view.findViewById(R.id.setProvince);
        spinnerProvince = view.findViewById(R.id.welcomeProvince);
    }

    private void SetAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getContext(), R.array.Provinces, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerProvince.setAdapter(adapter);
        int selectedPosition = GetProvinceUserAdapterPosition(adapter);
        spinnerProvince.setSelection(selectedPosition);
    }

    private int GetProvinceUserAdapterPosition(ArrayAdapter<CharSequence> adapter){
        if(provinceName != null){
            int position = adapter.getPosition(provinceName);
            return position;
        }
        return 0;
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            provinceName = (String)args.get("provinceName");
        }
    }

    private void SetHandlers(){
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provinceName = (String)spinnerProvince.getSelectedItem();
                ((InitialActivity)getActivity()).SetProvinceName(provinceName);
                //set province name at activity
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        };
    }
}
