package com.hudutech.myvalancery.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalancery.contact_activities.AutoVehicleFragment;
import com.hudutech.myvalancery.contact_activities.GoodsVehicleFragment;
import com.hudutech.myvalancery.contact_activities.JcbVehicleFragment;
import com.hudutech.myvalancery.contact_activities.PickUpVehicleFragment;
import com.hudutech.myvalancery.contact_activities.TaxiVehicleFragment;


public class VehicleSectionAdapter extends FragmentPagerAdapter {

    public VehicleSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AutoVehicleFragment();
            case 1:
                return new TaxiVehicleFragment();
            case 2:
                return new GoodsVehicleFragment();
            case 3:
                return new PickUpVehicleFragment();
            case 4:
                return new JcbVehicleFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Auto";
            case 1:
                return "Taxi";
            case 2:
                return "Goods";
            case 3:
                return "PickUp";

            case 4:
                return "JCB";
            default:
                return null;
        }
    }
}