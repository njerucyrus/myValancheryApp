package com.hudutech.myvalancery.admin_timing_and_booking;


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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.timming_adapters.RestaurantListAdapter;
import com.hudutech.myvalancery.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {
    private static final String TAG = "RestaurantFragment";
    private Context mContext;
    private RestaurantListAdapter mAdapter;
    private List<Restaurant> restaurantList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        mContext = getContext();
        mRef = FirebaseFirestore.getInstance().collection("restaurants");
        restaurantList = new ArrayList<>();
        mAdapter = new RestaurantListAdapter(mContext, restaurantList);
        mProgress = new ProgressDialog(getContext());
        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);
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
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Restaurant restaurant = snapshot.toObject(Restaurant.class);
                                if (restaurant != null) {
                                    if (Config.isAdmin(mContext)) {
                                        restaurantList.add(restaurant);
                                    } else if (!Config.isAdmin(mContext)) {
                                        if (restaurant.isValidated())
                                            restaurantList.add(restaurant);
                                    }
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });


    }

}
