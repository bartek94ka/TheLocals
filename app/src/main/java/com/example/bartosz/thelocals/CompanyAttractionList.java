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
import android.widget.Button;
import android.widget.ListView;

import com.example.bartosz.thelocals.AttractionAdapters.AttractionListAdapter;
import com.example.bartosz.thelocals.AttractionAdapters.CompanyAttractionListAdapter;
import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Providers.AttractionInfoProvider;
import com.example.bartosz.thelocals.Providers.AttractionListsProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Continuation;


public class CompanyAttractionList extends Fragment {

    private View view;
    private ListView listViewAttractions;

    private Handler handler;

    private IAttractionPassListener mListener;
    private IComapnyPassListener comapnyPassListener;
    private AttractionInfoProvider attractionInfoProvider;
    private CompanyAttractionListAdapter attractionListAdapter;
    private AttractionListManager attractionListManager;
    private AttractionList attractionList;
    private String attractionListId;
    private String provinceName = "Wielkopolskie";

    public CompanyAttractionList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comapny_attraction_list, container, false);
        Button nextButton = view.findViewById(R.id.nextButton);
        Button saveButton = view.findViewById(R.id.saveListsButton);
        Button backButton = view.findViewById(R.id.backButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.PassAttractionList((ArrayList<Attraction>) attractionListAdapter.GetSelectedAttractionList());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attractionList.Attractions = new HashMap<>();
                List<Attraction> selectedAttraction = attractionListAdapter.GetSelectedAttractionList();
                if(selectedAttraction != null){
                    for(Attraction attraction : selectedAttraction){
                        attractionList.Attractions.put(attraction.Id, attraction);
                    }
                }
                attractionListManager.UpdateFirebaseAttractionList(attractionListId, attractionList);
                comapnyPassListener.PassComapnyIdToComapnyAttractionSugesstedList(attractionList.CompanyId);
                //mListener.PassAttractionListToAttractionLists((ArrayList<Attraction>) attractionListAdapter.GetSelectedAttractionList());
                //zapis listy do bazy
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.PassAttractionListToAttractionLists(attractionList);
                comapnyPassListener.PassComapnyIdToComapnyAttractionSugesstedList(attractionList.CompanyId);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (IAttractionPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IAttractionPassListener");
        }
        try{
            comapnyPassListener = (IComapnyPassListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement IComapnyPassListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_company_attracion_list));
        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles(){

        listViewAttractions = view.findViewById(R.id.listview_attractions);

        handler = new MyHandler();
        SetAttracionListIdArguments();
        attractionInfoProvider = new AttractionInfoProvider(provinceName);
        attractionListAdapter = new CompanyAttractionListAdapter(getContext());
        attractionListAdapter.ClearList();
        listViewAttractions.setAdapter(attractionListAdapter);
        attractionListManager = new AttractionListManager(getContext());
        Thread thread = new ThreadGetMoreData();
        thread.start();

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

    private void SetAttracionListIdArguments(){
        Bundle args = getArguments();
        if(args != null){
            attractionListId = (String)args.get("attractionListId");
            provinceName = (String)args.get("provinceName");
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
                    attractionListAdapter.addListItemToAdapter((ArrayList<Attraction>)msg.obj);
                    attractionListManager.GetAttractionListById(attractionListId).addOnCompleteListener(new OnCompleteListener<com.example.bartosz.thelocals.Models.AttractionList>() {
                        @Override
                        public void onComplete(@NonNull Task<com.example.bartosz.thelocals.Models.AttractionList> task) {
                            attractionList = task.getResult();
                            if(attractionList.Attractions != null){
                                ArrayList<Attraction> values = GetAttractionListFromHashMap(attractionList.Attractions);
                                attractionListAdapter.SetSelectedAttractionList(values);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            attractionInfoProvider.GetAllAttractionsByProvince(provinceName).
                    onSuccess(new Continuation<ArrayList<Attraction>, Void>() {

                        @Override
                        public Void then(bolts.Task<ArrayList<Attraction>> task) throws Exception {
                            handler.sendEmptyMessage(0);
                            ArrayList<Attraction> lstResult = task.getResult();
                            Message msg = handler.obtainMessage(1, lstResult);
                            handler.sendMessage(msg);
                            return null;
                        }});
        }
    }

}
