package com.example.bartosz.thelocals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bartosz.thelocals.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCompany extends Fragment {


    public AddCompany() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_company, container, false);
    }

}
