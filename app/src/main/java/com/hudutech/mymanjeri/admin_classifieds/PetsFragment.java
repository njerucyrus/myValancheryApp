package com.hudutech.mymanjeri.admin_classifieds;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudutech.mymanjeri.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetsFragment extends Fragment {


    public PetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pets, container, false);
    }

}
