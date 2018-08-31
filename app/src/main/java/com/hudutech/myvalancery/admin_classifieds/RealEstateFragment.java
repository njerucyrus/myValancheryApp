package com.hudutech.myvalancery.admin_classifieds;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.classifields_adapters.RealEstateListAdapter;
import com.hudutech.myvalancery.models.classifields_models.RealEstate;

import java.util.ArrayList;
import java.util.List;

public class RealEstateFragment extends Fragment {
    private static final String TAG = "RealEstateFragment";
    private Context mContext;
    private RealEstateListAdapter mAdapter;
    private List<RealEstate> realEstateList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    public RealEstateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_real_estate, container, false);

        mContext = getContext();
        mRef = FirebaseFirestore.getInstance().collection("classifields_real_estate");
        realEstateList = new ArrayList<>();
        mAdapter = new RealEstateListAdapter(mContext, realEstateList);
        mProgress = new ProgressDialog(getContext());
        RecyclerView mRecyclerView = v.findViewById(R.id.admin_recyclerview);
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

        mRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (documentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                                RealEstate realEstate = snapshot.toObject(RealEstate.class);
                                realEstateList.add(realEstate);
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
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
