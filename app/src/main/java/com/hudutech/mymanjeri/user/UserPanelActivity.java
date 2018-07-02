package com.hudutech.mymanjeri.user;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hudutech.mymanjeri.MainActivity;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.admin.AddBloodBankFragment;
import com.hudutech.mymanjeri.admin.AddShoppingFragment;

public class UserPanelActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        getSupportActionBar().setTitle("User Panel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.user_panel_add_blood_bank).setOnClickListener(this);
        findViewById(R.id.user_panel_add_shops).setOnClickListener(this);
        findViewById(R.id.user_panel_add_vehicles).setOnClickListener(this);
        layout = findViewById(R.id.layout_userpanel_menu_ui);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_panel_add_blood_bank:
                layout.setVisibility(View.GONE);
                Fragment bloodBankFragment = new AddBloodBankFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.user_panel_container, bloodBankFragment);
                transaction.commit();
                break;

            case R.id.user_panel_add_shops:
                layout.setVisibility(View.GONE);
                Fragment addShoppingFragment = new AddShoppingFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.user_panel_container, addShoppingFragment);
                fragmentTransaction.commit();
                break;

            case R.id.user_panel_add_vehicles:
                layout.setVisibility(View.GONE);
                Fragment addVehicleFragment = new AddVehicleFragment();
                FragmentTransaction addVehicleTxn = getSupportFragmentManager().beginTransaction();
                addVehicleTxn.replace(R.id.user_panel_container, addVehicleFragment);
                addVehicleTxn.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStackImmediate();

        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
