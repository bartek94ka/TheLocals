package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttractionChangeOrderAdapter extends BaseAdapter {

    private Context context;
    private List<Attraction> attractions;

    public AttractionChangeOrderAdapter(Context context){
        this.context = context;
        attractions = new ArrayList<>();
    }

    public void AddAllItemsToAdapter(List<Attraction> list){
        attractions.addAll(list);
        this.notifyDataSetChanged();
    }

    public List<Attraction> GetAttracionList(){return attractions;}

    @Override
    public int getCount() {
        return attractions.size();
    }

    @Override
    public Object getItem(int i) {
        return attractions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_attraction_change_order, null);
        final Attraction attraction = (Attraction)getItem(position);
        TextView itemName = v.findViewById(R.id.item_name);
        itemName.setText(attraction.Name);
        ImageButton moveUp = v.findViewById(R.id.item_move_up);
        moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position-1 >= 0){
                    Collections.swap(attractions, position -1, position);
                    notifyDataSetChanged();
                }
            }
        });
        ImageButton moveDown = v.findViewById(R.id.item_move_down);
        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < getCount() - 1 ){
                    Collections.swap(attractions, position +1, position);
                    notifyDataSetChanged();
                }
            }
        });
        return v;
    }
}
