package com.example.bartosz.thelocals;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Listeners.IGuidePassListener;
import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Models.Guide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class AddGuide extends Fragment {

    private View view;
    private EditText guideFirstName;
    private EditText guideLastName;
    private EditText guidePhoneNumber;
    private EditText guideCity;
    private EditText guideEmail;
    private EditText guideAboutMe;
    private EditText guidePhotoUrl;
    private Button buttonNext;

    private GuideManager guideManager;
    private IGuidePassListener listener;

    public AddGuide() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_guide, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_add_guide));
        InitializeVeribles();
        SetButtonListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IGuidePassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IGuidePassListener");
        }
    }

    private void InitializeVeribles(){
        guideManager = new GuideManager(getContext());

        buttonNext = view.findViewById(R.id.nextButton);
        guideFirstName = view.findViewById(R.id.guideFirstName);
        guideLastName = view.findViewById(R.id.guideLastName);
        guidePhoneNumber = view.findViewById(R.id.guidePhoneNumber);
        guideEmail = view.findViewById(R.id.guideEmail);
        guideCity = view.findViewById(R.id.guideCity);
        guideAboutMe = view.findViewById(R.id.guideAboutMe);
        guidePhotoUrl = view.findViewById(R.id.guidePhotoUrl);
    }

    private void SetButtonListener(){
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGuide();
            }
        });
    }

    private void AddGuide(){
        String firstName = guideFirstName.getText().toString().trim();
        String lastName = guideLastName.getText().toString().trim();
        String phoneNumber = guidePhoneNumber.getText().toString().trim();
        String email = guideEmail.getText().toString().trim();
        String city = guideCity.getText().toString().trim();
        String aboutMe = guideAboutMe.getText().toString().trim();
        String photoUrl = guidePhotoUrl.getText().toString().trim();

        Guide guide = new Guide(firstName, lastName, email, city, phoneNumber, aboutMe, photoUrl, null, null,null, FirebaseAuth.getInstance().getUid());
        String id = UUID.randomUUID().toString();
        guide.Id = id;
        try{
            guideManager.AddGuide(guide);
            ResetTextFields();
        }catch(Exception ex){
            System.out.print(ex.getMessage());
        }
        listener.PassAttractionListIdToGuideTripList(id);
    }

    private void ResetTextFields() {
        guideFirstName.setText("");
        guideLastName.setText("");
        guidePhoneNumber.setText("");
        guideEmail.setText("");
        guideCity.setText("");
        guideAboutMe.setText("");
        guidePhotoUrl.setText("");
    }
}
