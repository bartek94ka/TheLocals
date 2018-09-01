package com.example.bartosz.thelocals.Providers;

import com.example.bartosz.thelocals.Models.Attraction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import bolts.TaskCompletionSource;

public class ProvinceProvider {

    private FirebaseDatabase firebaseDatabase;
    private String collectionName;

    public ProvinceProvider(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        collectionName = "Provinces";
    }

    public Task<ArrayList<String>> GetAllProvinces(){
        final TaskCompletionSource<ArrayList<String>> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference reference = firebaseDatabase.getReference(collectionName);
        final ArrayList<String> provinces = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot child: dataSnapshot.getChildren()){
                    String province  = child.getValue(String.class);
                    provinces.add(province);
                }
                taskCompletionSource.setResult(provinces);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }
}
