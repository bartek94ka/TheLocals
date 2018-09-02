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

import com.example.bartosz.thelocals.Adapters.CompanyListAdapter;
import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import bolts.Continuation;


public class CompanyList extends Fragment {
    private View view;
    private ListView listViewCompanies;
    private Handler handler;
    private CompanyManager companyManager;
    private CompanyListAdapter companyListAdapter;

    public CompanyList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_company_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles() {
        listViewCompanies = view.findViewById(R.id.listview_companies);
        handler = new MyHandler();
        companyManager = new CompanyManager(getContext());
        companyListAdapter = new CompanyListAdapter(getContext());
        companyListAdapter.ClearList();
        listViewCompanies.setAdapter(companyListAdapter);

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
                    companyListAdapter.AddAllCompaniesToAdapter((ArrayList<Company>)msg.obj);
                    break;
            }
        }
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            companyManager.GetAllCompanies().addOnCompleteListener(new OnCompleteListener<ArrayList<Company>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<Company>> task) {
                    if(task.isSuccessful()){
                        handler.sendEmptyMessage(0);
                        ArrayList<Company> lstResult = task.getResult();
                        Message msg = handler.obtainMessage(1, lstResult);
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }
}
