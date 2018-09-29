package com.example.bartosz.thelocals;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bartosz.thelocals.Adapters.AttractionListAttractionAdapter;
import com.example.bartosz.thelocals.Listeners.IAttractionListDetailsPassListener;
import com.example.bartosz.thelocals.Listeners.IAttractionListPassListener;
import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AttractionListDetails extends Fragment {

    private View view;
    private TextView attractionListName;
    private TextView attractionListProvince;
    private TextView attractionListDuration;
    private TextView attractionListAttractionCount;
    private TextView attractionListDescription;
    private TextView attractionListAdditionalInfo;
    private TextView attractionListVisitsCounter;
    private Button buttonSeeAtractionsOnMap;
    private Button buttonSeeOrganizator;
    private ListView attractionListAttracions;

    private AttractionListAttractionAdapter adapter;
    private IAttractionPassListener attractionPassListener;
    private IAttractionListDetailsPassListener iAttractionListDetailsPassListener;
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
        SetHeaderForActivity();
        SetPropertiesFromArguments();
        attractionListManager = new AttractionListManager(getContext());
        adapter = new AttractionListAttractionAdapter(getContext());
        InitializeVeribles();
        SetAttrcationListProperties();
        SetButtonEvents();
    }

    private void SetHeaderForActivity(){
        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        String name = taskInfo.get(0).topActivity.getClassName();
        if(name.contains("InitialActivity")){
            ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_list_details));
        }
        else if(name.contains("MainActivity")){
            ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_list_details));
        }
    }

    private void InitializeVeribles(){

        buttonSeeOrganizator = view.findViewById(R.id.buttonSeeOrganizator);
        buttonSeeAtractionsOnMap = view.findViewById(R.id.buttonSeeAtractionsOnMap);
        attractionListName = view.findViewById(R.id.attractionListName);
        attractionListProvince = view.findViewById(R.id.attractionListProvince);
        attractionListDuration = view.findViewById(R.id.attractionListDuration);
        attractionListAttractionCount = view.findViewById(R.id.attractionListAttractionCount);
        attractionListDescription = view.findViewById(R.id.attractionListDescription);
        attractionListAdditionalInfo = view.findViewById(R.id.attractionListAdditionalInfo);
        attractionListAttracions = view.findViewById(R.id.listview_attractions);
        attractionListVisitsCounter = view.findViewById(R.id.attractionListsVisitsCounter);
        attractionListAttracions.setAdapter(adapter);
    }

    private void SetAttrcationListProperties(){
        attractionListManager.GetAttractionListById(attractionListId).addOnCompleteListener(new OnCompleteListener<AttractionList>() {
            @Override
            public void onComplete(@NonNull Task<AttractionList> task) {
                if(task.isSuccessful()){
                    attractionList = task.getResult();
                    attractionListName.setText(attractionList.Name);
                    attractionListProvince.setText(attractionList.Province);
                    attractionListDuration.setText(attractionList.Duration + " h");
                    if(attractionList.Attractions == null){
                        attractionListAttractionCount.setText("0");
                    }else{
                        attractionListAttractionCount.setText(String.valueOf(attractionList.Attractions.size()));
                    }
                    attractionListDescription.setText(attractionList.Description);
                    attractionListAdditionalInfo.setText(attractionList.AdditionalInfo);
                    if(attractionList.Attractions != null){
                        ArrayList<Attraction> values = GetAttractionListFromHashMap(attractionList.Attractions);
                        Collections.sort(values);
                        adapter.AddListItemToAdapter(values);
                    }
                    IncrementAttractionListVisitsCounter();
                    attractionListVisitsCounter.setText(attractionList.VisitsCounter.toString());
                }
            }
        });
    }

    private void IncrementAttractionListVisitsCounter(){
        if(attractionList.VisitsCounter == null){
            attractionList.VisitsCounter = 1;
        }else{
            attractionList.VisitsCounter++;
        }
        attractionListManager.UpdateAttractionListVisitsCounter(attractionList);
    }

    private void SetButtonEvents(){
        buttonSeeAtractionsOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attractionPassListener.PassAttractionList((ArrayList<Attraction>) adapter.GetAttractionList());
            }
        });
        buttonSeeOrganizator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!attractionList.CompanyId.isEmpty()){
                    iAttractionListDetailsPassListener.PassCompanyIdToComapnyDetails(attractionList.CompanyId);
                }else if(!attractionList.GuideId.isEmpty()){
                    iAttractionListDetailsPassListener.PassGuideIdToGuideDetails(attractionList.GuideId);
                }
            }
        });
    }

    private ArrayList<Attraction> GetAttractionListFromHashMap(HashMap<String, Attraction> hashMap){
        Object[] array = hashMap.values().toArray();
        ArrayList<Attraction> attractions = new ArrayList<>();
        if(array != null){
            for(Object object: array){
                Attraction attraction = (Attraction)object;
                attractions.add(attraction);
            }
        }
        return attractions;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            attractionPassListener = (IAttractionPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IAttractionPassListener");
        }
        try {
            iAttractionListDetailsPassListener = (IAttractionListDetailsPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IAttractionListDetailsPassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attractionPassListener = null;
        iAttractionListDetailsPassListener = null;
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            attractionListId = (String)args.get("attractionListId");
        }
    }
}
