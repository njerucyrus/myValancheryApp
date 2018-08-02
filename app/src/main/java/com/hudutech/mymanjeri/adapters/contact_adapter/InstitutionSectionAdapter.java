package com.hudutech.mymanjeri.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.mymanjeri.contact_activities.CollegeFragment;
import com.hudutech.mymanjeri.contact_activities.CouchingCenterFragment;
import com.hudutech.mymanjeri.contact_activities.EnglishMediumFragment;
import com.hudutech.mymanjeri.contact_activities.HSCFragment;
import com.hudutech.mymanjeri.contact_activities.LPUPFragment;


public class InstitutionSectionAdapter extends FragmentPagerAdapter {

    public InstitutionSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LPUPFragment();
            case 1:
                return new HSCFragment();
            case 2:
                return new EnglishMediumFragment();
            case 3:
                return new CollegeFragment();
            case 4:
                return new CouchingCenterFragment();
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
                return "LP/UP";
            case 1:
                return "HSC";
            case 2:
                return "ENGLISH MEDIUM";
            case 3:
                return "COLLEGE";
            case 4:
                return "COUCHING CENTRE";

            default:
                return null;
        }
    }
}