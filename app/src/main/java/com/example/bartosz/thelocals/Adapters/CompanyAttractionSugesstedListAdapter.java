package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class CompanyAttractionSugesstedListAdapter extends BaseAdapter{
    private Context context;
    private List<AttractionList> attractionLists;
    private AttractionList selectedAttractionList;

    public CompanyAttractionSugesstedListAdapter(Context context){
        this.context = context;
        attractionLists = new ArrayList<AttractionList>();
    }

    public void AddListItemToAdapter(AttractionList attractionList) {
        //Add list to current array list of data
        attractionLists.add(attractionList);
        //Notify UI
        this.notifyDataSetChanged();
    }

    public void DeleteListItem(AttractionList attractionList){
        attractionLists.remove(attractionList);
        this.notifyDataSetChanged();
    }

    public AttractionList GetAttraqtionList(String id) {
        for(AttractionList attractionList: attractionLists){
            if(attractionList.Id == id){
                return attractionList;
            }
        }
        return null;
    }

    public AttractionList GetSelectedAttractionList(){
        return selectedAttractionList;
    }

    public void ClearList(){
        attractionLists.clear();
    }

    @Override
    public int getCount() {
        return attractionLists.size();
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
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = View.inflate(context, R.layout.item_attraction_suggested_list, null);
        selectedAttractionList = (AttractionList)getItem(position);
        /*
        TextView itemName = (TextView)v.findViewById(R.id.item_name);
        ImageButton itemDetailsButton = (ImageButton)v.findViewById(R.id.item_details);
        ImageButton itemRemoveButton = (ImageButton)v.findViewById(R.id.item_remove);
        if(itemDetailsButton != null){
            itemDetailsButton.setTag(attractionLists.get(position).Id);
            itemDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String attractionListId = (String) v.getTag();
                    //move to another window
                }
            });
        }

        if(itemRemoveButton != null){
            itemRemoveButton.setTag(attractionLists.get(position).Id);
            itemRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String attractionListId = (String) v.getTag();
                    AttractionList attractionList = GetAttraqtionList(attractionListId);
                    DeleteListItem(attractionList);
                    //delete attraction List from database
                }
            });
        }*/
        return v;
    }
}
