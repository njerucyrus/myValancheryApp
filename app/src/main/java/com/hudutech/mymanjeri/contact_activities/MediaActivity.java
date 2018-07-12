package com.hudutech.mymanjeri.contact_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.mymanjeri.R;

public class MediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
