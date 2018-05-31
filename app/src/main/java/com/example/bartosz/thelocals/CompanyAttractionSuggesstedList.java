package com.example.bartosz.thelocals;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bartosz.thelocals.Adapters.CompanyAttractionSugesstedListAdapter;
import com.example.bartosz.thelocals.Models.AttractionList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyAttractionSuggesstedList extends Fragment {

    private List<AttractionList> attractionLists;

    private CompanyAttractionSugesstedListAdapter _listAdapter;
    private ListView _attractionLists;

    public CompanyAttractionSuggesstedList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        attractionLists = new ArrayList<>();
        _listAdapter = new CompanyAttractionSugesstedListAdapter(getActivity().getApplicationContext(), attractionLists);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_attraction_suggessted_lists, container, false);
    }

}
