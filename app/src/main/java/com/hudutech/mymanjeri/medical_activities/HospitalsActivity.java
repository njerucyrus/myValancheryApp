package com.hudutech.mymanjeri.medical_activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.medical_adapters.UserMedicalSectionAdapter;

public class HospitalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mViewPager = findViewById(R.id.admin_contact_view_pager);
        UserMedicalSectionAdapter mSectionsPagerAdapter = new UserMedicalSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_contact_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
