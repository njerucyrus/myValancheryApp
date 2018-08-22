package com.hudutech.myvalanchery.adapters.timming_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalanchery.admin_timing_and_booking.BusFragment;
import com.hudutech.myvalanchery.admin_timing_and_booking.FilmFragment;
import com.hudutech.myvalanchery.admin_timing_and_booking.HotelFragment;
import com.hudutech.myvalanchery.admin_timing_and_booking.RestaurantFragment;
import com.hudutech.myvalanchery.admin_timing_and_booking.TravelFragment;


public class TimingSectionAdapter extends FragmentPagerAdapter {

    public TimingSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BusFragment();
            case 1:
                return new FilmFragment();
            case 2:
                return new HotelFragment();
            case 3:
                return new TravelFragment();
            case 4:
                return new RestaurantFragment();

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
                return "Bus";
            case 1:
                return "Film";
            case 2:
                return "Hotel";
            case 3:
                return "Travel";
            case 4:
                return "Restaurant";

            default:
                return null;
        }
    }
}