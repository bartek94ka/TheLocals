package com.example.bartosz.thelocals;


import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Managers.UserManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Executor;

public class Login extends Fragment {

    private View view;

    private EditText email;
    private EditText password;
    private Button loginButton;
    private TextView tvRegisterLink;
    private ProgressDialog progressDialog;

    private LoginManager loginManager;
    private FirebaseAuth firebaseAuth;
    private UserManager userManager;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_login));
        InitializeLocalVeribles();
        InitializeClasses();
        SetHandlers();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseAuth.addAuthStateListener(authStateListener);
        /*
        user = firebaseAuth.getCurrentUser();
        if(user != null){
            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(loginIntent);
            LoginActivity.this.finish();
        }
        */
    }

    @Override
    public void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void InitializeLocalVeribles(){
        progressDialog = new ProgressDialog(getContext());
        email = view.findViewById(R.id.loginEmail);
        password = view.findViewById(R.id.loginPassword);
        loginButton = view.findViewById(R.id.loginButton);
        tvRegisterLink = view.findViewById(R.id.loginRegisterHere);
    }

    private void InitializeClasses(){
        FacebookSdk.sdkInitialize(getContext());
        loginManager = LoginManager.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userManager = new UserManager();
        callbackManager = CallbackManager.Factory.create();
    }

    private void SetHandlers(){

        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    if(email != null && firebaseAuth.getCurrentUser() != null)
                    {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Zalogowano", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding listener to change view
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        Log.d("LoginSuccess", "facebook:onSuccess");
                        Toast.makeText(getContext(), "Zalogowano przez Facebook", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("LoginCancel", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("LoginError", "facebook:onError", exception);
                    }
                });

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //user = firebaseAuth.getCurrentUser();
            }
        });

    }

    private void handleFacebookAccessToken(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isComplete()) {
                            FirebaseUser user = task.getResult().getUser();
                            final String uid = user.getUid();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getContext(), "Zalogowano pomyślnie za pomocą konta facebook", Toast.LENGTH_SHORT).show();
                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    JSONObject obj = response.getJSONObject();
                                    try {
                                        String name = obj.getString("name");
                                        String id = obj.getString("id");
                                        userManager.AddUserToDatabase(getContext(), uid, "", name, "", "Wielkopolskie");
                                        //_userManager.
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            request.executeAsync();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Nie udało się zalogować za pomocą konta facebook", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void LoginUser(){

        String emailString =  email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        if(TextUtils.isEmpty(emailString))
        {
            Toast.makeText(getContext(), "Proszę podać adres email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!userManager.isEmailValid(emailString)){
            Toast.makeText(getContext(), "Nieprawidłowy format adresu email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passwordString))
        {
            Toast.makeText(getContext(), "Proszę podać hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logowanie...");
        progressDialog.show();

        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Zalogowano", Toast.LENGTH_SHORT).show();
                        } else {
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException invalidEmail){
                                Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword){
                                Toast.makeText(getContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception ex){
                                Toast.makeText(getContext(), "Login Failed. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.hide();
                    }
                });
    }

}
