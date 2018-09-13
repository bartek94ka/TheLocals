package com.example.bartosz.thelocals.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.R;

import java.util.ArrayList;
import java.util.List;

public class EditCompanyAdapter extends BaseAdapter {
    private Context context;
    private IComapnyPassListener listener;
    private List<Company> companyList;

    public EditCompanyAdapter(Context context){
        this.context = context;
        listener = (IComapnyPassListener) context;
        companyList = new ArrayList<>();
    }

    public void AddAllCompaniesToAdapter(List<Company> companies){
        companyList.addAll(companies);
        this.notifyDataSetChanged();
    }

    public void ClearList(){
        this.companyList.clear();
    }

    @Override
    public int getCount() {
        return companyList.size();
    }

    @Override
    public Object getItem(int i) {
        return companyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_edit_company, null);
        final Company company = (Company)getItem(i);
        TextView itemName = v.findViewById(R.id.item_name);
        Button buttonEdit = v.findViewById(R.id.item_Edit);
        itemName.setText(company.Name);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.PassCompanyIdToCompanyEdit(company.Id);
            }
        });
        return v;
    }
}
