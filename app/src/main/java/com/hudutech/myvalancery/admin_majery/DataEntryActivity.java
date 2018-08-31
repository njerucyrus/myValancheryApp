package com.hudutech.myvalancery.admin_majery;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.AdminMajerySectionAdapter;

public class DataEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        getSupportActionBar().setTitle("Admin Valancery Section");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mViewPager = findViewById(R.id.adminDataEntryViewPager);
        AdminMajerySectionAdapter mSectionsPagerAdapter = new AdminMajerySectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.adminDataEntryTablayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
