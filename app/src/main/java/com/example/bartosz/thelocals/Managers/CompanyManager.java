package com.example.bartosz.thelocals.Managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyManager {

    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private String collectionName = "Companies";


    public CompanyManager(Context context){
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public com.google.android.gms.tasks.Task<Company> GetCompanyData(String id){
        final TaskCompletionSource<Company> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference localReference = firebaseDatabase.getReference(collectionName).child(id);
        localReference.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Company company = dataSnapshot.getValue(Company.class);
                        taskCompletionSource.setResult(company);
                        localReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return taskCompletionSource.getTask();
    }

    public void UpdateFirebaseComapnyData(String id, Company comapny){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put(id, comapny);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(collectionName);
        reference.updateChildren(taskMap);
    }

    public void AddComapny(Company company){
        try {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Dodawanie firmy...");
            progressDialog.show();
            firebaseDatabase.getReference().child(collectionName).child(company.Id).setValue(company).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d("TAG", "AddComapny:success");
                                Toast.makeText(context, "Pomyślnie dodano firmę", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(context, "Nie udało się dodać firmy. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public void DeleteAttractionListFromCompany(final String companyId, final String attractionListId){
        GetCompanyData(companyId).addOnCompleteListener(new OnCompleteListener<Company>() {
            @Override
            public void onComplete(@NonNull Task<Company> task) {
                if(task.isSuccessful()){
                    Company company = task.getResult();
                    if(company.AttractionSuggestedList != null){
                        company.AttractionSuggestedList.remove(attractionListId);
                        UpdateFirebaseComapnyData(companyId, company);
                    }
                }
            }
        });
    }

    public Task<ArrayList<Company>> GetAllCompanies(){
        final TaskCompletionSource<ArrayList<Company>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<Company> list = new ArrayList<>();
        final DatabaseReference localReference = firebaseDatabase.getReference().child(collectionName);
        /*
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Ładowanie listy biur...");
        progressDialog.show();
        */
        localReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Company company = snapshot.getValue(Company.class);
                        if(company != null){
                            list.add(company);
                        }
                    }
                    localReference.removeEventListener(this);
                }
                taskCompletionSource.setResult(list);
                //progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<ArrayList<Company>> GetCompaniesAddedByUserId(String userId){
        final TaskCompletionSource<ArrayList<Company>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<Company> list = new ArrayList<>();
        final Query query = firebaseDatabase.getReference().child(collectionName).orderByChild("UserId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Company company = snapshot.getValue(Company.class);
                        if(company != null){
                            list.add(company);
                        }
                    }
                }
                taskCompletionSource.setResult(list);
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }
}
