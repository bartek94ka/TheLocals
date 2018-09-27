package com.example.bartosz.thelocals;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Managers.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Register extends Fragment {

    private View view;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText name;
    private Spinner spinnerProvince;
    private Button registerButton;
    private TextView tvLoginLink;

    private UserManager userManager;
    private FirebaseAuth firebaseAuth;

    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_register));
        InitializeLocalVeribles();
        InitializeClasses();
        SetHandlers();
        SetAdapter();
    }

    private void InitializeLocalVeribles(){
        email = view.findViewById(R.id.regEmail);
        password = view.findViewById(R.id.regPassword);
        confirmPassword = view.findViewById(R.id.regConfirmPassword);
        name = view.findViewById(R.id.regName);
        spinnerProvince = view.findViewById(R.id.regProvince);
        registerButton = view.findViewById(R.id.regButton);
        tvLoginLink = view.findViewById(R.id.regLoginHere);
    }

    private void InitializeClasses(){
        userManager = new UserManager();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void SetHandlers(){
        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding listener to change view
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String passwordText  = password.getText().toString().trim();
                String confirmPasswordText = confirmPassword.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                if(!passwordText.contains(confirmPasswordText)){
                    Toast.makeText(getContext(), "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show();
                }
                else{
                    RegisterUser();
                }
            }
        });
    }

    private void RegisterUser(){

        final String emailText = email.getText().toString().trim();
        final String passwordText = password.getText().toString().trim();

        if(TextUtils.isEmpty(emailText))
        {
            Toast.makeText(getContext(), "Wprowadź adres email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!userManager.isEmailValid(emailText)){
            Toast.makeText(getContext(), "Nieprawidłowy format adresu email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passwordText))
        {
            Toast.makeText(getContext(), "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        try {
            this.firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show();
                                String nameText = name.getText().toString().trim();
                                String province = (String)spinnerProvince.getSelectedItem();
                                userManager.AddUserToDatabase(getContext(), firebaseAuth.getUid(), "", nameText, "", province);
                                //Change activity
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getContext(), "Zalogowano", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "Nie udało się zarejestrować. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    private void SetAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getContext(), R.array.Provinces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
    }
}
