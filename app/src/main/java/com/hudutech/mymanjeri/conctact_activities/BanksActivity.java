package com.hudutech.mymanjeri.conctact_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hudutech.mymanjeri.R;

public class BanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
