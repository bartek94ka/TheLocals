package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IAttractionPassListener;
import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class AttractionListDisplayAdapter extends BaseAdapter{

    private Context context;
    private List<Attraction> attractions;
    //private List<Attraction> selectedAttractions;
    private IAttractionPassListener listener;

    public AttractionListDisplayAdapter(Context context){
        this.context = context;
        listener = (IAttractionPassListener) context;
        attractions = new ArrayList<>();
        //selectedAttractions = new ArrayList<>();
    }

    public void addListItemToAdapter(List<Attraction> list) {
        //Add list to current array list of data
        attractions.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }

    public void DeleteAttractionFromSelectedList(Attraction attraction){
        //selectedAttractions.remove(attraction);
        this.notifyDataSetChanged();
    }

    public Attraction GetAttraction(String id){
        for (Attraction attraction: attractions) {
            if(attraction.Id == id){
                return attraction;
            }
        }
        return null;
    }

    /*
    public List<Attraction> GetSelectedAttractionList(){
        return selectedAttractions;
    }*/

    public void ClearList(){
        attractions.clear();
    }

    @Override
    public int getCount() { return attractions.size(); }

    @Override
    public Object getItem(int position) {
        return attractions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item_attraction_list_display, null);
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
