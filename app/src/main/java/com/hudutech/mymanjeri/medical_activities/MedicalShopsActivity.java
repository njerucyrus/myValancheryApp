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
import com.hudutech.mymanjeri.adapters.medical_adapters.MedicalShopListAdapter;
import com.hudutech.mymanjeri.models.medical_models.MedicalShop;
import com.hudutech.mymanjeri.models.medical_models.MedicalShop;

import java.util.ArrayList;
import java.util.List;

public class MedicalShopsActivity extends AppCompatActivity {

    private MedicalShopListAdapter mAdapter;
    private List<MedicalShop> medicalShopList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_shops);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef = FirebaseFirestore.getInstance().collection("medical_shops");
        mProgress = new ProgressDialog(this);
        medicalShopList = new ArrayList<>();
        mAdapter = new MedicalShopListAdapter(this, medicalShopList);
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
                            for(DocumentSnapshot snapshot: snapshots.getDocuments()) {
                                MedicalShop medicalShop = snapshot.toObject(MedicalShop.class);
                                if (medicalShop != null) {
                                    if (Config.isAdmin(getApplicationContext())) {
                                        medicalShopList.add(medicalShop);
                                    } else if (!Config.isAdmin(getApplicationContext())){
                                        if (medicalShop.isValidated()){
                                            medicalShopList.add(medicalShop);
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
