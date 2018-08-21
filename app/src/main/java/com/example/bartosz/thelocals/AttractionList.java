package com.example.bartosz.thelocals;


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

import com.example.bartosz.thelocals.AttractionAdapters.AttractionListAdapter;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Providers.AttractionInfoProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import bolts.Continuation;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionList extends Fragment {

    private View view;
    private ListView listViewAttractions;

    private Handler handler;

    private AttractionInfoProvider attractionInfoProvider;
    private AttractionListAdapter attractionListAdapter;
    private String provinceName = "Wielkopolskie";

    public AttractionList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attraction_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles(){

        listViewAttractions = view.findViewById(R.id.listview_attractions);

        handler = new MyHandler();
        attractionListAdapter = new AttractionListAdapter();
        attractionInfoProvider = new AttractionInfoProvider(provinceName);

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
                    int count = ((ArrayList<Attraction>) msg.obj).size();
                    if(count == 0){
                        //_informText.setVisibility(View.VISIBLE);
                    }
                    //Remove loading view after update listview
                    //_listViewUser.removeFooterView(_footerView);
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
