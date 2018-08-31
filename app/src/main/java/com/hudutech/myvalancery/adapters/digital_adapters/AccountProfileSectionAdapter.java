package com.hudutech.myvalancery.adapters.digital_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalancery.digital_activities.DepositsFragment;
import com.hudutech.myvalancery.digital_activities.WithdrawsFragment;


public class AccountProfileSectionAdapter extends FragmentPagerAdapter {

    public AccountProfileSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DepositsFragment();

            case 1:
                return new WithdrawsFragment();

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
                return "DEPOSITS";
            case 1:
                return "WITHDRAWS";
            default:
                return null;
        }
    }
}