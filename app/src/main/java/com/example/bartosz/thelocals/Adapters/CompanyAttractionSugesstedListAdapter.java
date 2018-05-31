package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.R;

import java.util.List;

public class CompanyAttractionSugesstedListAdapter extends BaseAdapter{
    private Context _context;
    private List<AttractionList> _attractionLists;

    public CompanyAttractionSugesstedListAdapter(Context context, List<AttractionList> attractionLists){
        _context = context;
        _attractionLists = attractionLists;
    }

    public void AddListItemToAdapter(List<AttractionList> attractionList) {
        //Add list to current array list of data
        _attractionLists.addAll(attractionList);
        //Notify UI
        this.notifyDataSetChanged();
    }

    public void DeleteListItem(AttractionList attractionList){
        _attractionLists.remove(attractionList);
        this.notifyDataSetChanged();
    }

    public AttractionList GetAttraqtionList(String id) {
        for(AttractionList attractionList: _attractionLists){
            if(attractionList.Id == id){
                return attractionList;
            }
        }
        return null;
    }

    public void ClearList(){
        _attractionLists.clear();
    }

    @Override
    public int getCount() {
        return _attractionLists.size();
    }

    @Override
    public Object getItem(int position) {
        return _attractionLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = View.inflate(_context, R.layout.item_attraction_suggested_list, null);
        TextView itemName = (TextView)v.findViewById(R.id.item_name);
        ImageButton itemDetailsButton = (ImageButton)v.findViewById(R.id.item_details);
        ImageButton itemRemoveButton = (ImageButton)v.findViewById(R.id.item_remove);
        if(itemDetailsButton != null){
            itemDetailsButton.setTag(_attractionLists.get(position).Id);
            itemDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String attractionListId = (String) v.getTag();
                    //move to another window
                }
            });
        }

        if(itemRemoveButton != null){
            itemRemoveButton.setTag(_attractionLists.get(position).Id);
            itemRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String attractionListId = (String) v.getTag();
                    AttractionList attractionList = GetAttraqtionList(attractionListId);
                    DeleteListItem(attractionList);
                    //delete attraction List from database
                }
            });
        }
        return v;
    }
}
