package com.example.bartosz.thelocals.Providers;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AttractionListsProvider {
    private FirebaseDatabase _firebaseDatabase;
    private String _companyId;

    public AttractionListsProvider(String companyId){
        _companyId = companyId;
        _firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public Task<ArrayList<AttractionList>> GetAttractionListsForCompany(){
        final TaskCompletionSource<ArrayList<AttractionList>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<AttractionList> list = new ArrayList<>();
        final DatabaseReference reference = _firebaseDatabase.getReference().child("Company");
        return null;
    }
}
