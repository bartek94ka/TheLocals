package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class AttractionListAttractionAdapter extends BaseAdapter {

    IAttractionPassListener listener;
    private Context context;
    private List<Attraction> attractions;

    public AttractionListAttractionAdapter(Context context){
        this.context = context;
        listener = (IAttractionPassListener) context;
        attractions = new ArrayList<>();
    }

    public void AddListItemToAdapter(List<Attraction> list){
        attractions.addAll(list);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return attractions.size();
    }

    @Override
    public Object getItem(int position) {
        return attractions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_attraction_detail, null);

        final Attraction attraction = (Attraction)getItem(position);
        TextView itemName = (TextView)v.findViewById(R.id.item_name);
        itemName.setText(attraction.Name);
        Button itemDetails = v.findViewById(R.id.item_Details);
        itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.PassAttractionIdToAttractionDetails(attraction.Id, attraction.Province);
            }
        });
        return v;
    }
}
