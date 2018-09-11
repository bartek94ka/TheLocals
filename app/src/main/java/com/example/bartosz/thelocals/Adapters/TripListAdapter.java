package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Listeners.IGuidePassListener;
import com.example.bartosz.thelocals.Managers.AttractionListManager;
import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Managers.GuideManager;
import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class TripListAdapter extends BaseAdapter{
    private Context context;
    private List<AttractionList> attractionLists;
    private AttractionListManager attractionListManager;
    private GuideManager guideManager;
    private AttractionList selectedAttractionList;
    private String guideId;
    private IGuidePassListener mListener;

    public TripListAdapter(Context context){
        this.context = context;
        mListener = (IGuidePassListener) context;
        attractionListManager = new AttractionListManager(context);
        attractionLists = new ArrayList<AttractionList>();
        guideManager = new GuideManager(context);
    }

    public void AddListItemToAdapter(AttractionList attractionList) {
        //Add list to current array list of data
        attractionLists.add(attractionList);
        //Notify UI
        this.notifyDataSetChanged();
    }

    public void AddAllItemsToAdapter(List<AttractionList> attractionLists){
        this.attractionLists.addAll(attractionLists);
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

    public void SetGuideId(String guideId){
        this.guideId = new String();
        this.guideId = guideId;
    }

    public AttractionList GetSelectedAttractionList(){
        return selectedAttractionList;
    }

    public void UpdateAttractionListByPosition(AttractionList attractionList, int position){
        attractionLists.set(position, attractionList);
        attractionListManager.UpdateFirebaseAttractionList(attractionList.Id, attractionList);
    }

    public AttractionList GetAttractionByPosition(int position){
        return attractionLists.get(position);
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
        View v = View.inflate(context, R.layout.item_company_attraction_suggested_list, null);
        AttractionList attractionList = GetAttractionByPosition(position);

        TextView itemName = v.findViewById(R.id.item_name);
        itemName.setText(attractionList.Name);
        ImageButton itemEditAttractionList = v.findViewById(R.id.item_edit_attraction_list);
        ImageButton itemRemoveButton = v.findViewById(R.id.item_remove);
        if(itemEditAttractionList != null){
            itemEditAttractionList.setTag(attractionLists.get(position).Id);
            itemEditAttractionList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String attractionListId = (String) v.getTag();
                    mListener.PassAttractionListIdToGuideTripDetails(attractionListId);
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
                    attractionListManager.RemoveAttractionList(attractionListId);
                }
            });
        }

        itemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AttractionList attractionList = GetAttractionByPosition(position);
                attractionList.Name = s.toString();
                UpdateAttractionListByPosition(attractionList, position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
}
