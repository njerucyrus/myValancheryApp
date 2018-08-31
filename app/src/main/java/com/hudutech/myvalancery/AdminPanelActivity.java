package com.hudutech.myvalancery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hudutech.myvalancery.admin_banner_setting.AdminBarnersActivity;
import com.hudutech.myvalancery.admin_banner_setting.StartBannerActivity;
import com.hudutech.myvalancery.admin_classifieds.ClassfiedsEntryPointActivity;
import com.hudutech.myvalancery.admin_contacts.ContactsEntryPointActivity;
import com.hudutech.myvalancery.admin_majery.DataEntryActivity;
import com.hudutech.myvalancery.admin_medical.MedicalEntryPointActivity;
import com.hudutech.myvalancery.admin_timing_and_booking.TimingAndBookingEntryPointActivity;
import com.hudutech.myvalancery.auth.LoginActivity;

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {


    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
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
        findViewById(R.id.admin_panel_live_tv).setOnClickListener(this);
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

            case R.id.admin_panel_medical:
                showActivity(MedicalEntryPointActivity.class);
                break;

            case R.id.admin_panel_timing_and_booking:
                showActivity(TimingAndBookingEntryPointActivity.class);
                break;

            case R.id.admin_panel_banner_setting:
                showActivity(AdminBarnersActivity.class);
                break;

            case R.id.admin_panel_opening_banner:
                showActivity(StartBannerActivity.class);
                break;

            case R.id.admin_panel_live_tv:
                showActivity(AdminLiveTvActivity.class);
                break;

            case R.id.admin_panel_logout:
                signOut();
                break;


        }
    }

    private void showActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to logout?");
        builder.setMessage("You will be logged out");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                sharedPrefEditor.putBoolean("isAdmin", false);
                sharedPrefEditor.putBoolean("isSBAdmin", false);
                sharedPrefEditor.apply();
                sharedPrefEditor.commit();
                Toast.makeText(AdminPanelActivity.this, "You Are logged out", Toast.LENGTH_SHORT).show();
                showActivity(MainActivity.class);
                finish();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    @Override
    protected void onStart() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "You are not allowed to view this page. login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class).putExtra("menuName", "admin"));
        } else {
            if (!Config.isAdmin(this)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                Toast.makeText(this, "Access denied You are not an admin.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
    }
}
