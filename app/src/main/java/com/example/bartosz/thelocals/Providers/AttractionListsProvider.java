package com.example.bartosz.thelocals.Providers;

import android.support.annotation.NonNull;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttractionListsProvider {
    private FirebaseDatabase firebaseDatabase;
    private String companyCollectionName;
    private String collectionName;
    private String companyId;

    public AttractionListsProvider(String companyId){
        companyCollectionName = "Companies";
        collectionName = "AttractionsList";
        this.companyId = companyId;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public Task<ArrayList<AttractionList>> GetAttractionListsForCompany(){
        final TaskCompletionSource<ArrayList<AttractionList>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<AttractionList> list = new ArrayList<>();
        final DatabaseReference reference = firebaseDatabase.getReference().child(companyCollectionName).child(companyId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company.AttractionSuggestedList != null){
                    for (String attractionListId: company.AttractionSuggestedList) {
                        GetAttractionListById(attractionListId).addOnCompleteListener(new OnCompleteListener<AttractionList>() {
                            @Override
                            public void onComplete(@NonNull Task<AttractionList> task) {
                                if(task.isSuccessful()){
                                    AttractionList attractionList = (AttractionList)task.getResult();
                                    list.add(attractionList);
                                }
                            }
                        });
                    }
                }
                taskCompletionSource.setResult(list);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }

    private Task<AttractionList> GetAttractionListById(String id){
        final TaskCompletionSource<AttractionList> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference reference = firebaseDatabase.getReference().child(collectionName).child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AttractionList attractionList = dataSnapshot.getValue(AttractionList.class);
                attractionList.Id = companyId;
                taskCompletionSource.setResult(attractionList);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }
}
