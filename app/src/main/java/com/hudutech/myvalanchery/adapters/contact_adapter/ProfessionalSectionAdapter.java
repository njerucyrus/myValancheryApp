package com.hudutech.myvalanchery.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalanchery.contact_activities.AccountantFragment;
import com.hudutech.myvalanchery.contact_activities.AdvocatesFragment;
import com.hudutech.myvalanchery.contact_activities.DocumentWritingFragment;
import com.hudutech.myvalanchery.contact_activities.EngineersFragment;
import com.hudutech.myvalanchery.contact_activities.GraphicDesigningFragment;


public class ProfessionalSectionAdapter extends FragmentPagerAdapter {

    public ProfessionalSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EngineersFragment();
            case 1:
                return new AdvocatesFragment();
            case 2:
                return new DocumentWritingFragment();

            case 3:
                return new AccountantFragment();

            case 4:
                return new GraphicDesigningFragment();

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
                return "Engineers";
            case 1:
                return "Advocates";
            case 2:
                return "Document Writing";
            case 3:
                return "Accountant";
            case 4:
                return "Graphic Designing";
            default:
                return null;
        }
    }
}