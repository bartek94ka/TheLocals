package com.example.bartosz.thelocals;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.Attraction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAttraction extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_attraction);

        _database = FirebaseDatabase.getInstance();
        _databaseRef = _database.getReference();
        _progressBar = new ProgressDialog(this);

        etName = (EditText) findViewById(R.id.newAttractionName);
        etDescription = (EditText) findViewById(R.id.newAttractionDescription);
        etPhotoUrl = (EditText) findViewById(R.id.newAttractionPhotoUrl);
        etSourceUrl = (EditText) findViewById(R.id.newAttractionSourceUrl);
        etLatitude = (EditText) findViewById(R.id.newAttractionLatitude);
        etLongitude = (EditText) findViewById(R.id.newAttractionLongitude);

        buttonAdd = (Button) findViewById(R.id.newAttractionAddButton);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAttraction();
            }
        });
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
                    Toast.makeText(getApplicationContext(), "Dodano atrakcję" , Toast.LENGTH_SHORT).show();
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
