package com.example.bartosz.thelocals;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bartosz.thelocals.Adapters.CompanyAttractionSugesstedListAdapter;
import com.example.bartosz.thelocals.Adapters.TripListAdapter;
import com.example.bartosz.thelocals.Listeners.IWelcomePageListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Models.Guide;
import com.example.bartosz.thelocals.Providers.AttractionListsProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GuideTripList extends ListFragment{

    private View view;
    private ListView listViewAttractionLists;
    private IWelcomePageListener iWelcomePageListener;

    private AttractionListsProvider attractionListsProvider;
    private AttractionListManager attractionListManager;
    private GuideManager guideManager;
    private List<AttractionList> attractionLists;
    private AttractionList selectedAttractionList;
    private String guideId;
    private Guide guide;

    private TripListAdapter tripListAdapter;

    public GuideTripList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_company_attraction_suggessted_lists, container, false);
        //attractionLists = new ArrayList<>();
        tripListAdapter = new TripListAdapter(getContext());


        Button saveButton = view.findViewById(R.id.saveListsButton);
        Button addListItemButton = view.findViewById(R.id.addListItemButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWelcomePageListener.GoToWelcomePage();
            }
        });
        addListItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AttractionList attractionsList = new AttractionList();
                attractionsList.Id = UUID.randomUUID().toString();
                attractionsList.Description = "";
                attractionsList.Duration = "";
                attractionsList.AdditionalInfo = "";
                attractionsList.UserId = FirebaseAuth.getInstance().getUid();
                attractionsList.CompanyId = "";
                attractionsList.GuideId = guideId;

                attractionListManager.AddAttractionList(attractionsList);
                tripListAdapter.AddListItemToAdapter(attractionsList);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetGuideIdFromArguments();
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_company_attracion_suggessted_list));
        attractionListsProvider = new AttractionListsProvider("");
        attractionListsProvider.GetAttractionListsForGuide(guideId)
                .addOnCompleteListener(new OnCompleteListener<ArrayList<AttractionList>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<AttractionList>> task) {
                if(task.isSuccessful()){
                    ArrayList<AttractionList> attractionLists = task.getResult();
                    tripListAdapter.AddAllItemsToAdapter(attractionLists);
                }
            }
        });

        attractionListManager = new AttractionListManager(getContext());
        guideManager = new GuideManager(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializeLocalVeribles();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            iWelcomePageListener = (IWelcomePageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IWelcomePageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iWelcomePageListener = null;
    }

    private void InitializeLocalVeribles(){
        listViewAttractionLists = view.findViewById(android.R.id.list);
        tripListAdapter = new TripListAdapter(getContext());
        tripListAdapter.ClearList();
        //attractionSugesstedListAdapter.SetCompanyId(companyId);
        listViewAttractionLists.setAdapter(tripListAdapter);
        setListAdapter(tripListAdapter);
    }

    private void SetGuideIdFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            guideId = (String)args.get("guideId");
        }
    }
}
