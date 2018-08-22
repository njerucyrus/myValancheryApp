package com.hudutech.myvalanchery.majery_activities;

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
import com.hudutech.myvalanchery.adapters.contact_adapter.ShopListAdapter;
import com.hudutech.myvalanchery.models.majery_models.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuDetailActivity extends AppCompatActivity {
    private static final String TAG = "ShopMenuDetailActivity";
    private CollectionReference mRef;
    private ShopListAdapter mAdapter;
    private List<Shop> shopList;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_menu_detail);
        getSupportActionBar().setTitle(getIntent().getStringExtra("shopType"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shopList = new ArrayList<>();
        mAdapter = new ShopListAdapter(this, shopList);
        mRef = FirebaseFirestore.getInstance().collection("shops");

        RecyclerView mRecyclerView = findViewById(R.id.admin_contact_vehicle_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        mProgress = new ProgressDialog(this);
        displayData(getIntent().getStringExtra("shopType"));

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
                                    if (Config.isAdmin(ShopMenuDetailActivity.this)) {
                                        shopList.add(shop);
                                    } else if (!Config.isAdmin(ShopMenuDetailActivity.this)) {
                                        if (shop.isValidated()) {
                                            shopList.add(shop);
                                        }
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ShopMenuDetailActivity.this, "No data!", Toast.LENGTH_SHORT).show();
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
