package com.example.bartosz.thelocals.AttractionAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class AttractionListAdapter extends BaseAdapter{

    private Context context;
    private List<Attraction> attractions;
    private List<Attraction> selectedAttractions;

    public AttractionListAdapter(Context context){
        this.context = context;
        attractions = new ArrayList<>();
        selectedAttractions = new ArrayList<>();
    }

    public void addListItemToAdapter(List<Attraction> list) {
        //Add list to current array list of data
        attractions.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }

    public void DeleteAttractionFromSelectedList(Attraction attraction){
        selectedAttractions.remove(attraction);
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

    public List<Attraction> GetSelectedAttractionList(){
        return selectedAttractions;
    }

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
        View v = View.inflate(context, R.layout.item_attraction_list, null);
        TextView itemName = (TextView)v.findViewById(R.id.item_name);
        final CheckBox checkBox = (CheckBox)v.findViewById(R.id.item_checked);

        itemName.setText(attractions.get(position).Name);
        checkBox.setTag(attractions.get(position).Id);
        checkBox.setChecked(attractions.get(position).getSelected());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String attractionId = (String)buttonView.getTag();
                Attraction attraction = GetAttraction(attractionId);
                attraction.setSelected(isChecked);
                attractions.get(position).setSelected(isChecked);
                if(isChecked){
                    selectedAttractions.add(attraction);
                    buttonView.setChecked(isChecked);
                }
                else{
                    DeleteAttractionFromSelectedList(attraction);
                }
            }
        });

        v.setTag(attractions.get(position));
        return v;
    }
}
