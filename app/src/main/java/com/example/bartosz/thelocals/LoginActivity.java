package com.example.bartosz.thelocals;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import android.content.Intent;
import java.io.Console;

import bolts.Task;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        firebaseAuth = FirebaseAuth.getInstance();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        int a = 5;
                        Toast.makeText(getApplicationContext(), "Zalogowano przez Facebook", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        //Log.d(TAG, "facebook:onError", exception);
                    }
                });

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                user = firebaseAuth.getCurrentUser();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = firebaseAuth.getCurrentUser();
        Toast.makeText(getApplicationContext(), "Update UI", Toast.LENGTH_SHORT).show();
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isComplete()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            user = firebaseAuth.getCurrentUser();
                            int a = 5;
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
