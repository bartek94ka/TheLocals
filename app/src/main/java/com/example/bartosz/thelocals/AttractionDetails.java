package com.example.bartosz.thelocals;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Managers.AttractionManager;
import com.example.bartosz.thelocals.Managers.ImageManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Providers.AttractionInfoProvider;

import java.util.List;

import bolts.Continuation;
import bolts.Task;


public class AttractionDetails extends Fragment {

    private IAttractionPassListener mListener;

    private View view;
    private TextView textViewName;
    private TextView textViewDescription;
    private TextView textViewProvince;
    private TextView textViewSource;
    private ImageView attractionImageView;
    private TextView textViewVisitsCounter;

    private AttractionInfoProvider attractionInfoProvider;
    private ImageManager imageManager;
    private AttractionManager attractionManager;
    private String provinceName;
    private String attractionId;

    private Attraction attraction;

    public AttractionDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attraction_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        SetHeaderForActivity();
        SetPropertiesFromArguments();
        attractionInfoProvider = new AttractionInfoProvider(provinceName);

        attractionImageView = (ImageView) view.findViewById(R.id.iMageview);
        textViewName = view.findViewById(R.id.attractionName);
        textViewDescription = view.findViewById(R.id.attractionDescription);
        textViewProvince = view.findViewById(R.id.attractionProvince);
        textViewSource = view.findViewById(R.id.attractionSource);
        textViewVisitsCounter = view.findViewById(R.id.attractionVisitsCounter);

        InitializeLocalVeribles();
    }

    private void SetHeaderForActivity(){
        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        String name = taskInfo.get(0).topActivity.getClassName();
        if(name.contains("InitialActivity")){
            ((InitialActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_details));
        }
        else if(name.contains("MainActivity")){
            ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_attraction_details));
        }
    }

    private void InitializeLocalVeribles(){

        attractionManager = new AttractionManager(getContext());
        attractionInfoProvider.GetAttractionById(attractionId).
                continueWith(new Continuation<Attraction, Void>() {
                    @Override
                    public Void then(Task<Attraction> task) throws Exception {
                        attraction = task.getResult();
                        textViewName.setText(attraction.Name);
                        textViewDescription.setText(attraction.Description);
                        textViewProvince.setText(attraction.Province);
                        textViewSource.setText(attraction.SourceUrl);
                        new ImageManager(attractionImageView).execute(attraction.PhotoUrl);
                        IncrementAttractionVisitsCounter();
                        textViewVisitsCounter.setText(attraction.VisitsCounter.toString());
                        return null;
                    }
                });

    }

    private void IncrementAttractionVisitsCounter(){
        if(attraction.VisitsCounter == null){
            attraction.VisitsCounter = 1;
        }else{
            attraction.VisitsCounter++;
        }
        attractionManager.UpdateAttractionVisitsCounter(attraction);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (IAttractionPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IAttractionPassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            attractionId = (String)args.get("attractionId");
            provinceName = (String)args.get("provinceName");
        }
    }
}
