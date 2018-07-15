package com.hudutech.mymanjeri.contact_activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.contact_adapter.LabourerSectionAdapter;

public class LabouresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboures);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mViewPager = findViewById(R.id.admin_contact_view_pager);
        LabourerSectionAdapter mSectionsPagerAdapter = new LabourerSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_contact_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
