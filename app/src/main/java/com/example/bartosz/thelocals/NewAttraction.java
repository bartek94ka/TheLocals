package com.example.bartosz.thelocals;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.Attraction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAttraction extends Fragment implements View.OnClickListener {

    private FirebaseDatabase _database;
    private DatabaseReference _databaseRef;
    private ProgressDialog _progressBar;

    private EditText etName;
    private EditText etDescription;
    private EditText etPhotoUrl;
    private EditText etSourceUrl;
    private EditText etLatitude;
    private EditText etLongitude;
    private Button buttonAdd;


    public NewAttraction(){
        _database = FirebaseDatabase.getInstance();
        _databaseRef = _database.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_attraction, container, false);

        //buttonAdd = (Button) view.findViewById(R.id.newAttractionAddButton);
        //buttonAdd.setOnClickListener(this);

        return inflater.inflate(R.layout.fragment_add_attraction, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _progressBar = new ProgressDialog(getContext());
        etName = (EditText) view.findViewById(R.id.newAttractionName);
        etDescription = (EditText) view.findViewById(R.id.newAttractionDescription);
        etPhotoUrl = (EditText) view.findViewById(R.id.newAttractionPhotoUrl);
        etSourceUrl = (EditText) view.findViewById(R.id.newAttractionSourceUrl);
        etLatitude = (EditText) view.findViewById(R.id.newAttractionLatitude);
        etLongitude = (EditText) view.findViewById(R.id.newAttractionLongitude);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newAttractionAddButton:
            {
                AddAttraction();
                break;
            }
    }
}


    private void AddAttraction()
    {
        _progressBar.setMessage("Dodawanie atrakcji ...");
        _progressBar.show();
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String photoUrl = etPhotoUrl.getText().toString().trim();
        String sourceUrl = etSourceUrl.getText().toString().trim();
        String Latitude = etLatitude.getText().toString().trim();
        String Longitude = etLongitude.getText().toString().trim();

        Attraction attraction = new Attraction(name, description, photoUrl, sourceUrl, Longitude, Latitude);

        try{
            String key = _database.getReference("Attractions").push().getKey();
            _database.getReference("Attractions").child(key).setValue(attraction).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    _progressBar.hide();
                    Toast.makeText(getContext(), "Dodano atrakcję" , Toast.LENGTH_SHORT).show();
                    ResetTextFields();
                }
            });
        }catch(Exception ex){
            System.out.print(ex.getMessage());

        }
    }

    private void ResetTextFields(){
        etName.setText("");
        etDescription.setText("");
        etPhotoUrl.setText("");
        etSourceUrl.setText("");
        etLongitude.setText("");
        etLatitude.setText("");
    }
}
