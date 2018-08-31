package com.hudutech.myvalancery.timing_and_booking_activities;

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
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.ImageViewPagerAdapter;
import com.hudutech.myvalancery.adapters.timming_adapters.RestaurantListAdapter;
import com.hudutech.myvalancery.models.Banner;
import com.hudutech.myvalancery.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsActivity extends AppCompatActivity {
    private static final String TAG = "RestaurantsActivity";
    final int delay = 10000; //8 second
    Handler handler = new Handler();
    Runnable runnable;
    private ImageViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<Banner> bannerList;
    private CollectionReference mBannerRef;
    private CollectionReference mHotelsRef;
    private RestaurantListAdapter restaurantListAdapter;
    private List<Restaurant> restaurantList;
    private ProgressDialog mProgress;


    private int[] pagerIndex = {-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBannerRef = FirebaseFirestore.getInstance().collection("barners");
        mHotelsRef = FirebaseFirestore.getInstance().collection("restaurants");

        bannerList = new ArrayList<>();
        restaurantList = new ArrayList<>();

        mViewPager = findViewById(R.id.banner_viewpager);
        mAdapter = new ImageViewPagerAdapter(this, bannerList);
        mViewPager.setAdapter(mAdapter);

        mProgress = new ProgressDialog(this);
        restaurantListAdapter = new RestaurantListAdapter(this, restaurantList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(restaurantListAdapter);
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
                                Restaurant restaurant = snapshot.toObject(Restaurant.class);
                                if (restaurant != null) {
                                    if (Config.isAdmin(RestaurantsActivity.this)) {
                                        restaurantList.add(restaurant);
                                    } else if (!Config.isAdmin(RestaurantsActivity.this)) {
                                        restaurantList.add(restaurant);
                                    }
                                }
                            }
                            restaurantListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(RestaurantsActivity.this, "No Data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RestaurantsActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
