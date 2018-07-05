package com.hudutech.mymanjeri.admin_majery;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.AdminSectionAdapter;

public class DataEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        getSupportActionBar().setTitle("Admin Panel- Data Entry");

        ViewPager mViewPager = findViewById(R.id.adminDataEntryViewPager);
        AdminSectionAdapter mSectionsPagerAdapter = new AdminSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.adminDataEntryTablayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
