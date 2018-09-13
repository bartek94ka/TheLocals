package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bartosz.thelocals.Listeners.IGuidePassListener;
import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Models.Guide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class EditGuide extends Fragment {

    private View view;
    private EditText guideFirstName;
    private EditText guideLastName;
    private EditText guidePhoneNumber;
    private EditText guideCity;
    private EditText guideEmail;
    private EditText guideAboutMe;
    private EditText guidePhotoUrl;
    private Button buttonEdit;

    private GuideManager guideManager;
    private IGuidePassListener listener;
    private String guideId;
    private Guide guide;

    public EditGuide() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_edit_guide, container, false);
        buttonEdit = view.findViewById(R.id.nextEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateGuideData();
                listener.PassAttractionListIdToGuideTripList(guideId);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_edit_guide));
        SetPropertiesFromArguments();
        InitializeVeribles();
        FillGuideData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        try {
            listener = (IGuidePassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IGuidePassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void InitializeVeribles(){
        guideManager = new GuideManager(getContext());

        guideFirstName = view.findViewById(R.id.guideFirstName);
        guideLastName = view.findViewById(R.id.guideLastName);
        guidePhoneNumber = view.findViewById(R.id.guidePhoneNumber);
        guideEmail = view.findViewById(R.id.guideEmail);
        guideCity = view.findViewById(R.id.guideCity);
        guideAboutMe = view.findViewById(R.id.guideAboutMe);
        guidePhotoUrl = view.findViewById(R.id.guidePhotoUrl);
    }

    private void FillGuideData(){
        guideManager.GetGuide(guideId).addOnCompleteListener(new OnCompleteListener<Guide>() {
            @Override
            public void onComplete(@NonNull Task<Guide> task) {
                if(task.isSuccessful()){
                    guide = task.getResult();
                    guideFirstName.setText(guide.FirstName);
                    guideLastName.setText(guide.LastName);
                    guideAboutMe.setText(guide.AboutMe);
                    guideCity.setText(guide.City);
                    guideEmail.setText(guide.Email);
                    guidePhotoUrl.setText(guide.PhotoUrl);
                    guidePhoneNumber.setText(guide.PhoneNumber);
                }
            }
        });
    }

    private void UpdateGuideData(){
        String firstName = guideFirstName.getText().toString().trim();
        String lastName = guideLastName.getText().toString().trim();
        String phoneNumber = guidePhoneNumber.getText().toString().trim();
        String email = guideEmail.getText().toString().trim();
        String city = guideCity.getText().toString().trim();
        String aboutMe = guideAboutMe.getText().toString().trim();
        String photoUrl = guidePhotoUrl.getText().toString().trim();
        guide.FirstName = firstName;
        guide.LastName = lastName;
        guide.PhoneNumber = phoneNumber;
        guide.Email = email;
        guide.City = city;
        guide.AboutMe = aboutMe;
        guide.PhotoUrl = photoUrl;
        guideManager.UpdateFirebaseGuideData(guideId, guide);
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            guideId = (String)args.get("guideId");
        }
    }
}
