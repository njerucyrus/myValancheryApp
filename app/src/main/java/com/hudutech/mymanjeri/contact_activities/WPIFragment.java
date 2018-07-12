package com.hudutech.mymanjeri.contact_activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.contact_adapter.PoliticsListAdapter;
import com.hudutech.mymanjeri.models.contact_models.Politics;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WPIFragment extends Fragment {
    private static final String TAG = "WPIFragment";
    private Context mContext;
    private PoliticsListAdapter mAdapter;
    private List<Politics> politicsList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    private ListenerRegistration registration;

    public WPIFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wpi, container, false);
        mContext = getContext();
        mRef = FirebaseFirestore.getInstance().collection("politics");
        politicsList = new ArrayList<>();
        mAdapter = new PoliticsListAdapter(mContext, politicsList);
        mProgress = new ProgressDialog(getContext());
        RecyclerView mRecyclerView = v.findViewById(R.id.admin_contact_vehicle_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadData();

        return v;

    }

    private void loadData() {
        mProgress.setMessage("Loading...");
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.show();

        mRef.whereEqualTo("party", "WPI").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (documentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                                Politics politics = snapshot.toObject(Politics.class);
                                if (politics != null) {
                                    if (Config.isAdmin(mContext)) {
                                        politicsList.add(politics);
                                    } else if (!Config.isAdmin(mContext)){
                                        if (politics.isValidated()) politicsList.add(politics);
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "No Data found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Log.e(TAG, "onFailure: "+e.getMessage());
                        Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
