package com.hudutech.myvalancery.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.myvalancery.admin_contacts.AddEmergencyFragment;
import com.hudutech.myvalancery.admin_contacts.ContactAddGeneralFragment;
import com.hudutech.myvalancery.admin_contacts.ContactArtAndCultureFragment;
import com.hudutech.myvalancery.admin_contacts.ContactBanksFragment;
import com.hudutech.myvalancery.admin_contacts.ContactInstitutionFragment;
import com.hudutech.myvalancery.admin_contacts.ContactLabourerFragment;
import com.hudutech.myvalancery.admin_contacts.ContactMediaFragment;
import com.hudutech.myvalancery.admin_contacts.ContactMlaFragment;
import com.hudutech.myvalancery.admin_contacts.ContactMpFragment;
import com.hudutech.myvalancery.admin_contacts.ContactMunicipalityFragment;
import com.hudutech.myvalancery.admin_contacts.ContactPoliticsFragment;
import com.hudutech.myvalancery.admin_contacts.ContactProfessionalFragment;
import com.hudutech.myvalancery.admin_contacts.ContactVehicleFragment;


public class AdminContactSectionAdapter extends FragmentPagerAdapter {

    public AdminContactSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ContactVehicleFragment();
            case 1:
                return new AddEmergencyFragment();
            case 2:
                return new ContactAddGeneralFragment();
            case 3:
                return new ContactMpFragment();
            case 4:
                return new ContactMlaFragment();
            case 5:
                return new ContactPoliticsFragment();
            case 6:
                return new ContactMediaFragment();
            case 7:
                return new ContactArtAndCultureFragment();
            case 8:
                return new ContactMunicipalityFragment();
            case 9:
                return new ContactBanksFragment();
            case 10:
                return new ContactProfessionalFragment();
            case 11:
                return new ContactLabourerFragment();
            case 12:
                return new ContactInstitutionFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 13;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "VEHICLE";
            case 1:
                return "EMERGENCY";
            case 2:
                return "GENERAL";
            case 3:
                return "MP";
            case 4:
                return "MLA";
            case 5:
                return "POLITICS";
            case 6:
                return "MEDIA";
            case 7:
                return "ART & CULTURE";
            case 8:
                return "MUNICIPALITY";
            case 9:
                return "BANKS";
            case 10:
                return "PROFESSIONALS";
            case 11:
                return "LABOURERS";
            case 12:
                return "INSTITUTIONS";
            default:
                return null;
        }
    }
}