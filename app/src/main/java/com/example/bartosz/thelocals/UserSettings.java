package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Managers.UserManager;
import com.example.bartosz.thelocals.Models.User;
import com.example.bartosz.thelocals.Validators.UserDataValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class UserSettings extends Fragment implements View.OnClickListener {

    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth _firebaseAuth;
    private FirebaseDatabase _database;
    private FirebaseUser _currentUser;
    private User _user;

    private EditText _settingsName;
    private EditText _settingsSurname;
    private Button _settingsSaveButton;

    private EditText _settingsOldPassword;
    private EditText _settingsNewPassword;
    private EditText _settingsConfirmPassword;
    private Button _settingsChangePasswordButton;

    private OnFragmentInteractionListener mListener;
    private UserManager _userManager;
    private UserDataValidator _userDataValidator;

    public UserSettings() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserSettings newInstance(String param1, String param2) {
        UserSettings fragment = new UserSettings();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _userManager = new UserManager();
        _userDataValidator = new UserDataValidator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        Button settingsChangePasswordButton = (Button) view.findViewById(R.id.settingsChangePasswordButton);
        settingsChangePasswordButton.setOnClickListener(this);

        Button settingsSaveButton = (Button) view.findViewById(R.id.settingsSaveButton);
        settingsSaveButton.setOnClickListener(this);

        InitializeLocalVeribles();
        FillBasicUserData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.settingsChangePasswordButton:
                String oldPassword = _settingsOldPassword.getText().toString().trim();
                final String newPassword = _settingsNewPassword.getText().toString().trim();
                final String confirmPassword = _settingsConfirmPassword.getText().toString().trim();
                IsOldPasswordCorrect(oldPassword).
                        addOnCompleteListener(new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull Task<Boolean> task)
                            {
                                Boolean result = task.getResult();
                                if(result == true){
                                    if(newPassword.equals(confirmPassword)){
                                        _userManager.ChangeUserPassword(getActivity(), _currentUser, newPassword);
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Passwords are not equal", Toast.LENGTH_SHORT).show();
                                }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.settingsSaveButton:
                final String name = _settingsName.getText().toString().trim();
                final String surname = _settingsSurname.getText().toString().trim();
                if(_userDataValidator.IsDataCorrect(getActivity(), name, surname) == true){
                    Thread thread = new ThreadUpdateData();
                    thread.start();
                }
                break;
        }
    }

    private void InitializeLocalVeribles(){
        _firebaseAuth = FirebaseAuth.getInstance();
        _database = FirebaseDatabase.getInstance();
        _currentUser = _firebaseAuth.getCurrentUser();

        _settingsName = view.findViewById(R.id.settingsName);
        _settingsSurname = (EditText) view.findViewById(R.id.settingsSurname);

        _settingsOldPassword = (EditText) view.findViewById(R.id.settingsOldPassword);
        _settingsNewPassword = (EditText) view.findViewById(R.id.settingsNewPassword);
        _settingsConfirmPassword = (EditText) view.findViewById(R.id.settingsConfirmNewPassword);
    }

    private void FillBasicUserData(){
        _user = _userManager.getUserData(_currentUser.getUid()).getResult();
        _settingsName.setText(_user.Name);
        _settingsSurname.setText(_user.Surname);
    }

    public class ThreadUpdateData extends Thread {
        @Override
        public void run() {
            final String name = _settingsName.getText().toString().trim();
            final String surname = _settingsSurname.getText().toString().trim();
            _user.Name = name;
            _user.Surname = surname;
            _userManager.UpdateFirebaseUserData(_currentUser.getUid(), _user);
        }
    }

    private Task<Boolean> IsOldPasswordCorrect(String oldPassword){
        final TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        _firebaseAuth.signInWithEmailAndPassword(_currentUser.getEmail(), oldPassword).
                addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Boolean isPasswordCorrect = false;
                        if (task.isSuccessful()) {
                            isPasswordCorrect = true;
                        }
                        taskCompletionSource.setResult(isPasswordCorrect);
                    }
                });
        return taskCompletionSource.getTask();
    }
}
