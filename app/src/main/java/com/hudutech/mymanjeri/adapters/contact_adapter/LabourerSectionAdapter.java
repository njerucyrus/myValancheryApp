package com.hudutech.mymanjeri.adapters.contact_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hudutech.mymanjeri.contact_activities.ChefFragment;
import com.hudutech.mymanjeri.contact_activities.ContractorsFragment;
import com.hudutech.mymanjeri.contact_activities.CurtainSofaFragment;
import com.hudutech.mymanjeri.contact_activities.ElectricalFragment;
import com.hudutech.mymanjeri.contact_activities.FabricationGlassFragment;
import com.hudutech.mymanjeri.contact_activities.FridgeACFragment;
import com.hudutech.mymanjeri.contact_activities.IndustrialWorkFragment;
import com.hudutech.mymanjeri.contact_activities.LabourersFragment;
import com.hudutech.mymanjeri.contact_activities.PaintingFragment;
import com.hudutech.mymanjeri.contact_activities.TailoringFragment;


public class LabourerSectionAdapter extends FragmentPagerAdapter {

    public LabourerSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ContractorsFragment();
            case 1:
                return new TailoringFragment();
            case 2:
                return new ElectricalFragment();
            case 3:
                return new FridgeACFragment();
            case 4:
                return new ChefFragment();
            case 5:
                return new IndustrialWorkFragment();
            case 6:
                return new CurtainSofaFragment();
            case 7:
                return new FabricationGlassFragment();
            case 8:
                return new LabourersFragment();
            case 9:
                return new PaintingFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);

        switch (position) {
            case 0:
                return "Contractors";
            case 1:
                return "Tailoring";
            case 2:
                return "Electrical";
            case 3:
                return "Fridge & AC";
            case 4:
                return "Chef";
            case 5:
                return "Industrial Work";
            case 6:
                return "Curtain & Sofa";
            case 7:
                return "Fabrication & Glass";
            case 8:
                return "Labourers";
            case 9:
                return "Painting";
            default:
                return null;
        }
    }
}