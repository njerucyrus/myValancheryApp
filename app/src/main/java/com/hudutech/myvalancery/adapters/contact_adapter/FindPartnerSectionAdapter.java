package com.hudutech.myvalancery.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalancery.contact_activities.FemaleFragment;
import com.hudutech.myvalancery.contact_activities.MaleFragment;


public class FindPartnerSectionAdapter extends FragmentPagerAdapter {

    public FindPartnerSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MaleFragment();
            case 1:
                return new FemaleFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Male";
            case 1:
                return "Female";

            default:
                return null;
        }
    }
}