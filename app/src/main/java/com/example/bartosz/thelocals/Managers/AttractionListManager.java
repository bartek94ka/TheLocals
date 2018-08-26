package com.example.bartosz.thelocals.Managers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.AttractionList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AttractionListManager {

    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private String collectionName = "AttractionLists";

    public AttractionListManager(Context context){
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void UpdateFirebaseAttractionList(String id, AttractionList attractionList){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put(id, attractionList);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(collectionName);
        reference.updateChildren(taskMap);
    }


    public void AddAttractionList(AttractionList attractionList){
        try{
            firebaseDatabase.getReference().child(collectionName).child(attractionList.Id).setValue(attractionList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("TAG", "AddAttractionList:success");
                        Toast.makeText(context, "attraction list created successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Could not created attraction list. Please try again", Toast.LENGTH_SHORT).show();
                    }
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
}
