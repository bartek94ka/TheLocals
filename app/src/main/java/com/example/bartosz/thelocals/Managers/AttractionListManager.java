package com.example.bartosz.thelocals.Managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.Attraction;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttractionListManager {

    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private String collectionName = "AttractionList";

    public AttractionListManager(Context context){
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void UpdateFirebaseAttractionList(String id, AttractionList attractionList){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        HashMap<String, Attraction> attractions = attractionList.Attractions;
        attractionList.Attractions = null;
        taskMap.put(id, attractionList);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(collectionName);
        reference.updateChildren(taskMap);
        taskMap.clear();
        attractionList.Attractions = attractions;
        taskMap.put(id, attractionList);
        reference.updateChildren(taskMap);
    }

    public void AddAttractionList(AttractionList attractionList){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Dodawanie atrakcji...");
            progressDialog.show();
            firebaseDatabase.getReference().child(collectionName).child(attractionList.Id).setValue(attractionList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("TAG", "AddAttractionList:success");
                        //Toast.makeText(context, "attraction list created successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(context, "Could not created attraction list. Please try again", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.hide();
                }
            });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public void RemoveAttractionList(String id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(collectionName);
        reference.child(id).removeValue();
    }

    public Task<AttractionList> GetAttractionListById(String id){
        final TaskCompletionSource<AttractionList> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference localReference = firebaseDatabase.getReference(collectionName).child(id);
        localReference.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AttractionList company = dataSnapshot.getValue(AttractionList.class);
                        taskCompletionSource.setResult(company);
                        localReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return taskCompletionSource.getTask();
    }

    public Task<ArrayList<AttractionList>> GetAllAttracionLists(){
        final TaskCompletionSource<ArrayList<AttractionList>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<AttractionList> list = new ArrayList<>();
        final DatabaseReference localReference = firebaseDatabase.getReference().child(collectionName);
        localReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        AttractionList attractionList = snapshot.getValue(AttractionList.class);
                        if(attractionList != null){
                            list.add(attractionList);
                        }
                    }
                    localReference.removeEventListener(this);
                }
                taskCompletionSource.setResult(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }
}
