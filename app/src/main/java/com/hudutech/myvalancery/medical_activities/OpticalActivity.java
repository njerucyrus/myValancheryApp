package com.hudutech.myvalancery.medical_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.medical_adapters.OpticalListAdapter;
import com.hudutech.myvalancery.models.medical_models.Optical;

import java.util.ArrayList;
import java.util.List;

public class OpticalActivity extends AppCompatActivity {
    private OpticalListAdapter mAdapter;
    private List<Optical> opticalList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opticals);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRef = FirebaseFirestore.getInstance().collection("opticals");
        mProgress = new ProgressDialog(this);
        opticalList = new ArrayList<>();
        mAdapter = new OpticalListAdapter(this, opticalList);
        RecyclerView mRecyclerView = findViewById(R.id.list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        loadData();
    }

    private void loadData() {
        mProgress.setMessage("Loading...");
        mProgress.show();
        mRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Optical optical = snapshot.toObject(Optical.class);
                                if (optical != null) {
                                    if (Config.isAdmin(getApplicationContext())) {
                                        opticalList.add(optical);
                                    } else if (!Config.isAdmin(getApplicationContext())) {
                                        if (optical.isValidated()) {
                                            opticalList.add(optical);
                                        }
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                    }
                });
    }
}
