package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Managers.UserManager;
import com.example.bartosz.thelocals.Models.User;
import com.example.bartosz.thelocals.Validators.UserDataValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class UserSettings extends Fragment implements View.OnClickListener {

    private View view;

    private FirebaseAuth _firebaseAuth;
    private FirebaseUser _currentUser;
    private User _user;

    private EditText _settingsName;
    private Button _settingsSaveButton;

    private EditText _settingsOldPassword;
    private EditText _settingsNewPassword;
    private EditText _settingsConfirmPassword;
    private Button _settingsChangePasswordButton;
    private Spinner spinnerProvince;

    //private OnFragmentInteractionListener mListener;
    private UserManager _userManager;
    private UserDataValidator _userDataValidator;

    public UserSettings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_user_settings));
        InitializeLocalVeribles();
        FillBasicUserData();
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
                if(_userDataValidator.IsDataCorrect(getActivity(), name, "") == true){
                    Thread thread = new ThreadUpdateData();
                    thread.start();
                }
                break;
        }
    }

    private void InitializeLocalVeribles(){
        _userManager = new UserManager();
        _userDataValidator = new UserDataValidator();

        _firebaseAuth = FirebaseAuth.getInstance();
        _currentUser = _firebaseAuth.getCurrentUser();

        _settingsName = view.findViewById(R.id.settingsName);
        spinnerProvince = view.findViewById(R.id.settingsProvince);

        _settingsOldPassword = (EditText) view.findViewById(R.id.settingsOldPassword);
        _settingsNewPassword = (EditText) view.findViewById(R.id.settingsNewPassword);
        _settingsConfirmPassword = (EditText) view.findViewById(R.id.settingsConfirmNewPassword);
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
        int position = adapter.getPosition(_user.SelectedProvince);
        return position;
    }

    private void FillBasicUserData(){
        try{
            _userManager.getUserData(_currentUser.getUid()).onSuccessTask(new SuccessContinuation<User, Void>() {
                @NonNull
                @Override
                public Task<Void> then(@Nullable User user) throws Exception {
                    _user = user;
                    _settingsName.setText(_user.Name);
                    SetAdapter();
                    return null;
                }
            });
        }catch(Exception ex){

        }
    }

    public class ThreadUpdateData extends Thread {
        @Override
        public void run() {
            final String name = _settingsName.getText().toString().trim();
            _user.Name = name;
            _user.FullName = _user.Name;
            _user.SelectedProvince = (String)spinnerProvince.getSelectedItem();
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
