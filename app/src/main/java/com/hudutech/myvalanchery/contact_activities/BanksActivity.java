package com.hudutech.myvalanchery.contact_activities;

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
import com.hudutech.myvalanchery.Config;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.adapters.contact_adapter.BankListAdapter;
import com.hudutech.myvalanchery.models.contact_models.Bank;

import java.util.ArrayList;
import java.util.List;

public class BanksActivity extends AppCompatActivity {
    private static final String TAG = "BanksActivity";
    private List<Bank> bankList;
    private BankListAdapter mAdapter;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        bankList = new ArrayList<>();
        mRef = FirebaseFirestore.getInstance().collection("banks");
        mAdapter = new BankListAdapter(this, bankList);

        RecyclerView mRecyclerView = findViewById(R.id.banks_list_recyclerview);
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
                                Bank bank = snapshot.toObject(Bank.class);
                                if (bank != null) {
                                    if (Config.isAdmin(getApplicationContext())) {
                                        bankList.add(bank);
                                    } else if (!Config.isAdmin(BanksActivity.this)) {
                                        if (bank.isValidated()) bankList.add(bank);
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
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(BanksActivity.this, "No data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
