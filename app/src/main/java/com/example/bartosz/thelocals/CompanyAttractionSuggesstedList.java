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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Adapters.CompanyAttractionSugesstedListAdapter;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Models.Attraction;
import android.widget.AdapterView.OnItemClickListener;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Providers.AttractionListsProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CompanyAttractionSuggesstedList extends ListFragment implements OnItemClickListener {

    private View view;
    private ListView listViewAttractionLists;

    private AttractionListsProvider attractionListsProvider;
    private AttractionListManager attractionListManager;
    private CompanyManager companyManager;
    private List<AttractionList> attractionLists;
    private AttractionList selectedAttractionList;
    private String companyId;
    private Company company;

    private CompanyAttractionSugesstedListAdapter attractionSugesstedListAdapter;

    public CompanyAttractionSuggesstedList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_company_attraction_suggessted_lists, container, false);
        //attractionLists = new ArrayList<>();
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
                final AttractionList attractionsList = new AttractionList();
                attractionsList.Id = UUID.randomUUID().toString();
                attractionsList.CompanyId = companyId;


                attractionListManager.AddAttractionList(attractionsList);
                attractionSugesstedListAdapter.AddListItemToAdapter(attractionsList);
                companyManager.GetCompanyData(companyId).addOnCompleteListener(new OnCompleteListener<Company>() {
                    @Override
                    public void onComplete(@NonNull Task<Company> task) {
                        if(task.isSuccessful()){
                            company = task.getResult();
                            if(company.AttractionSuggestedList == null){
                                company.AttractionSuggestedList = new ArrayList<>();
                            }
                            company.AttractionSuggestedList.add(attractionsList.Id);
                            companyManager.UpdateFirebaseComapnyData(companyId, company);
                        }
                    }
                });
            }
        });
/*
        if(attractionListsProvider != null){
            attractionListsProvider.GetAttractionListsForCompany().addOnCompleteListener(new OnCompleteListener<ArrayList<AttractionList>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<AttractionList>> task) {
                    attractionLists = task.getResult();
                    attractionSugesstedListAdapter.AddAllItemsToAdapter(attractionLists);
                }
            });

        }
*/

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetComapnyIdArguments();

        attractionListsProvider = new AttractionListsProvider(companyId);
        attractionListsProvider.GetAttractionListsForCompany().addOnCompleteListener(new OnCompleteListener<ArrayList<AttractionList>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<AttractionList>> task) {
                attractionLists = task.getResult();
                attractionSugesstedListAdapter.AddAllItemsToAdapter(attractionLists);
            }
        });
        attractionListManager = new AttractionListManager(getContext());
        companyManager = new CompanyManager(getContext());

        /*
        InitializeLocalVeribles();
        listViewAttractionLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int a = 5;
                selectedAttractionList = (AttractionList)attractionSugesstedListAdapter.getItem(position);
                Toast.makeText(getContext(), "Kliknięto: " +position, Toast.LENGTH_SHORT);
            }
        });
*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Planets, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        */
        InitializeLocalVeribles();
    }

    private void SetAttractionListsForCompany(){
        if(attractionListsProvider != null){
            attractionListsProvider.GetAttractionListsForCompany().addOnCompleteListener(new OnCompleteListener<ArrayList<AttractionList>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<AttractionList>> task) {
                    attractionLists = task.getResult();
                    attractionSugesstedListAdapter.AddAllItemsToAdapter(attractionLists);
                }
            });
        }
    }


    private void InitializeLocalVeribles(){
        listViewAttractionLists = view.findViewById(android.R.id.list);
        attractionSugesstedListAdapter = new CompanyAttractionSugesstedListAdapter(getContext());
        attractionSugesstedListAdapter.ClearList();
        attractionSugesstedListAdapter.SetCompanyId(companyId);
        listViewAttractionLists.setAdapter(attractionSugesstedListAdapter);
        setListAdapter(attractionSugesstedListAdapter);
        getListView().setOnItemClickListener(this);
        //listViewAttractionLists.setlistview

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "Kliknięto: " +position, Toast.LENGTH_SHORT);
    }

    private void SetComapnyIdArguments(){
        Bundle args = getArguments();
        if(args != null){
            companyId = (String)args.get("companyId");

        }
    }
}
