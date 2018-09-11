package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IGuidePassListener;
import com.example.bartosz.thelocals.Models.Guide;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class GuideListAdapter extends BaseAdapter{

    private IGuidePassListener guidePassListener;
    private Context context;
    private List<Guide> guideList;

    public GuideListAdapter(Context context){
        this.context = context;
        guidePassListener = (IGuidePassListener) context;
        guideList = new ArrayList<>();
    }

    public void AddAllGuidesToAdapter(List<Guide> guides){
        this.guideList.addAll(guides);
        this.notifyDataSetChanged();
    }

    public void ClearList(){
        this.guideList.clear();
    }

    @Override
    public int getCount() {
        return guideList.size();
    }

    @Override
    public Object getItem(int position) {
        return guideList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_guide_list, null);
        final Guide guide = (Guide)getItem(position);
        TextView itemName = view.findViewById(R.id.item_name);
        Button buttonDetails = view.findViewById(R.id.item_Details);
        itemName.setText(guide.FirstName + " " + guide.LastName);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            guidePassListener.PassGuideIdToGuideDetails(guide.Id);
                //comapnyPassListener.PassCompanyIdToCompanyDetails(company.Id);
            }
        });
        return view;
    }
}
