package com.hudutech.mymanjeri.admin_classifieds;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.AdminClassifieldSectionAdapter;

public class ClassfiedsEntryPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classfieds_entry_point);

        ViewPager mViewPager = findViewById(R.id.admin_view_pager);
        AdminClassifieldSectionAdapter mSectionsPagerAdapter = new AdminClassifieldSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.admin_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
