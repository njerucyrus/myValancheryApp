package com.hudutech.myvalanchery.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalanchery.contact_activities.AmbulanceFragment;
import com.hudutech.myvalanchery.contact_activities.FireForceFragment;
import com.hudutech.myvalanchery.contact_activities.KSEBFragment;
import com.hudutech.myvalanchery.contact_activities.KSRTCFragment;
import com.hudutech.myvalanchery.contact_activities.PoliceFragment;
import com.hudutech.myvalanchery.contact_activities.RailwayFragment;


public class EmergencySectionAdapter extends FragmentPagerAdapter {

    public EmergencySectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AmbulanceFragment();
            case 1:
                return new PoliceFragment();
            case 2:
                return new FireForceFragment();
            case 3:
                return new RailwayFragment();
            case 4:
                return new KSEBFragment();
            case 5:
                return new KSRTCFragment();
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
                return "Ambulance";
            case 1:
                return "Police";
            case 2:
                return "FireForce";
            case 3:
                return "Railway";

            case 4:
                return "KSEB";

            case 5:
                return "KSRTC";
            default:
                return null;
        }
    }
}