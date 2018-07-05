package com.hudutech.mymanjeri.admin_majery;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hudutech.mymanjeri.R;

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
                break;

        }
    }

    private void showActivity(Class<?> cls){
        startActivity(new Intent(this, cls));
    }

    private void showActivity(Intent intent) {
        startActivity(intent);
    }
}
