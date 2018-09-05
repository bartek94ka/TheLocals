package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bartosz.thelocals.Adapters.AttractionSuggesstedListAdapter;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class AttractionSuggesstedList extends Fragment {

    private View view;
    private ListView listViewAttractionLists;
    private Handler handler;
    private AttractionListManager attractionListManager;
    private AttractionSuggesstedListAdapter adapter;

    public AttractionSuggesstedList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attraction_suggessted_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles() {
        listViewAttractionLists = view.findViewById(R.id.listview_attractionLists);
        handler = new MyHandler();
        attractionListManager = new AttractionListManager(getContext());
        adapter = new AttractionSuggesstedListAdapter(getContext());
        listViewAttractionLists.setAdapter(adapter);

        Thread thread = new ThreadGetMoreData();
        thread.start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Add loading view during search processing
                    //listViewAttractions.addFooterView(_footerView);
                    break;
                case 1:
                    //Update data adapter and UI
                    adapter.AddAllItemsToAdapter(((ArrayList<AttractionList>)msg.obj));
                    break;
            }
        }
    }

    private class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            attractionListManager.GetAllAttracionLists().addOnCompleteListener(new OnCompleteListener<ArrayList<AttractionList>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<AttractionList>> task) {
                    if(task.isSuccessful()){
                        handler.sendEmptyMessage(0);
                        ArrayList<AttractionList> lstResult = task.getResult();
                        Message msg = handler.obtainMessage(1, lstResult);
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }
}
