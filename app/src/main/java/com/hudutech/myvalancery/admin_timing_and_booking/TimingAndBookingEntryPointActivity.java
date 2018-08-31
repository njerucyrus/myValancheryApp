package com.hudutech.myvalancery.admin_timing_and_booking;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.timming_adapters.TimingSectionAdapter;

public class TimingAndBookingEntryPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_and_booking_entry_point);
        getSupportActionBar().setTitle("Timing And Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPager mViewPager = findViewById(R.id.admin_contact_view_pager);
        TimingSectionAdapter mSectionsPagerAdapter = new TimingSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_contact_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
