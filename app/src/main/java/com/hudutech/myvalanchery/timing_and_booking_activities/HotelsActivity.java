package com.hudutech.myvalanchery.timing_and_booking_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
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
import com.hudutech.myvalanchery.adapters.ImageViewPagerAdapter;
import com.hudutech.myvalanchery.adapters.timming_adapters.HotelListAdapter;
import com.hudutech.myvalanchery.models.Banner;
import com.hudutech.myvalanchery.models.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelsActivity extends AppCompatActivity {
    private static final String TAG = "HotelsActivity";
    final int delay = 10000; //8 second
    Handler handler = new Handler();
    Runnable runnable;
    private ImageViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<Banner> bannerList;
    private CollectionReference mBannerRef;
    private CollectionReference mHotelsRef;
    private HotelListAdapter hotelListAdapter;
    private List<Hotel> hotelList;
    private ProgressDialog mProgress;


    private int[] pagerIndex = {-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBannerRef = FirebaseFirestore.getInstance().collection("barners");
        mHotelsRef = FirebaseFirestore.getInstance().collection("hotels");

        bannerList = new ArrayList<>();
        hotelList = new ArrayList<>();

        mViewPager = findViewById(R.id.banner_viewpager);
        mAdapter = new ImageViewPagerAdapter(this, bannerList);
        mViewPager.setAdapter(mAdapter);

        mProgress = new ProgressDialog(this);
        hotelListAdapter = new HotelListAdapter(this, hotelList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hotelListAdapter);
        recyclerView.setHasFixedSize(true);

        loadBanners();
        loadData();
    }

    private void loadData() {
        mHotelsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (snapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                                Hotel hotel = snapshot.toObject(Hotel.class);
                                if (hotel != null) {
                                    if (Config.isAdmin(HotelsActivity.this)) {
                                        hotelList.add(hotel);
                                    } else if (!Config.isAdmin(HotelsActivity.this)) {
                                        hotelList.add(hotel);
                                    }
                                }
                            }
                            hotelListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(HotelsActivity.this, "No Data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HotelsActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        handler.postDelayed(new Runnable() {
                                public void run() {
                                    pagerIndex[0]++;
                                    if (pagerIndex[0] >= mAdapter.getCount()) {
                                        pagerIndex[0] = 0;
                                    }

                                    mViewPager.setCurrentItem(pagerIndex[0]);
                                    runnable = this;

                                    handler.postDelayed(runnable, delay);
                                }
                            }
                , delay);


        super.onStart();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    private void loadBanners() {
        mBannerRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Banner barner = snapshot.toObject(Banner.class);
                                bannerList.add(barner);

                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }


}
