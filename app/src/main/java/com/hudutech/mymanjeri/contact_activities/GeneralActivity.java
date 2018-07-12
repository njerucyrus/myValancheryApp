package com.hudutech.mymanjeri.contact_activities;

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
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.contact_adapter.GeneralListAdapter;
import com.hudutech.mymanjeri.models.contact_models.General;

import java.util.ArrayList;
import java.util.List;

public class GeneralActivity extends AppCompatActivity {
    private static final String TAG = "GeneralActivity";
    private List<General> generalList;
    private GeneralListAdapter mAdapter;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        generalList = new ArrayList<>();
        mRef = FirebaseFirestore.getInstance().collection("general");
        mAdapter = new GeneralListAdapter(this, generalList);

        RecyclerView mRecyclerView = findViewById(R.id.general_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        loadData();

    }

    private void loadData() {
        mProgress.setMessage("loading...");
        mProgress.show();
        mRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                General general = snapshot.toObject(General.class);
                                if (general != null) {
                                    if (Config.isAdmin(getApplicationContext())) {
                                        generalList.add(general);
                                    } else if (!Config.isAdmin(GeneralActivity.this)) {
                                        if (general.isValidated()) generalList.add(general);
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
                        Log.e(TAG, "onFailure: "+e.getMessage());
                        Toast.makeText(GeneralActivity.this, "No data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
