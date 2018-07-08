package com.hudutech.mymanjeri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hudutech.mymanjeri.admin_classifieds.ClassfiedsEntryPointActivity;
import com.hudutech.mymanjeri.admin_contacts.ContactsEntryPointActivity;
import com.hudutech.mymanjeri.admin_majery.DataEntryActivity;

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {


    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        actionBar = getSupportActionBar();
        if (actionBar !=null) {
            actionBar.setTitle("Admin Panel");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.admin_panel_majery).setOnClickListener(this);
        findViewById(R.id.admin_panel_contacts).setOnClickListener(this);
        findViewById(R.id.admin_panel_timing_and_booking).setOnClickListener(this);
        findViewById(R.id.admin_panel_classifieds).setOnClickListener(this);
        findViewById(R.id.admin_panel_banner_setting).setOnClickListener(this);
        findViewById(R.id.admin_panel_medical).setOnClickListener(this);
        findViewById(R.id.admin_panel_opening_banner).setOnClickListener(this);
        findViewById(R.id.admin_panel_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.admin_panel_majery:
                showActivity(DataEntryActivity.class);
                break;
            case R.id.admin_panel_contacts:
                showActivity(ContactsEntryPointActivity.class);
                break;
            case R.id.admin_panel_classifieds:
                    showActivity(ClassfiedsEntryPointActivity.class);
                break;

        }
    }

    private void showActivity(Class<?> cls){
        startActivity(new Intent(this, cls));
    }


}
