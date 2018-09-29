package com.example.bartosz.thelocals;


import android.app.ActivityManager;
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
import android.widget.ListView;

import com.example.bartosz.thelocals.Adapters.GuideListAdapter;
import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Models.Guide;
import com.example.bartosz.thelocals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class GuideList extends Fragment {

    private View view;
    private ListView listViewGuides;
    private Handler handler;
    private GuideManager guideManager;
    private GuideListAdapter guideListAdapter;

    public GuideList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_guide_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetHeaderForActivity();
        InitializeLocalVeribles();
    }

    private void SetHeaderForActivity(){
        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        String name = taskInfo.get(0).topActivity.getClassName();
        if(name.contains("InitialActivity")){
            ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_guide_list));
        }
        else if(name.contains("MainActivity")){
            ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_guide_list));
        }
    }

    private void InitializeLocalVeribles() {
        listViewGuides = view.findViewById(R.id.listview_guides);
        handler = new MyHandler();
        guideManager = new GuideManager(getContext());
        guideListAdapter = new GuideListAdapter(getContext());
        guideListAdapter.ClearList();
        listViewGuides.setAdapter(guideListAdapter);
        Thread thread = new ThreadGetMoreData();
        thread.start();
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
                    guideListAdapter.AddAllGuidesToAdapter((ArrayList<Guide>)msg.obj);
                    break;
            }
        }
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            guideManager.GetAllGuides().addOnCompleteListener(new OnCompleteListener<ArrayList<Guide>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<Guide>> task) {
                    if(task.isSuccessful()){
                        handler.sendEmptyMessage(0);
                        ArrayList<Guide> lstResult = task.getResult();
                        Message msg = handler.obtainMessage(1, lstResult);
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }
}
