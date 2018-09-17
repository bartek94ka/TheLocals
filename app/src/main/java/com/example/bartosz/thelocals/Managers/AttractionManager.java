package com.example.bartosz.thelocals.Managers;

import android.content.Context;

import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AttractionManager {
    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private String collectionName;

    public AttractionManager(Context context){
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        collectionName = context.getString(R.string.collection_attractions);
    }

    public void UpdateAttractionVisitsCounter(String id, Attraction attraction){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        //taskMap.put(id, attraction);
        taskMap.put("VisitsCounter", attraction.VisitsCounter);
        //collectionName = collectionName + "\\" + attraction.Province;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(collectionName).child(attraction.Province).child(attraction.Id);

        reference.updateChildren(taskMap);
    }
}
