package com.example.bartosz.thelocals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Managers.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseDatabase _database;
    private FirebaseAuth _firebaseAuth;
    private DatabaseReference _databaseRef;
    private ProgressDialog _progressBar;
    private UserManager _userManager;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etName;
    private EditText etSurname;

    private Button buttonRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        _userManager = new UserManager();

        etEmail = (EditText) findViewById(R.id.regEmail);
        etPassword = (EditText) findViewById(R.id.regPassword);
        etConfirmPassword = (EditText) findViewById(R.id.regConfirmPassword);
        etName = (EditText) findViewById(R.id.regName);
        etSurname = (EditText) findViewById(R.id.regSurname);

        buttonRegister = (Button) findViewById(R.id.regButton);
        tvLoginLink = (TextView) findViewById(R.id.regLoginHere);

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(registerIntent);
                RegisterActivity.this.finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password  = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String surname = etSurname.getText().toString().trim();
                if(password != confirmPassword){
                    Toast.makeText(getApplicationContext(), "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show();
                }
                else{
                    _userManager.RegisterUser(getApplicationContext(), email, password, name, surname);
                }
            }
        });
    }
}
