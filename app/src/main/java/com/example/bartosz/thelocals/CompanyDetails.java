package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Managers.ImageManager;
import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

public class CompanyDetails extends Fragment {

    private View view;
    private ImageView companyLogo;
    private TextView comapnyName;
    private TextView companyEmail;
    private TextView companyPhoneNumber;
    private TextView companyAddress;
    private TextView companyDescription;
    private TextView companyUrlAddress;

    private CompanyManager companyManager;
    private String companyId;

    public CompanyDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_company_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_company_detials));
        SetPropertiesFromArguments();
        companyManager = new CompanyManager(getContext());
        InitializeVeribles();
        SetCompanyProperties();
    }

    private void InitializeVeribles(){
        companyLogo = view.findViewById(R.id.iMageview);
        comapnyName = view.findViewById(R.id.companyName);
        companyEmail = view.findViewById(R.id.companyEmail);
        companyAddress = view.findViewById(R.id.companyAddress);
        companyPhoneNumber = view.findViewById(R.id.companyPhoneNumber);
        companyUrlAddress = view.findViewById(R.id.companyUrlAddress);
        companyDescription = view.findViewById(R.id.companyDescription);
    }

    private void SetCompanyProperties(){
        companyManager.GetCompanyData(companyId).
                addOnCompleteListener(new OnCompleteListener<Company>() {
                    @Override
                    public void onComplete(@NonNull Task<Company> task) {
                        if(task.isSuccessful()){
                            Company company = task.getResult();

                            comapnyName.setText(company.Name);
                            companyEmail.setText(company.Email);
                            companyAddress.setText(company.Address);
                            companyPhoneNumber.setText(company.PhoneNumber);
                            companyUrlAddress.setText(company.UrlAddress);
                            companyDescription.setText(company.Description);
                            if(!company.LogoUrl.isEmpty()){
                                new ImageManager(companyLogo).execute(company.LogoUrl);
                            }
                        }
                    }
                });
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            companyId = (String)args.get("companyId");
        }
    }
}
