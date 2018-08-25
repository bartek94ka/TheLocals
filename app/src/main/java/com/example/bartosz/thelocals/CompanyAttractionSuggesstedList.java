package com.example.bartosz.thelocals;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Adapters.CompanyAttractionSugesstedListAdapter;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CompanyAttractionSuggesstedList extends ListFragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView listViewAttractionLists;

    private List<AttractionList> attractionLists;
    private AttractionList selectedAttractionList;

    private CompanyAttractionSugesstedListAdapter attractionSugesstedListAdapter;

    public CompanyAttractionSuggesstedList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_company_attraction_suggessted_lists, container, false);
        attractionLists = new ArrayList<>();
        attractionSugesstedListAdapter = new CompanyAttractionSugesstedListAdapter(getContext());

        Button saveButton = view.findViewById(R.id.saveListsButton);
        Button addListItemButton = view.findViewById(R.id.addListItemButton);
        Button deleteListItemButton  = view.findViewById(R.id.deleteListItemButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addListItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttractionList attractionsList = new AttractionList();
                attractionsList.Id = UUID.randomUUID().toString();
                attractionSugesstedListAdapter.AddListItemToAdapter(attractionsList);
            }
        });
        deleteListItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attractionSugesstedListAdapter.DeleteListItem(selectedAttractionList);
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeLocalVeribles();
        listViewAttractionLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int a = 5;
                selectedAttractionList = (AttractionList)attractionSugesstedListAdapter.getItem(position);
                Toast.makeText(getContext(), "Kliknięto: " +position, Toast.LENGTH_SHORT);
            }
        });

    }

    private void InitializeLocalVeribles(){
        listViewAttractionLists = view.findViewById(android.R.id.list);
        attractionSugesstedListAdapter = new CompanyAttractionSugesstedListAdapter(getContext());
        attractionSugesstedListAdapter.ClearList();
        listViewAttractionLists.setAdapter(attractionSugesstedListAdapter);
        //listViewAttractionLists.setlistview

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "Kliknięto: " +position, Toast.LENGTH_SHORT);
    }
}
