package com.hudutech.mymanjeri.majery_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.majery_adapters.EducationAdapter;
import com.hudutech.mymanjeri.models.majery_models.Education;

import java.util.ArrayList;
import java.util.List;

public class EducationActivity extends AppCompatActivity {
    private static final String TAG = "EducationActivity";
    private EducationAdapter mAdapter;
    private List<Education> educationList;
    private CollectionReference mEducationRef;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEducationRef = FirebaseFirestore.getInstance().collection("education");
        mProgress = new ProgressDialog(this);

        educationList = new ArrayList<>();
        mAdapter = new EducationAdapter(this, educationList);
        RecyclerView mRecyclerView = findViewById(R.id.education_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        fetchData();
    }

    private void fetchData() {
        mProgress.setMessage("Loading please wait...");
        mProgress.show();
        mEducationRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Education education = snapshot.toObject(Education.class);
                            educationList.add(education);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }
}
