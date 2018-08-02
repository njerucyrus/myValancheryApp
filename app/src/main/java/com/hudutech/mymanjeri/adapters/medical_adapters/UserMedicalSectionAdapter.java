package com.hudutech.mymanjeri.adapters.medical_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.mymanjeri.medical_activities.AyourvedicHospitalFragment;
import com.hudutech.mymanjeri.medical_activities.DentalHospitalFragment;
import com.hudutech.mymanjeri.medical_activities.GeneralHospitalFragment;
import com.hudutech.mymanjeri.medical_activities.HomeoHospitalFragment;
import com.hudutech.mymanjeri.medical_activities.OphthalHospitalFragment;
import com.hudutech.mymanjeri.medical_activities.PhysioHospitalFragment;


public class UserMedicalSectionAdapter extends FragmentPagerAdapter {

    public UserMedicalSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GeneralHospitalFragment();
            case 1:
                return new HomeoHospitalFragment();
            case 2:
                return new AyourvedicHospitalFragment();
            case 3:
                return new DentalHospitalFragment();
            case 4:
                return new OphthalHospitalFragment();
            case 5:
                return new PhysioHospitalFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "General";
            case 1:
                return "Homeo";
            case 2:
                return "Ayurvedic";
            case 3:
                return "Dental";
            case 4:
                return "Ophthal";

            case 5:
                return "Physio";
            default:
                return null;
        }
    }
}