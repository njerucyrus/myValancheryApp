package com.hudutech.mymanjeri.digital_activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.digital_adapters.TransactionAdapter;
import com.hudutech.mymanjeri.models.digital_models.TransactionRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawsFragment extends Fragment {

    private Context mContext;
    private TransactionAdapter mAdapter;
    private List<TransactionRecord> transactionRecordList;
    private TextView tvNoData;

    private ProgressDialog mProgress;

    public WithdrawsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraws, container, false);
        mContext = getContext();

        mProgress = new ProgressDialog(getContext());

        transactionRecordList = new ArrayList<>();

        mAdapter = new TransactionAdapter(getContext(), transactionRecordList);
        RecyclerView mRecyclerView = view.findViewById(R.id.withdraw_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        tvNoData = view.findViewById(R.id.tv_no_withdraws);
        tvNoData.setVisibility(View.GONE);

        loadTransactions();

        return view;
    }

    @Override
    public void onStart() {
        //loadTransactions();
        super.onStart();
    }

    private void loadTransactions() {
        mProgress.setMessage("Loading please wait...");
        mProgress.show();
        SharedPreferences sharedPrefs = mContext.getSharedPreferences("SELECTED_ACC_USER",
                Context.MODE_PRIVATE);
        final String userUid = sharedPrefs.getString("userUid", null);

        Toast.makeText(mContext, "" + userUid, Toast.LENGTH_SHORT).show();

        CollectionReference ref = FirebaseFirestore.getInstance().collection("sb_transactions");
        ref.orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            if (mProgress.isShowing()) mProgress.dismiss();

                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                                TransactionRecord record = snapshot.toObject(TransactionRecord.class);
                                if (record != null)
                                    if (record.getTransactionType() == 1 && record.getUserUid().equals(userUid)) {
                                        transactionRecordList.add(record);
                                    }
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
