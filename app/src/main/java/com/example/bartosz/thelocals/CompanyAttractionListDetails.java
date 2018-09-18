package com.example.bartosz.thelocals;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.bartosz.thelocals.Adapters.AttractionChangeOrderAdapter;
import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CompanyAttractionListDetails extends Fragment {

    private View view;

    private Button backButton;
    private Button saveButton;
    private Button setAttractionsButton;

    private EditText etName;
    private Spinner spinnerProvince;
    private EditText etDuration;
    private EditText etDescription;
    private EditText etAdditionalInfo;
    private ListView listViewAttractions;
    private AttractionChangeOrderAdapter adapter;

    private Handler handler;
    private IComapnyPassListener mListener;
    private AttractionListManager attractionListManager;
    private AttractionList attractionList;


    private String attractionListId;

    public CompanyAttractionListDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_company_attraction_list_details, container, false);
        backButton = view.findViewById(R.id.backButton);
        saveButton = view.findViewById(R.id.saveButton);
        setAttractionsButton = view.findViewById(R.id.setAttractionsButton);
        SetButtonActions();
        return view;
    }

    private void SetButtonActions(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mListener.PassComapnyIdToComapnyAttractionSugesstedList(attractionList.CompanyId);
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
            mListener.PassAttractionListIdToCompanyAttractionList(attractionListId, province);
            }
        });
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
        attractionList.Attractions = new HashMap<>();
        List<Attraction> attractions = adapter.GetAttracionList();
        if(attractions != null){
            int order = 1;
            for(Attraction attraction : attractions){
                attraction.AttracionListOrder = order;
                attractionList.Attractions.put(attraction.Id, attraction);
                order++;
            }
        }
        attractionListManager.UpdateFirebaseAttractionList(attractionListId, attractionList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles(){
        handler = new MyHandler();
        SetAttracionListIdArguments();
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_company_attracion_list_details));
        etName = view.findViewById(R.id.attractionListName);
        etDuration = view.findViewById(R.id.attractionListDuration);
        etDescription = view.findViewById(R.id.attractionListDescription);
        etAdditionalInfo = view.findViewById(R.id.attractionListAdditionalInfo);
        spinnerProvince = view.findViewById(R.id.attractionListProvince);
        listViewAttractions = view.findViewById(R.id.listview_attractions);
        attractionListManager = new AttractionListManager(getContext());
        adapter = new AttractionChangeOrderAdapter(getContext());
        listViewAttractions.setAdapter(adapter);

        Thread thread = new ThreadGetData();
        thread.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (IComapnyPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IComapnyPassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void SetAttracionListIdArguments(){
        Bundle args = getArguments();
        if(args != null){
            attractionListId = (String)args.get("attractionListId");
        }
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
                    if(attractionList.Attractions != null){
                        ArrayList<Attraction> values = GetAttractionListFromHashMap(attractionList.Attractions);
                        Collections.sort(values);
                        adapter.AddAllItemsToAdapter(values);
                    }
                    UpdateInterface(attractionList);
                    break;
                default:
                    break;
            }
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
}
