package com.example.bartosz.thelocals;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Listeners.IMapPassListener;
import com.example.bartosz.thelocals.Models.Attraction;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAttraction extends Fragment implements AdapterView.OnItemSelectedListener {

    private FirebaseDatabase _database;
    private DatabaseReference _databaseRef;
    private ProgressDialog _progressBar;

    private EditText etName;
    private EditText etDescription;
    private EditText etPhotoUrl;
    private EditText etSourceUrl;
    private EditText etLatitude;
    private EditText etLongitude;
    private TextView textViewNewAttractionLatitude;
    private TextView textViewNewAttractionLongitude;
    private Button buttonAdd;
    private Button buttonSetLocation;
    private Spinner spinnerProvince;

    private IMapPassListener mapPassListener;

    public NewAttraction(){
        _database = FirebaseDatabase.getInstance();
        _databaseRef = _database.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_attraction, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _progressBar = new ProgressDialog(getContext());
        etName = (EditText) view.findViewById(R.id.newAttractionName);
        etDescription = (EditText) view.findViewById(R.id.newAttractionDescription);
        etPhotoUrl = (EditText) view.findViewById(R.id.newAttractionPhotoUrl);
        etSourceUrl = (EditText) view.findViewById(R.id.newAttractionSourceUrl);
        spinnerProvince = view.findViewById(R.id.newAttractionProvince);
        buttonAdd = view.findViewById(R.id.newAttractionAddButton);
        buttonSetLocation = view.findViewById(R.id.setLocationButton);
        textViewNewAttractionLatitude = view.findViewById(R.id.newAttractionLatitude);
        textViewNewAttractionLongitude = view.findViewById(R.id.newAttractionLongitude);

        SetMarkerOption();
        SetAdapter();
        SetListeners();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mapPassListener = (IMapPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IMapPassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapPassListener = null;
    }

    private void SetListeners(){
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAttraction();
            }
        });
        buttonSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPassListener.OpenSetMarkerOnMapFragment();
            }
        });
    }

    private void SetAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getContext(), R.array.Provinces, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
    }

    private void AddAttraction()
    {
        _progressBar.setMessage("Dodawanie atrakcji ...");
        _progressBar.show();
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String photoUrl = etPhotoUrl.getText().toString().trim();
        String sourceUrl = etSourceUrl.getText().toString().trim();
        Double latitude = Double.parseDouble(textViewNewAttractionLatitude.getText().toString().trim());
        Double longitude = Double.parseDouble(textViewNewAttractionLongitude.getText().toString().trim());
        String userId = FirebaseAuth.getInstance().getUid();
        String province = spinnerProvince.getSelectedItem().toString();

        //Attraction attraction = new Attraction(name, description, photoUrl, sourceUrl, Longitude, Latitude);
        Attraction attraction = new Attraction(name, description, province, photoUrl, sourceUrl, longitude.toString(), latitude.toString());
        attraction.UserId = userId;
        try{
            String key = _database.getReference("Attractions/" + attraction.Province).push().getKey();
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
        textViewNewAttractionLongitude.setText("");
        textViewNewAttractionLatitude.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void SetMarkerOption(){
        Bundle args = getArguments();
        if(args != null){
            String latitude = (String)args.get("latitude");
            String longitude = (String)args.get("longitude");
            textViewNewAttractionLatitude.setText(latitude);
            textViewNewAttractionLongitude.setText(longitude);
        }
    }
}
