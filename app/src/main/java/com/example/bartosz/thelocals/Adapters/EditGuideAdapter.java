package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Listeners.IGuidePassListener;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Models.Guide;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class EditGuideAdapter extends BaseAdapter {
    private Context context;
    private IGuidePassListener listener;
    private List<Guide> guideList;

    public EditGuideAdapter(Context context){
        this.context = context;
        listener = (IGuidePassListener) context;
        guideList = new ArrayList<>();
    }

    public void AddAllCompaniesToAdapter(List<Guide> guides){
        guideList.addAll(guides);
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
    public Object getItem(int i) {
        return guideList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_edit_company, null);
        final Guide guide = (Guide)getItem(i);
        TextView itemName = v.findViewById(R.id.item_name);
        Button buttonEdit = v.findViewById(R.id.item_Edit);
        itemName.setText(guide.FirstName + " " + guide.LastName);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.PassGuideIdToGuideEdit(guide.Id);
            }
        });
        return v;
    }
}
