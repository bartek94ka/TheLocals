package com.example.bartosz.thelocals;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bartosz.thelocals.Adapters.AttractionListAttractionAdapter;
import com.example.bartosz.thelocals.Listeners.IAttractionListPassListener;
import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class GuideTripDetails extends Fragment {

    private View view;

    private Button backButton;
    private Button saveButton;
    private Button setAttractionsButton;

    private EditText etName;
    private Spinner spinnerProvince;
    private EditText etDuration;
    private EditText etDescription;
    private EditText etAdditionalInfo;

    private Handler handler;
    private AttractionListAttractionAdapter adapter;
    private IAttractionListPassListener mListener;
    private String attractionListId;
    private AttractionListManager attractionListManager;
    private AttractionList attractionList;

    public GuideTripDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_trip_details, container, false);
        backButton = view.findViewById(R.id.backButton);
        saveButton = view.findViewById(R.id.saveButton);
        setAttractionsButton = view.findViewById(R.id.setAttractionsButton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_list_details));
        //companyId = "f93fd190-4c8c-4e53-9cab-a9e8bef0f288";
        SetPropertiesFromArguments();
        attractionListManager = new AttractionListManager(getContext());
        adapter = new AttractionListAttractionAdapter(getContext());
        InitializeVeribles();
        SetButtonEvents();
        Thread thread = new ThreadGetData();
        thread.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (IAttractionListPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IAttractionPassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void InitializeVeribles(){
        handler = new MyHandler();
        etName = view.findViewById(R.id.attractionListName);
        spinnerProvince = view.findViewById(R.id.attractionListProvince);
        etDuration = view.findViewById(R.id.attractionListDuration);
        etDescription = view.findViewById(R.id.attractionListDescription);
        etAdditionalInfo = view.findViewById(R.id.attractionListAdditionalInfo);
    }

    private void SetButtonEvents(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.PassGuideIdToGuideTripList(attractionList.GuideId);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAttracionList();
            }
        });

        setAttractionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = (String)spinnerProvince.getSelectedItem();
                UpdateAttracionList();
                mListener.PassAttractionListIdToCompanyAttraction(attractionListId, province);
            }
        });
    }

    private void SetAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getContext(), R.array.Provinces, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerProvince.setAdapter(adapter);
        if(attractionList.Province != null){
            int selectedPosition = GetProvinceUserAdapterPosition(adapter);
            spinnerProvince.setSelection(selectedPosition);
        }
    }

    private int GetProvinceUserAdapterPosition(ArrayAdapter<CharSequence> adapter){
        int position = adapter.getPosition(attractionList.Province);
        return position;
    }



    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            attractionListId = (String)args.get("attractionListId");
        }
    }

    private void UpdateInterface(AttractionList attractionList){
        if(attractionList != null){
            if(attractionList.Name != null)
                etName.setText(attractionList.Name);
            if(attractionList.Duration != null)
                etDuration.setText(attractionList.Duration);
            if(attractionList.Description != null)
                etDescription.setText(attractionList.Description);
            if(attractionList.AdditionalInfo != null)
                etAdditionalInfo.setText(attractionList.AdditionalInfo);
            SetAdapter();
        }
    }

    private void UpdateAttracionList(){
        String name = etName.getText().toString().trim();
        String province = (String)spinnerProvince.getSelectedItem();
        String duration = etDuration.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String additionalInfo = etAdditionalInfo.getText().toString().trim();
        attractionList.Name = name;
        attractionList.Province = province;
        attractionList.Duration = duration;
        attractionList.Description = description;
        attractionList.AdditionalInfo = additionalInfo;
        attractionListManager.UpdateFirebaseAttractionList(attractionListId, attractionList);
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Add loading view during search processing
                    //listViewAttractions.addFooterView(_footerView);
                    break;
                case 1:
                    //Update data adapter and UI
                    attractionList = ((AttractionList) msg.obj);
                    UpdateInterface(attractionList);
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadGetData extends Thread {
        @Override
        public void run() {
            attractionListManager.GetAttractionListById(attractionListId).
                    addOnCompleteListener(new OnCompleteListener<com.example.bartosz.thelocals.Models.AttractionList>() {
                        @Override
                        public void onComplete(@NonNull Task<AttractionList> task) {
                            if(task.isSuccessful()){
                                handler.sendEmptyMessage(0);
                                AttractionList lstResult = task.getResult();
                                Message msg = handler.obtainMessage(1, lstResult);
                                handler.sendMessage(msg);
                            }
                        }
                    });
        }
    }
}
