package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class AttractionSuggesstedListAdapter extends BaseAdapter {

    private Context context;
    private List<AttractionList> attractionLists;

    public AttractionSuggesstedListAdapter(Context context){
        this.context = context;
        attractionLists = new ArrayList<>();
    }

    public void AddAllItemsToAdapter(List<AttractionList> attractionLists){
        this.attractionLists.addAll(attractionLists);
        this.notifyDataSetChanged();
    }

    public AttractionList GetAttractionListByPosition(int position){
        return attractionLists.get(position);
    }

    @Override
    public int getCount() {
        return this.attractionLists.size();
    }

    @Override
    public Object getItem(int position) {
        return attractionLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_attraction_suggested_list, null);
        AttractionList attractionList = GetAttractionListByPosition(position);

        TextView itemName = view.findViewById(R.id.item_name);
        itemName.setText(attractionList.Name);
        Button itemDetailsButton = view.findViewById(R.id.item_Details);
        itemDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to attractionlist details
            }
        });
        return view;
    }
}
