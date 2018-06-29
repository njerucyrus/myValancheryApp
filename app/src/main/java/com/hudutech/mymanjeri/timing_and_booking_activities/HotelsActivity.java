package com.hudutech.mymanjeri.timing_and_booking_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.mymanjeri.R;

public class HotelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
