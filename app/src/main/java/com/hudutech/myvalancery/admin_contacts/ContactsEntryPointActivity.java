package com.hudutech.myvalancery.admin_contacts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.AdminContactSectionAdapter;

public class ContactsEntryPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_entry_point);
        getSupportActionBar().setTitle("Admin Contact Section");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager mViewPager = findViewById(R.id.admin_contact_view_pager);
        AdminContactSectionAdapter mSectionsPagerAdapter = new AdminContactSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_contact_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
