package com.hudutech.myvalanchery.contact_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalanchery.Config;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.adapters.contact_adapter.MunicipalityListAdapter;
import com.hudutech.myvalanchery.models.contact_models.Municipality;

import java.util.ArrayList;
import java.util.List;

public class MunicipalityActivity extends AppCompatActivity {

    private List<Municipality> municipalityList;
    private MunicipalityListAdapter mAdapter;
    private CollectionReference mRef;
    private ProgressDialog mProgress;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = findViewById(R.id.layout_item_list);

        mProgress = new ProgressDialog(this);
        municipalityList = new ArrayList<>();
        mAdapter = new MunicipalityListAdapter(this, municipalityList);
        mRef = FirebaseFirestore.getInstance().collection("municipality");
        RecyclerView mRecyclerView = findViewById(R.id.item_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadData();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            getFragmentManager().popBackStack();
            moveTaskToBack(false);
        } else {
            super.onBackPressed();
        }
        layout.setVisibility(View.VISIBLE);

    }

    private void loadData() {
        mProgress.setMessage("loading...");
        mProgress.show();
        mRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Municipality municipality = snapshot.toObject(Municipality.class);
                                if (municipality != null) {
                                    if (Config.isAdmin(getApplicationContext())) {
                                        municipalityList.add(municipality);
                                    } else if (!Config.isAdmin(MunicipalityActivity.this)) {
                                        if (municipality.isValidated())
                                            municipalityList.add(municipality);
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MunicipalityActivity.this, "No Data found.", Toast.LENGTH_SHORT).show();
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
