package com.hudutech.myvalanchery.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalanchery.admin_majery.AddBloodBankFragment;
import com.hudutech.myvalanchery.admin_majery.AddEducationFragment;
import com.hudutech.myvalanchery.admin_majery.AddHistoryFragment;
import com.hudutech.myvalanchery.admin_majery.AddNewsFragment;
import com.hudutech.myvalanchery.admin_majery.AddNotificationFragment;
import com.hudutech.myvalanchery.admin_majery.AddShoppingFragment;
import com.hudutech.myvalanchery.admin_majery.AddTourismFragment;


public class AdminMajerySectionAdapter extends FragmentPagerAdapter {

    public AdminMajerySectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddNewsFragment();


            case 1:
                return new AddBloodBankFragment();

            case 2:
                return new AddTourismFragment();
            case 3:
                return new AddHistoryFragment();
            case 4:
                return new AddShoppingFragment();

            case 5:
                return new AddNotificationFragment();

            case 6:
                return new AddEducationFragment();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "NEWS";
            case 1:
                return "BLOOD BANK";

            case 2:
                return "TOURISM";

            case 3:
                return "HISTORY";
            case 4:
                return "SHOPPING";
            case 5:
                return "NOTIFICATION";
            case 6:
                return "EDUCATION";
            default:
                return null;
        }
    }
}