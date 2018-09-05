package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AttractionListDetails extends Fragment {

    private View view;
    private TextView attractionListName;
    private TextView attractionListProvince;
    private TextView attractionListDuration;
    private TextView attractionListAttractionCount;
    private TextView attractionListDescription;
    private TextView attractionListAdditionalInfo;

    private String attractionListId;
    private AttractionListManager attractionListManager;
    private AttractionList attractionList;

    public AttractionListDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attraction_list_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //companyId = "f93fd190-4c8c-4e53-9cab-a9e8bef0f288";
        SetPropertiesFromArguments();
        attractionListManager = new AttractionListManager(getContext());
        InitializeVeribles();
        SetAttrcationListProperties();
    }

    private void InitializeVeribles(){

        attractionListName = view.findViewById(R.id.attractionListName);
        attractionListProvince = view.findViewById(R.id.attractionListProvince);
        attractionListDuration = view.findViewById(R.id.attractionListDuration);
        attractionListAttractionCount = view.findViewById(R.id.attractionListAttractionCount);
        attractionListDescription = view.findViewById(R.id.attractionListDescription);
        attractionListAdditionalInfo = view.findViewById(R.id.attractionListAdditionalInfo);
    }

    private void SetAttrcationListProperties(){
        attractionListManager.GetAttractionListById(attractionListId).addOnCompleteListener(new OnCompleteListener<AttractionList>() {
            @Override
            public void onComplete(@NonNull Task<AttractionList> task) {
                attractionList = task.getResult();
                attractionListName.setText(attractionList.Name);
                attractionListProvince.setText(attractionList.Province);
                attractionListDuration.setText(attractionList.Duration + " h");
                attractionListAttractionCount.setText(String.valueOf(attractionList.Attractions.size()));
                attractionListDescription.setText(attractionList.Description);
                attractionListAdditionalInfo.setText(attractionList.AdditionalInfo);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            attractionListId = (String)args.get("attractionListId");
        }
    }
}
