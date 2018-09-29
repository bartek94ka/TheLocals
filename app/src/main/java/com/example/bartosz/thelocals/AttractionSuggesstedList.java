package com.example.bartosz.thelocals;

import android.app.ActivityManager;
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
import com.example.bartosz.thelocals.Listeners.IAttractionListPassListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Managers.UserManager;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AttractionSuggesstedList extends Fragment {

    private View view;
    private ListView listViewAttractionLists;
    private Handler handler;
    private AttractionListManager attractionListManager;
    private AttractionSuggesstedListAdapter adapter;
    private UserManager userManager;
    private User user;

    private String provinceName;

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
        SetHeaderForActivity();
        SetPropertiesFromArguments();
        InitializeLocalVeribles();
        Thread thread = new ThreadGetMoreData();
        thread.start();
    }

    private void SetHeaderForActivity(){
        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        String name = taskInfo.get(0).topActivity.getClassName();
        if(name.contains("InitialActivity")){
            ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_sugessted_list));
        }
        else if(name.contains("MainActivity")){
            ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_sugessted_list));
        }
    }

    private void InitializeLocalVeribles() {
        listViewAttractionLists = view.findViewById(R.id.listview_attractionLists);
        handler = new MyHandler();
        attractionListManager = new AttractionListManager(getContext());
        adapter = new AttractionSuggesstedListAdapter(getContext());
        listViewAttractionLists.setAdapter(adapter);
        userManager = new UserManager();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            SetUserData();
        }
    }

    private void SetUserData(){
        userManager.getUserData(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<User>() {
            @Override
            public void onComplete(@NonNull Task<User> task) {
                if(task.isSuccessful()){
                    user = task.getResult();
                    provinceName = user.SelectedProvince;
                }
            }
        });
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            provinceName = (String)args.get("provinceName");
        }
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
            attractionListManager.GetAllAttracionListsFromProvinceOrderByVisitsCounter(provinceName).addOnCompleteListener(new OnCompleteListener<ArrayList<AttractionList>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<AttractionList>> task) {
                    if(task.isSuccessful()){
                        handler.sendEmptyMessage(0);
                        ArrayList<AttractionList> lstResult = task.getResult();
                        Collections.reverse(lstResult);
                        Message msg = handler.obtainMessage(1, lstResult);
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }
}
