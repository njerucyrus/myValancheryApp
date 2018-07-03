package com.hudutech.mymanjeri.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.admin.AddBloodBankFragment;
import com.hudutech.mymanjeri.admin.AddShoppingFragment;
import com.hudutech.mymanjeri.digital_activities.FindPartnerActivity;

public class UserPanelActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layout;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        actionBar = getSupportActionBar();
        actionBar.setTitle("User Panel");
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.user_panel_add_blood_bank).setOnClickListener(this);
        findViewById(R.id.user_panel_add_shops).setOnClickListener(this);
        findViewById(R.id.user_panel_add_vehicles).setOnClickListener(this);
        findViewById(R.id.user_panel_add_professional).setOnClickListener(this);
        findViewById(R.id.user_panel_add_labourers).setOnClickListener(this);
        findViewById(R.id.user_panel_add_hotel).setOnClickListener(this);
        findViewById(R.id.user_panel_add_restaurant).setOnClickListener(this);
        findViewById(R.id.user_panel_findpartner).setOnClickListener(this);

        layout = findViewById(R.id.layout_userpanel_menu_ui);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_panel_add_blood_bank:
                actionBar.setTitle("Add Blood Bank");
                layout.setVisibility(View.GONE);
                Fragment bloodBankFragment = new AddBloodBankFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.user_panel_container, bloodBankFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.user_panel_add_shops:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Add Shops");
                Fragment addShoppingFragment = new AddShoppingFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.user_panel_container, addShoppingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.user_panel_add_vehicles:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Add Vehicle");
                Fragment addVehicleFragment = new AddVehicleFragment();
                FragmentTransaction addVehicleTxn = getSupportFragmentManager().beginTransaction();
                addVehicleTxn.replace(R.id.user_panel_container, addVehicleFragment);
                addVehicleTxn.addToBackStack(null);
                addVehicleTxn.commit();
                break;

            case R.id.user_panel_add_professional:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Add Professional");
                Fragment addProfessionalFragment = new AddProfessionalFragment();
                FragmentTransaction addProfTxn = getSupportFragmentManager().beginTransaction();
                addProfTxn.replace(R.id.user_panel_container, addProfessionalFragment);
                addProfTxn.addToBackStack(null);
                addProfTxn.commit();
                break;

            case R.id.user_panel_add_labourers:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Add Labourer");
                Fragment addLabourerFragment = new AddLabourerFragment();
                FragmentTransaction addLabourerTxn = getSupportFragmentManager().beginTransaction();
                addLabourerTxn.replace(R.id.user_panel_container, addLabourerFragment);
                addLabourerTxn.addToBackStack(null);
                addLabourerTxn.commit();
                break;

            case R.id.user_panel_add_hotel:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Add Hotel");
                Fragment addHotelFragment = new AddHotelFragment();
                FragmentTransaction addHotelTxn = getSupportFragmentManager().beginTransaction();
                addHotelTxn.replace(R.id.user_panel_container, addHotelFragment);
                addHotelTxn.addToBackStack(null);
                addHotelTxn.commit();
                break;

            case R.id.user_panel_add_restaurant:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Add Restaurant");
                Fragment addRestaurantFragment = new AddRestaurantFragment();
                FragmentTransaction addResTxn = getSupportFragmentManager().beginTransaction();
                addResTxn.replace(R.id.user_panel_container, addRestaurantFragment);
                addResTxn.addToBackStack(null);
                addResTxn.commit();
                break;

            case R.id.user_panel_findpartner:
                layout.setVisibility(View.GONE);
                actionBar.setTitle("Find Partner");
                Fragment findPartnerFragment = new FindPartnerFragment();
                FragmentTransaction partnerTxn = getSupportFragmentManager().beginTransaction();
                partnerTxn.replace(R.id.user_panel_container, findPartnerFragment);
                partnerTxn.addToBackStack(null);
                partnerTxn.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getFragmentManager().popBackStack();
        }
        layout.setVisibility(View.VISIBLE);
        actionBar.setTitle("User Panel");

        super.onBackPressed();
    }
}
