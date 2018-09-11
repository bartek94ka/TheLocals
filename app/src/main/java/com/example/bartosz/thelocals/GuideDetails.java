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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Managers.ImageManager;
import com.example.bartosz.thelocals.Models.Guide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GuideDetails extends Fragment {

    private View view;
    private ImageView guidePhoto;
    private TextView fullName;
    private TextView email;
    private TextView phoneNumber;
    private TextView city;
    private TextView aboutMe;

    private GuideManager guideManager;
    private String guideId;

    public GuideDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_guide_detials));
        SetGuideIdArguments();
        guideManager = new GuideManager(getContext());
        InitializeVeribles();
    }

    private void InitializeVeribles(){
        guidePhoto = view.findViewById(R.id.iMageview);
        fullName = view.findViewById(R.id.guideFullName);
        email = view.findViewById(R.id.guideEmail);
        phoneNumber = view.findViewById(R.id.guidePhoneNumber);
        city = view.findViewById(R.id.guideCity);
        aboutMe = view.findViewById(R.id.guideCity);
        SetGuideProperties();
    }

    private void SetGuideProperties(){
        guideManager.GetGuide(guideId).addOnCompleteListener(new OnCompleteListener<Guide>() {
            @Override
            public void onComplete(@NonNull Task<Guide> task) {
                if(task.isSuccessful()){
                    Guide guide = task.getResult();
                    fullName.setText(guide.FirstName + " " + guide.LastName);
                    email.setText(guide.Email);
                    phoneNumber.setText(guide.PhoneNumber);
                    city.setText(guide.City);
                    aboutMe.setText(guide.AboutMe);
                    if(!guide.PhotoUrl.isEmpty()){
                        new ImageManager(guidePhoto).execute(guide.PhotoUrl);
                    }
                }
            }
        });
    }

    private void SetGuideIdArguments(){
        Bundle args = getArguments();
        if(args != null){
            guideId = (String)args.get("guideId");
        }
    }
}
