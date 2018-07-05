package com.hudutech.mymanjeri.majery_activities;



import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.BloodDonorAdapter;
import com.hudutech.mymanjeri.models.majery_models.BloodDonor;

import java.util.ArrayList;
import java.util.List;

public class BloodGroupListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<BloodDonor> donorList;
    private BloodDonorAdapter mAdapter;
    private FirebaseFirestore db;
    private CollectionReference mRootRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group_list);
        ActionBar ab = getSupportActionBar();
        String bloodGroup = getIntent().getStringExtra("bloodGroup");
        ab.setTitle(bloodGroup+" Blood Banks.");
        ab.setDisplayHomeAsUpEnabled(true);

        donorList = new ArrayList<>();
        mAdapter = new BloodDonorAdapter(this, donorList);
        mRecyclerView = findViewById(R.id.blood_donors_recylerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mProgress = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        mRootRef = db.collection("donors");

        fetchDonors(bloodGroup);

    }

    private void fetchDonors(String bloodGroup) {
        mProgress.setMessage("Loading...");
        mProgress.show();
        mRootRef.whereEqualTo("bloodGroup", bloodGroup)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                BloodDonor donor = snapshot.toObject(BloodDonor.class);
                                donorList.add(donor);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        TextView noData = findViewById(R.id.tv_no_banks);
                        if (donorList.size() == 0) {

                            noData.setVisibility(View.VISIBLE);
                        }else {
                            noData.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TextView noData = findViewById(R.id.tv_no_banks);
                        if (donorList.size() == 0) {

                            noData.setVisibility(View.VISIBLE);
                        }else {
                            noData.setVisibility(View.GONE);
                        }

                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(BloodGroupListActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
