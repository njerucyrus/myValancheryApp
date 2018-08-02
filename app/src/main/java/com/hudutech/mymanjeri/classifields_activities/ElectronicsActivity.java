package com.hudutech.mymanjeri.classifields_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.classifields_adapters.ElectronicsListAdapter;
import com.hudutech.mymanjeri.models.classifields_models.Electronic;

import java.util.ArrayList;
import java.util.List;

public class ElectronicsActivity extends AppCompatActivity {

    private static final String TAG = "ElectronicsActivity";
    private ElectronicsListAdapter mAdapter;
    private List<Electronic> electronicList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRef = FirebaseFirestore.getInstance().collection("classifields_electronics");
        electronicList = new ArrayList<>();
        mAdapter = new ElectronicsListAdapter(this, electronicList);
        mProgress = new ProgressDialog(this);
        RecyclerView mRecyclerView = findViewById(R.id.admin_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadData();
    }


    private void loadData() {
        mProgress.setMessage("Loading...");
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.show();

        mRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (documentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                                Electronic electronic = snapshot.toObject(Electronic.class);
                                electronicList.add(electronic);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ElectronicsActivity.this, "No Data found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(ElectronicsActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
