package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bartosz.thelocals.Managers.ImageManager;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Providers.AttractionInfoProvider;

import bolts.Continuation;
import bolts.Task;


public class AttractionDetails extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View view;
    private TextView textViewName;
    private TextView textViewDescription;
    private TextView textViewProvince;
    private TextView textViewSource;
    private ImageView attractionImageView;

    private AttractionInfoProvider attractionInfoProvider;
    private ImageManager imageManager;
    private String provinceName = "Wielkopolskie";
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

        attractionInfoProvider = new AttractionInfoProvider(provinceName);

        attractionId = "-LKSWSFULGGDFiQLjxFm";
        attractionImageView = (ImageView) view.findViewById(R.id.iMageview);
        textViewName = view.findViewById(R.id.attractionName);
        textViewDescription = view.findViewById(R.id.attractionDescription);
        textViewProvince = view.findViewById(R.id.attractionProvince);
        textViewSource = view.findViewById(R.id.attractionSource);

        InitializeLocalVeribles();
    }

    private void InitializeLocalVeribles(){

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
                        return null;
                    }
                });

    }

    private void SetImageViewProperties(){
        attractionImageView.setMinimumWidth(250);
    }
    /*
        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed(Uri uri) {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnFragmentInteractionListener) {
                //mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        *
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
