package com.hudutech.mymanjeri.majery_activities;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.ImageViewPagerAdapter;
import com.hudutech.mymanjeri.adapters.majery_adapters.NewsAdapter;
import com.hudutech.mymanjeri.models.Banner;
import com.hudutech.mymanjeri.models.majery_models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "NewsActivity";
    private RecyclerView mRecyclerView;
    private List<News> newsList;
    private NewsAdapter mAdapter;
    private FirebaseFirestore db;
    private CollectionReference mRootRef;
    private CollectionReference mBannerRef;
    private ProgressDialog mProgress;

    private ViewPager mViewPager;
    private List<Banner> barnerList;
    private ImageViewPagerAdapter bannerAdpter;

    Handler handler = new Handler();
    final int delay = 10000; //8 second
    Runnable runnable;
    private int[] pagerIndex = {-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newsList = new ArrayList<>();
        mAdapter = new NewsAdapter(this, newsList);
        mRecyclerView = findViewById(R.id.news_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mProgress = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        mRootRef = db.collection("news");
        mBannerRef = FirebaseFirestore.getInstance().collection("barners");
        barnerList = new ArrayList<>();
        mViewPager = findViewById(R.id.news_banner_viewpager);
        bannerAdpter = new ImageViewPagerAdapter(this, barnerList);
        mViewPager.setAdapter(bannerAdpter);
        loadBarners();
        fetchNews();
    }

    @Override
    protected void onStart() {
        handler.postDelayed(new Runnable() {
                                public void run() {
                                    pagerIndex[0]++;
                                    if (pagerIndex[0] >= bannerAdpter.getCount()) {
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

    private void loadBarners() {
        mBannerRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Banner barner = snapshot.toObject(Banner.class);
                                barnerList.add(barner);

                            }
                            bannerAdpter.notifyDataSetChanged();
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

    private void fetchNews() {
        mProgress.setMessage("Loading...");
        mProgress.show();
        mRootRef.orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                News news = snapshot.toObject(News.class);
                                newsList.add(news);

                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        TextView noData = findViewById(R.id.tv_no_news);
                        if (newsList.size() == 0) {

                            noData.setVisibility(View.VISIBLE);
                        }else {
                            noData.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TextView noData = findViewById(R.id.tv_no_news);
                        if (newsList.size() == 0) {

                            noData.setVisibility(View.VISIBLE);
                        }else {
                            noData.setVisibility(View.GONE);
                        }

                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(NewsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
