package com.hudutech.myvalancery.timing_and_booking_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.timming_adapters.BusListAdapter;
import com.hudutech.myvalancery.models.Bus;

import java.util.ArrayList;
import java.util.List;

public class BusActivity extends AppCompatActivity {
    private CollectionReference busRef;
    private ProgressDialog mProgress;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private List<Bus> busList;
    private BusListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        spinnerFrom = findViewById(R.id.spinner_starting_point);
        spinnerTo = findViewById(R.id.spinner_ending_point);
        busRef = FirebaseFirestore.getInstance().collection("buses");
        initRoutes();
        busList = new ArrayList<>();
        mAdapter = new BusListAdapter(this, busList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String to = parent.getItemAtPosition(position).toString();
                String from = spinnerFrom.getSelectedItem().toString();

                showBuses(from, to);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String from = parent.getItemAtPosition(position).toString();
                String to = spinnerTo.getSelectedItem().toString();
                showBuses(from, to);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRoutes() {
        mProgress.setMessage("loading please wait...");
        mProgress.show();
        busRef.orderBy("arrivalTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            ArrayAdapter<String> dataAdapterFrom;
                            ArrayAdapter<String> dataAdapterTo;


                            List<String> from = new ArrayList<>();
                            List<String> to = new ArrayList<>();
                            from.add("Select Starting Point");
                            to.add("Select Ending Point");

                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Bus bus = snapshot.toObject(Bus.class);
                                if (bus != null) {

                                    if (!from.contains(bus.getStartPoint())) {
                                        from.add(bus.getStartPoint());
                                    }
                                    if (!to.contains(bus.getEndPoint())) {
                                        to.add(bus.getEndPoint());
                                    }

                                }
                            }

                            dataAdapterFrom = new ArrayAdapter<>(BusActivity.this, android.R.layout.simple_spinner_dropdown_item, from);
                            spinnerFrom.setAdapter(dataAdapterFrom);

                            dataAdapterTo = new ArrayAdapter<>(BusActivity.this, android.R.layout.simple_spinner_dropdown_item, to);
                            spinnerTo.setAdapter(dataAdapterTo);
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

    private void showBuses(final String start, final String end) {
        mProgress.setMessage("initializing routes please wait...");
        mProgress.show();
        busRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        busList.clear();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Bus bus = snapshot.toObject(Bus.class);
                                if (bus != null) {
                                    if (bus.getStartPoint().equals(start) && bus.getEndPoint().equals(end)) {

                                        busList.add(bus);

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
