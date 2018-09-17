package com.example.bartosz.thelocals.Managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.bartosz.thelocals.Models.Company;
import com.example.bartosz.thelocals.Models.Guide;
import com.example.bartosz.thelocals.R;
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

public class GuideManager {

    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private String collectionName;


    public GuideManager(Context context){
        this.context = context;
        collectionName = context.getString(R.string.collection_guides);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public Task<Guide> GetGuide(String id){
        final TaskCompletionSource<Guide> taskCompletionSource = new TaskCompletionSource<>();
        final DatabaseReference localReference = firebaseDatabase.getReference(collectionName).child(id);
        localReference.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Guide guide = dataSnapshot.getValue(Guide.class);
                        taskCompletionSource.setResult(guide);
                        localReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return taskCompletionSource.getTask();
    }

    public void UpdateFirebaseGuideData(String id, Guide guide){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put(id, guide);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(collectionName);
        reference.updateChildren(taskMap);
    }

    public void AddGuide(Guide guide){
        try {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Dodawanie przewodnika...");
            progressDialog.show();
            firebaseDatabase.getReference().child(collectionName).child(guide.Id).setValue(guide).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d("TAG", "AddGuide:success");
                                Toast.makeText(context, "Dodano przewodnika", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(context, "Nie udało się dodać przewodnika. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }
                    });
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public void DeleteAttractionListFromCompany(final String companyId, final String attractionListId){
        GetGuide(companyId).addOnCompleteListener(new OnCompleteListener<Guide>() {
            @Override
            public void onComplete(@NonNull Task<Guide> task) {
                if(task.isSuccessful()){
                    Guide guide = task.getResult();
                    /*
                    if(guide.AttractionSuggestedList != null){
                        company.AttractionSuggestedList.remove(attractionListId);
                        UpdateFirebaseComapnyData(companyId, company);
                    }
                    */
                }
            }
        });
    }

    public Task<ArrayList<Guide>> GetAllGuides(){
        final TaskCompletionSource<ArrayList<Guide>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<Guide> list = new ArrayList<>();
        final DatabaseReference localReference = firebaseDatabase.getReference().child(collectionName);
        /*
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Pobieranie listy przewodników...");
        progressDialog.show();
*/
        localReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Guide guide = snapshot.getValue(Guide.class);
                        if(guide != null){
                            list.add(guide);
                        }
                    }
                    localReference.removeEventListener(this);
                }
                taskCompletionSource.setResult(list);
  //              progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<ArrayList<Guide>> GetGuidesAddedByUserId(String userId){
        final TaskCompletionSource<ArrayList<Guide>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<Guide> list = new ArrayList<>();
        final Query query = firebaseDatabase.getReference().child(collectionName).orderByChild("UserId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Guide guide = snapshot.getValue(Guide.class);
                        if(guide != null){
                            list.add(guide);
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
