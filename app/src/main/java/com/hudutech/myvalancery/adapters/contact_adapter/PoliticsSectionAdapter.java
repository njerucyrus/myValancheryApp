package com.hudutech.myvalancery.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalancery.contact_activities.BJMFragment;
import com.hudutech.myvalancery.contact_activities.CPIFragment;
import com.hudutech.myvalancery.contact_activities.CPIMFragment;
import com.hudutech.myvalancery.contact_activities.INCFragment;
import com.hudutech.myvalancery.contact_activities.INLFragment;
import com.hudutech.myvalancery.contact_activities.IUMLFragment;
import com.hudutech.myvalancery.contact_activities.SDPIFragment;
import com.hudutech.myvalancery.contact_activities.WPIFragment;


public class PoliticsSectionAdapter extends FragmentPagerAdapter {

    public PoliticsSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IUMLFragment();
            case 1:
                return new INCFragment();
            case 2:
                return new CPIMFragment();
            case 3:
                return new BJMFragment();
            case 4:
                return new CPIFragment();
            case 5:
                return new SDPIFragment();
            case 6:
                return new WPIFragment();
            case 7:
                return new INLFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "IUML";
            case 1:
                return "INC";
            case 2:
                return "CPIM";
            case 3:
                return "BJP";
            case 4:
                return "CPI";
            case 5:
                return "SDPI";
            case 6:
                return "WPI";
            case 7:
                return "INL";
            default:
                return null;
        }
    }
}