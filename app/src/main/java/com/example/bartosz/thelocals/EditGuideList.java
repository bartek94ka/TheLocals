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

import com.example.bartosz.thelocals.Adapters.EditCompanyAdapter;
import com.example.bartosz.thelocals.Adapters.EditGuideAdapter;
import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Models.Guide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EditGuideList extends Fragment {

    private View view;
    private ListView listViewCompanies;
    private Handler handler;
    private GuideManager guideManager;
    private EditGuideAdapter adapter;

    public EditGuideList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_company_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_edit_guide_list));
        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles() {
        listViewCompanies = view.findViewById(R.id.listview_companies);
        handler = new MyHandler();
        guideManager = new GuideManager(getContext());
        adapter = new EditGuideAdapter(getContext());
        adapter.ClearList();
        listViewCompanies.setAdapter(adapter);

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
                    adapter.AddAllCompaniesToAdapter((ArrayList<Guide>)msg.obj);
                    break;
            }
        }
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            guideManager.GetGuidesAddedByUserId(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<ArrayList<Guide>>() {
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
