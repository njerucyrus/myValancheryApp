package com.hudutech.myvalanchery.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalanchery.contact_activities.ChannelsFragment;
import com.hudutech.myvalanchery.contact_activities.NewsPaperFragment;


public class MediaSectionAdapter extends FragmentPagerAdapter {

    public MediaSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsPaperFragment();
            case 1:
                return new ChannelsFragment();

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
                return "News Paper";
            case 1:
                return "Channels";
            default:
                return null;
        }
    }
}