package com.example.bartosz.thelocals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;


public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
    }
}
