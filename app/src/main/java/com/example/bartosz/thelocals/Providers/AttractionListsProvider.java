package com.example.bartosz.thelocals.Providers;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.example.bartosz.thelocals.Models.Company;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttractionListsProvider {
    private FirebaseDatabase firebaseDatabase;
    private String companyCollectionName;
    private String collectionName;
    private String companyId;

    public AttractionListsProvider(){
        companyCollectionName = "Companies";
        collectionName = "AttractionList";
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public AttractionListsProvider(String companyId){
        companyCollectionName = "Companies";
        collectionName = "AttractionList";
        this.companyId = companyId;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public Task<ArrayList<AttractionList>> GetAttractionListsForCompany(){
        final TaskCompletionSource<ArrayList<AttractionList>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<AttractionList> list = new ArrayList<>();
        final Query query = firebaseDatabase.getReference().child(collectionName).orderByChild("CompanyId").equalTo(companyId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        GenericTypeIndicator<AttractionList> type = new GenericTypeIndicator<AttractionList>() {};
                        AttractionList attractionList = snapshot.getValue(AttractionList.class);
                        if(attractionList != null){
                            list.add(attractionList);
                        }
                        //list.add(attractionList);
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

    public Task<Company> GetCompany(){
        final TaskCompletionSource<Company> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference reference = firebaseDatabase.getReference().child(companyCollectionName).child(companyId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                taskCompletionSource.setResult(company);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<AttractionList> GetAttractionListById(String id){
        final TaskCompletionSource<AttractionList> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference reference = firebaseDatabase.getReference().child(collectionName).child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AttractionList attractionList = dataSnapshot.getValue(AttractionList.class);
                if(attractionList != null){
                    attractionList.Id = dataSnapshot.getKey();
                }
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
