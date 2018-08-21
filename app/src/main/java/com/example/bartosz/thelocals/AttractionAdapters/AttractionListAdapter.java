package com.example.bartosz.thelocals.AttractionAdapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.bartosz.thelocals.Models.Attraction;

import java.util.ArrayList;
import java.util.List;

public class AttractionListAdapter extends BaseAdapter{

    private List<Attraction> attractions;

    public AttractionListAdapter(){
        attractions = new ArrayList<>();
    }

    public void addListItemToAdapter(List<Attraction> list) {
        //Add list to current array list of data
        attractions.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }

    public void deleteListItem(Attraction attraction){
        attractions.remove(attraction);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
