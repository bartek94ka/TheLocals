package com.example.bartosz.thelocals;


import android.content.Context;
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
import com.example.bartosz.thelocals.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class AddCompany extends Fragment implements View.OnClickListener{

    private CompanyManager companyManager;
    private IComapnyPassListener mListener;

    private EditText etName;
    private EditText etAddress;
    private EditText etPhoneNumber;
    private EditText etEmail;
    private EditText etLogoUrl;
    private EditText etUrlAddress;
    private EditText etDescription;

    private Button buttonNext;

    public AddCompany() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_company, container, false);

        buttonNext = view.findViewById(R.id.nextButton);
        buttonNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).SetActionBarTitle(getString(R.string.fragmnet_add_company));
        companyManager = new CompanyManager(getContext());

        etName = view.findViewById(R.id.companyName);
        etAddress = view.findViewById(R.id.companyAddress);
        etPhoneNumber = view.findViewById(R.id.companyPhoneNumber);
        etEmail = view.findViewById(R.id.companyEmail);
        etLogoUrl = view.findViewById(R.id.companyLogoUrl);
        etUrlAddress = view.findViewById(R.id.companyUrlAddress);
        etDescription = view.findViewById(R.id.companyDescription);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
            {
                AddCompany();
                break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (IComapnyPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IComapnyPassListener");
        }
    }

    private void AddCompany(){
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String logoUrl = etLogoUrl.getText().toString().trim();
        String urlAddress = etUrlAddress.getText().toString().trim();
        String decription = etDescription.getText().toString().trim();

        Company company = new Company(name, address, decription, urlAddress, logoUrl, phoneNumber, email, FirebaseAuth.getInstance().getUid());

        String id = UUID.randomUUID().toString();
        company.Id = id;
        try{
            companyManager.AddComapny(company);
            ResetTextFields();
        }catch(Exception ex){
            System.out.print(ex.getMessage());
        }
        mListener.PassComapnyIdToComapnyAttractionSugesstedList(id);
    }

    private void ResetTextFields(){
        etName.setText("");
        etAddress.setText("");
        etPhoneNumber.setText("");
        etEmail.setText("");
    }
}
