package com.hudutech.mymanjeri.majery_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.majery_adapters.NotificationAdapter;
import com.hudutech.mymanjeri.models.majery_models.CustomNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private CollectionReference mNotifRef;
    private ProgressDialog mProgress;
    private List<CustomNotification> customNotificationList;
    private NotificationAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNotifRef = FirebaseFirestore.getInstance().collection("notifications");
        mProgress = new ProgressDialog(this);
        customNotificationList = new ArrayList<>();
        mAdapter = new NotificationAdapter(this, customNotificationList);

        RecyclerView mRecyclerView = findViewById(R.id.notification_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        loadNotifications();
    }

    private void loadNotifications() {
        mProgress.setMessage("Loading please wait...");
        mProgress.show();

        mNotifRef.orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            CustomNotification notification = snapshot.toObject(CustomNotification.class);
                            customNotificationList.add(notification);
                        }
                        mAdapter.notifyDataSetChanged();
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
