package com.example.bartosz.thelocals.Managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {

    private FirebaseDatabase _database;
    private FirebaseAuth _firebaseAuth;

    public UserManager(){
        _database = FirebaseDatabase.getInstance();
        _firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<User> getUserData(String userId) {
        final TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference localReference = _database.getReference("Users").child(userId);
        localReference.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        taskCompletionSource.setResult(user);
                        localReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return taskCompletionSource.getTask();
    }

    public void UpdateFirebaseUserData(String userId, User user){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put(userId, user);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.updateChildren(taskMap);
    }

    public void ChangeUserPassword(final Context context, FirebaseUser currentUser, String newPassword){
        if(currentUser == null){
            return;
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(currentUser.getEmail(), newPassword);
        final String password = newPassword;
        currentUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("PasswordUpdate", "Password updated");
                    Toast.makeText(context, "Hasło zmienione pomyślnie", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("PasswordUpadte", "Error password not updated");
                }
            }
        });
    }

    public void RegisterUser(final Context context, final String email, String password, final String name, final String surname){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(surname))
        {
            Toast.makeText(context, "Please enter surname", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        try {
            this._firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //CompleteRegistration(context, progressDialog, email, name, surname);
                            } else {
                                Log.d("TAG", "createUserWithEmail:fail");
                                Toast.makeText(context, "Could not register. Please try again", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public void AddUserToDatabase(Context context, String id, String email, String name, String surname, String province){
        String userId = _firebaseAuth.getCurrentUser().getUid();
        User newUser = new User(userId, email, name, surname, province);
        try {

            _database.getReference().child("Users").child(userId).setValue(newUser)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d("TAG", "AddUserToDatabase:success");
                                //Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                //Toast.makeText(context, "Could not register. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    private void CompleteRegistration(final Context context, final ProgressDialog progressDialog, String email, String name, String surname){
        String userId = _firebaseAuth.getCurrentUser().getUid();
        User newUser = CreateUserObject(email, name, surname);
        try {

            _database.getReference().child("Users").child(userId).setValue(newUser)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d("TAG", "createUserWithEmail:success");
                                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(context, "Could not register. Please try again", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public User CreateUserObject(String email, String name, String surname) {
        //User newCreatedUser = new User(email, name, surname);
        return null;
    }

    public void LoginUser(final Context context, String email, String password){

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(context, "Proszę wpisać email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(context, "Proszę wpisać hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logowanie...");
        progressDialog.show();

        _firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Zalogowano", Toast.LENGTH_SHORT).show();
                        } else {
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException invalidEmail){
                                Toast.makeText(context, "Nieprawidłowy adres email", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword){
                                Toast.makeText(context, "Nieprawidłowe hasło", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception ex){
                                Toast.makeText(context, "Nie udało się zalogować. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.hide();
                    }
                });
    }

    public void LogoutUser(){
        if(_firebaseAuth.getCurrentUser() != null){
            _firebaseAuth.signOut();
        }

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
