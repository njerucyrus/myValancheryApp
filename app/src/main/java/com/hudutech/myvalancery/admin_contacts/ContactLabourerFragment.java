package com.hudutech.myvalancery.admin_contacts;


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
import com.hudutech.myvalancery.adapters.contact_adapter.LabourerListAdapter;
import com.hudutech.myvalancery.models.contact_models.Labourer;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactLabourerFragment extends Fragment {
    private static final String TAG = "ContactLabourerFragment";
    private Context mContext;
    private LabourerListAdapter mAdapter;
    private List<Labourer> labourerList;
    private CollectionReference mLabourerRef;
    private ProgressDialog mProgress;

    public ContactLabourerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_labourer, container, false);

        mContext = getContext();
        mLabourerRef = FirebaseFirestore.getInstance().collection("labourers");
        labourerList = new ArrayList<>();
        mAdapter = new LabourerListAdapter(mContext, labourerList);
        mProgress = new ProgressDialog(getContext());
        RecyclerView mRecyclerView = v.findViewById(R.id.admin_contact_vehicle_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadLabourers();

        return v;
    }

    private void loadLabourers() {
        mProgress.setMessage("Loading...");
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.show();

        mLabourerRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (documentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()) {
                                Labourer labourer = snapshot.toObject(Labourer.class);
                                labourerList.add(labourer);
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
