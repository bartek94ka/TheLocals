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

public class CompanyListAdapter extends BaseAdapter{

    private IComapnyPassListener comapnyPassListener;
    private Context context;
    private List<Company> companyList;

    public CompanyListAdapter(Context context){
        this.context = context;
        comapnyPassListener = (IComapnyPassListener) context;
        companyList = new ArrayList<>();
    }

    public void AddAllCompaniesToAdapter(List<Company> companies){
        this.companyList.addAll(companies);
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
    public Object getItem(int position) {
        return companyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_company_list, null);
        final Company company = (Company)getItem(position);
        TextView itemName = view.findViewById(R.id.item_name);
        Button buttonDetails = view.findViewById(R.id.item_Details);
        itemName.setText(company.Name);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comapnyPassListener.PassCompanyIdToCompanyDetails(company.Id);
            }
        });
        return view;
    }
}
