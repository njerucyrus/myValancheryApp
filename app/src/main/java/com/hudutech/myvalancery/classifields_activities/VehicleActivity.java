package com.hudutech.myvalancery.classifields_activities;

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
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.classifields_adapters.VehicleListAdapter;
import com.hudutech.myvalancery.models.classifields_models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleActivity extends AppCompatActivity {
    private static final String TAG = "VehicleActivity";
    private VehicleListAdapter mAdapter;
    private List<Vehicle> vehicleList;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle2);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef = FirebaseFirestore.getInstance().collection("classifields_vehicles");
        vehicleList = new ArrayList<>();
        mAdapter = new VehicleListAdapter(this, vehicleList);
        mProgress = new ProgressDialog(this);
        RecyclerView mRecyclerView = findViewById(R.id.admin_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadData();
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
                                Vehicle vehicle = snapshot.toObject(Vehicle.class);
                                vehicleList.add(vehicle);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(VehicleActivity.this, "No Data found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(VehicleActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
