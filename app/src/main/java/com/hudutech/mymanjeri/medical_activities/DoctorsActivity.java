package com.hudutech.mymanjeri.medical_activities;

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
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.medical_adapters.DoctorListAdapter;
import com.hudutech.mymanjeri.models.medical_models.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity {
    private static final String TAG = "DoctorsActivity";
    private DoctorListAdapter mAdapter;
    private List<Doctor> doctorList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef = FirebaseFirestore.getInstance().collection("doctors");
        mProgress = new ProgressDialog(this);
        doctorList = new ArrayList<>();
        mAdapter = new DoctorListAdapter(this, doctorList);
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
                                Doctor doctor = snapshot.toObject(Doctor.class);
                                if (doctor != null) {
                                    if (Config.isAdmin(getApplicationContext())) {
                                        doctorList.add(doctor);
                                    } else if (!Config.isAdmin(getApplicationContext())) {
                                        if (doctor.isValidated()) {
                                            doctorList.add(doctor);
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
