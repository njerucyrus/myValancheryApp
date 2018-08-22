package com.hudutech.myvalanchery.majery_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.adapters.majery_adapters.TourismListAdapter;
import com.hudutech.myvalanchery.models.majery_models.TourismPlace;

import java.util.ArrayList;
import java.util.List;

public class TourismActivity extends AppCompatActivity {
    private static final String TAG = "TourismActivity";
    private List<TourismPlace> tourismPlaceList;
    private ProgressDialog mProgress;
    private CollectionReference mRef;
    private TourismListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tourismPlaceList = new ArrayList<>();
        mRef = FirebaseFirestore.getInstance().collection("tourism");
        mProgress = new ProgressDialog(this);
        mAdapter = new TourismListAdapter(this, tourismPlaceList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        loadData();

    }

    private void loadData() {
        mProgress.setMessage("loading please wait...");
        mProgress.show();

        mRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                TourismPlace tourismPlace = snapshot.toObject(TourismPlace.class);
                                if (tourismPlace != null) {
                                    if (tourismPlace.isValidated())
                                        tourismPlaceList.add(tourismPlace);
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
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });

    }
}
