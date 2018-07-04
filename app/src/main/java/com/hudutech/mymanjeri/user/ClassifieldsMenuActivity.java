package com.hudutech.mymanjeri.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.hudutech.mymanjeri.R;

public class ClassifieldsMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layout;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifield_menu);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Classifieds");
        actionBar.setDisplayHomeAsUpEnabled(true);

        layout = findViewById(R.id.layout_classifields_menu);

        findViewById(R.id.user_panel_classifields_addrealestate).setOnClickListener(this);
        findViewById(R.id.user_panel_classifields_addvehicle).setOnClickListener(this);
        findViewById(R.id.user_panel_classifields_addelectronics).setOnClickListener(this);
        findViewById(R.id.user_panel_classifields_addpets).setOnClickListener(this);
        findViewById(R.id.user_panel_classifields_addother).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.user_panel_classifields_addrealestate:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Real Estate");
                Fragment realEstateFragment = new AddRealEstateFragment();
                FragmentTransaction realEstateTxn = getSupportFragmentManager().beginTransaction();
                realEstateTxn.replace(R.id.classfield_container, realEstateFragment);
                realEstateTxn.addToBackStack(null);
                realEstateTxn.commit();
                break;
            case R.id.user_panel_classifields_addvehicle:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Vehicles");
                Fragment addVehicleClassfieldFragment = new AddVehicleClassfieldFragment();
                FragmentTransaction vehicleTxn = getSupportFragmentManager().beginTransaction();
                vehicleTxn.replace(R.id.classfield_container, addVehicleClassfieldFragment);
                vehicleTxn.addToBackStack(null);
                vehicleTxn.commit();
                break;
            case R.id.user_panel_classifields_addelectronics:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Electronics");
                Fragment addElectronicsFragment = new AddElectronicsFragment();
                FragmentTransaction electronicsTxn = getSupportFragmentManager().beginTransaction();
                electronicsTxn.replace(R.id.classfield_container, addElectronicsFragment);
                electronicsTxn.addToBackStack(null);
                electronicsTxn.commit();
                break;
            case R.id.user_panel_classifields_addpets:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Pets");
                Fragment addPetsFragment = new AddPetsFragment();
                FragmentTransaction petsTxn = getSupportFragmentManager().beginTransaction();
                petsTxn.replace(R.id.classfield_container, addPetsFragment);
                petsTxn.addToBackStack(null);
                petsTxn.commit();
                break;
            case R.id.user_panel_classifields_addother:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Others");
                Fragment addOtherFragment = new AddOtherFragment();
                FragmentTransaction othersTxn = getSupportFragmentManager().beginTransaction();
                othersTxn.replace(R.id.classfield_container, addOtherFragment);
                othersTxn.addToBackStack(null);
                othersTxn.commit();
                break;

        }
    }

    @Override
    public void onBackPressed() {


        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 1) {
           finish();
        } else if (count > 1) {
            getFragmentManager().popBackStack();
            moveTaskToBack(false);

        }else {
            layout.setVisibility(View.VISIBLE);
            actionBar.setTitle("Classifieds");
            super.onBackPressed();
        }


    }
}
