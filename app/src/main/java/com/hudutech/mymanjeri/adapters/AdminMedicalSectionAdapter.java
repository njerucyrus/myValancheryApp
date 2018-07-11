package com.hudutech.mymanjeri.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.mymanjeri.admin_medical.DoctorFragment;
import com.hudutech.mymanjeri.admin_medical.HospitalFragment;
import com.hudutech.mymanjeri.admin_medical.LabFragment;
import com.hudutech.mymanjeri.admin_medical.MedicalShopFragment;
import com.hudutech.mymanjeri.admin_medical.OpticalFragment;


public class AdminMedicalSectionAdapter extends FragmentPagerAdapter {

    public AdminMedicalSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HospitalFragment();
            case 1:
                return new DoctorFragment();
            case 2:
                return new LabFragment();
            case 3:
                return  new MedicalShopFragment();
            case 4:
                return new OpticalFragment();
            default:
                return  null;
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
                return "Hospitals";
            case 1:
                return "Doctors";
            case 2:
                return "Labs";
            case 3:
                return "Medical Shops";
            case 4:
                return "Optical";
            default:
                return null;
        }
    }
}