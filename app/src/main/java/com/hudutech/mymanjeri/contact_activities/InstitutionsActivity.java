package com.hudutech.mymanjeri.contact_activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.contact_adapter.InstitutionSectionAdapter;
import com.hudutech.mymanjeri.adapters.contact_adapter.VehicleSectionAdapter;

public class InstitutionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institutions);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mViewPager = findViewById(R.id.admin_contact_view_pager);
        InstitutionSectionAdapter mSectionsPagerAdapter = new InstitutionSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_contact_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
