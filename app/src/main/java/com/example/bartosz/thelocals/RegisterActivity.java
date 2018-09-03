package com.example.bartosz.thelocals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Managers.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private Spinner spinnerProvince;

    private Button buttonRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        _database = FirebaseDatabase.getInstance();
        _databaseRef = _database.getReference();
        _firebaseAuth = FirebaseAuth.getInstance();

        _userManager = new UserManager();
        _progressBar = new ProgressDialog(this);

        etEmail = (EditText) findViewById(R.id.regEmail);
        etPassword = (EditText) findViewById(R.id.regPassword);
        etConfirmPassword = (EditText) findViewById(R.id.regConfirmPassword);
        etName = (EditText) findViewById(R.id.regName);
        spinnerProvince = findViewById(R.id.regProvince);
        SetAdacpter();

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
                if(!password.contains(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show();
                }
                else{
                    //_userManager.RegisterUser(getApplicationContext(), email, password, name, surname);
                    RegisterUser();
                }
            }
        });
    }

    private void RegisterUser(){

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!_userManager.isEmailValid(email)){
            Toast.makeText(this, "Wrong email foramt. Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        _progressBar.setMessage("Registering User...");
        _progressBar.show();


        try {
            this._firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "createUserWithEmail:success");
                                Toast.makeText(RegisterActivity.this, "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show();
                                String name = etName.getText().toString().trim();
                                String province = (String)spinnerProvince.getSelectedItem();
                                _userManager.AddUserToDatabase(getApplicationContext(), _firebaseAuth.getUid(), "", name, "", province);
                                //TODO extend class by adding data od user
                                Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                RegisterActivity.this.startActivity(registerIntent);
                                RegisterActivity.this.finish();
                            } else {
                                Log.d("TAG", "createUserWithEmail:fail");
                                Toast.makeText(RegisterActivity.this, "Nie udało się zarejestrować. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            }
                            _progressBar.hide();
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    private void SetAdacpter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getApplicationContext(), R.array.Provinces, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerProvince.setAdapter(adapter);
    }
}
