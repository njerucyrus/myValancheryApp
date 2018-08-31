package com.hudutech.myvalancery.contact_activities;


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
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.contact_adapter.ShopListAdapter;
import com.hudutech.myvalancery.models.majery_models.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopItemFragment extends Fragment {
    private static final String TAG = "ShopItemFragment";
    private CollectionReference mRef;
    private ShopListAdapter mAdapter;
    private List<Shop> shopList;
    private Context mContext;
    private ProgressDialog mProgress;

    public ShopItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop_item, container, false);
        mRef = FirebaseFirestore.getInstance().collection("shopping");
        mContext = getContext();

        shopList = new ArrayList<>();
        mAdapter = new ShopListAdapter(mContext, shopList);

        RecyclerView mRecyclerView = v.findViewById(R.id.admin_contact_vehicle_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        mProgress = new ProgressDialog(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String shopType = bundle.getString("shopType");
            displayData(shopType);
        }
        return v;
    }

    private void displayData(String shopType) {
        mProgress.setMessage("Loading...");
        mProgress.show();
        mRef.whereEqualTo("shopType", shopType)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Shop shop = snapshot.toObject(Shop.class);
                                if (shop != null) {
                                    if (Config.isAdmin(mContext)) {
                                        shopList.add(shop);
                                    } else if (!Config.isAdmin(mContext)) {
                                        if (shop.isValidated()) {
                                            shopList.add(shop);
                                        }
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "No data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Log.d(TAG, "onFailure: " + e.getMessage());

                    }
                });
    }

}
