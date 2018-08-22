package com.hudutech.myvalanchery.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalanchery.admin_classifieds.ElectronicsFragment;
import com.hudutech.myvalanchery.admin_classifieds.OtherFragment;
import com.hudutech.myvalanchery.admin_classifieds.PetsFragment;
import com.hudutech.myvalanchery.admin_classifieds.RealEstateFragment;
import com.hudutech.myvalanchery.admin_classifieds.VehicleFragment;


public class AdminClassifieldSectionAdapter extends FragmentPagerAdapter {

    public AdminClassifieldSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RealEstateFragment();
            case 1:
                return new VehicleFragment();
            case 2:
                return new ElectronicsFragment();
            case 3:
                return new PetsFragment();
            case 4:
                return new OtherFragment();
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
                return "Real Estate";
            case 1:
                return "Vehicle";
            case 2:
                return "Electronics";
            case 3:
                return "Pets";
            case 4:
                return "Others";
            default:
                return null;
        }
    }
}