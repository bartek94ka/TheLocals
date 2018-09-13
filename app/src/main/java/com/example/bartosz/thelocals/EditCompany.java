package com.example.bartosz.thelocals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bartosz.thelocals.Listeners.IComapnyPassListener;
import com.example.bartosz.thelocals.Managers.CompanyManager;
import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class EditCompany extends Fragment {

    private CompanyManager companyManager;
    private IComapnyPassListener listener;

    private EditText etName;
    private EditText etAddress;
    private EditText etPhoneNumber;
    private EditText etEmail;
    private EditText etLogoUrl;
    private EditText etUrlAddress;
    private EditText etDescription;

    private Button buttonEdit;
    private String companyId;
    private Company company;

    public EditCompany() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_company, container, false);
        buttonEdit = view.findViewById(R.id.nextButton);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateCompanyData();
                listener.PassComapnyIdToComapnyAttractionSugesstedList(companyId);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragment_edit_company));
        companyManager = new CompanyManager(getContext());
        SetPropertiesFromArguments();
        etName = view.findViewById(R.id.companyName);
        etAddress = view.findViewById(R.id.companyAddress);
        etPhoneNumber = view.findViewById(R.id.companyPhoneNumber);
        etEmail = view.findViewById(R.id.companyEmail);
        etLogoUrl = view.findViewById(R.id.companyLogoUrl);
        etUrlAddress = view.findViewById(R.id.companyUrlAddress);
        etDescription = view.findViewById(R.id.companyDescription);
        FillCompanyData();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IComapnyPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IComapnyPassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void FillCompanyData(){
        companyManager.GetCompanyData(companyId).addOnCompleteListener(new OnCompleteListener<Company>() {
            @Override
            public void onComplete(@NonNull Task<Company> task) {
                if(task.isSuccessful()){
                    company = task.getResult();
                    etAddress.setText(company.Address);
                    etDescription.setText(company.Description);
                    etEmail.setText(company.Email);
                    etLogoUrl.setText(company.LogoUrl);
                    etName.setText(company.Name);
                    etPhoneNumber.setText(company.PhoneNumber);
                    etUrlAddress.setText(company.UrlAddress);
                }
            }
        });
    }

    private void UpdateCompanyData(){
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String logoUrl = etLogoUrl.getText().toString().trim();
        String urlAddress = etUrlAddress.getText().toString().trim();
        String decription = etDescription.getText().toString().trim();
        company.Name = name;
        company.Address = address;
        company.Description = decription;
        company.PhoneNumber = phoneNumber;
        company.Email = email;
        company.LogoUrl = logoUrl;
        company.UrlAddress = urlAddress;
        companyManager.UpdateFirebaseComapnyData(companyId, company);
    }

    private void SetPropertiesFromArguments(){
        Bundle args = getArguments();
        if(args != null){
            companyId = (String)args.get("companyId");
        }
    }
}
