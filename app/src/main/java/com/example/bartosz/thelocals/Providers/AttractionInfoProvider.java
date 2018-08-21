package com.example.bartosz.thelocals.Providers;

import com.example.bartosz.thelocals.Models.Attraction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import bolts.TaskCompletionSource;

public class AttractionInfoProvider {
    private FirebaseDatabase firebaseDatabase;
    private String collectionName;
    private String provinceName;

    public AttractionInfoProvider(String provinceName){
        firebaseDatabase = FirebaseDatabase.getInstance();
        collectionName = "Attractions2";
        this.provinceName = provinceName;
    }

    public Task<Attraction> GetAttractionById(String attractionId){
        final TaskCompletionSource<Attraction> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference reference = firebaseDatabase.getReference(collectionName).child(attractionId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Attraction attraction = dataSnapshot.getValue(Attraction.class);
                attraction.Id =dataSnapshot.getKey();
                taskCompletionSource.setResult(attraction);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<ArrayList<Attraction>> GetAllAttractions(){
        final TaskCompletionSource<ArrayList<Attraction>> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference reference = firebaseDatabase.getReference(collectionName  + "/" + provinceName);
        final ArrayList<Attraction> list = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Attraction attraction = child.getValue(Attraction.class);
                    attraction.Id = child.getKey();
                    //TODO: check is validated
                    list.add(attraction);
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
}
