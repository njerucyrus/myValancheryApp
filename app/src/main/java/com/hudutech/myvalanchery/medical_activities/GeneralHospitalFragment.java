package com.hudutech.myvalanchery.medical_activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalanchery.Config;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.adapters.medical_adapters.HospitalListAdapter;
import com.hudutech.myvalanchery.models.medical_models.Hospital;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralHospitalFragment extends Fragment {

    private List<Hospital> hospitalList;
    private HospitalListAdapter mAdapter;
    private CollectionReference mRef;
    private ProgressDialog mProgress;
    private Context mContext;


    public GeneralHospitalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_general_hospital, container, false);
        mContext = getContext();
        mProgress = new ProgressDialog(mContext);
        mRef = FirebaseFirestore.getInstance().collection("hospitals");
        hospitalList = new ArrayList<>();
        mAdapter = new HospitalListAdapter(mContext, hospitalList);

        RecyclerView mRecyclerView = v.findViewById(R.id.hospital_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadData();

        return v;

    }

    private void loadData() {
        mProgress.setMessage("loading...");
        mProgress.show();
        mRef.whereEqualTo("category", "General")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Hospital hospital = snapshot.toObject(Hospital.class);
                                if (hospital != null) {
                                    if (Config.isAdmin(mContext)) {
                                        hospitalList.add(hospital);
                                    } else if (!Config.isAdmin(mContext)) {
                                        if (hospital.isValidated()) {
                                            hospitalList.add(hospital);
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
