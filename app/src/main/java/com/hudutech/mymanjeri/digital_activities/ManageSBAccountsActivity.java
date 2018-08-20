package com.hudutech.mymanjeri.digital_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.digital_adapters.SBAccountsAdapter;
import com.hudutech.mymanjeri.models.digital_models.SBankAccount;

import java.util.ArrayList;
import java.util.List;

public class ManageSBAccountsActivity extends AppCompatActivity {

    private SBAccountsAdapter mAdapter;
    private List<SBankAccount>
            sBankAccountList;
    private CollectionReference mAccountsRef;
    private ProgressDialog mProgress;

    private TextView tvNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sb_accounts);
        getSupportActionBar().setTitle("SBank Accounts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAccountsRef = FirebaseFirestore.getInstance().collection("sb_accounts");

        mProgress = new ProgressDialog(this);
        sBankAccountList = new ArrayList<>();
        mAdapter = new SBAccountsAdapter(this, sBankAccountList);
        tvNoData = findViewById(R.id.tv_no_accounts);

        RecyclerView mRecyclerView = findViewById(R.id.sb_accounts_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        loadAccounts();
    }

    private void loadAccounts() {
        mProgress.setMessage("Loading please wait...");
        mProgress.show();
        mAccountsRef.orderBy("accountNo", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                SBankAccount account = snapshot.toObject(SBankAccount.class);
                                sBankAccountList.add(account);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
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
