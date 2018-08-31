package com.hudutech.myvalancery.admin_medical;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.AdminMedicalSectionAdapter;

public class MedicalEntryPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_entry_point);
        getSupportActionBar().setTitle("Medical");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mViewPager = findViewById(R.id.admin_contact_view_pager);
        AdminMedicalSectionAdapter mSectionsPagerAdapter = new AdminMedicalSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_contact_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
