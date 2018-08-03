package com.hudutech.mymanjeri.contact_activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.contact_models.Municipality;

/**
 * A simple {@link Fragment} subclass.
 */
public class MunicipalityFragment extends Fragment {


    public MunicipalityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_municipality, container, false);
        TextView mName = v.findViewById(R.id.tv_name);
        TextView mDesignation = v.findViewById(R.id.tv_designation);
        TextView mWardNo = v.findViewById(R.id.tv_ward_no);
        TextView mWardName = v.findViewById(R.id.tv_ward_name);
        TextView mMobileNo = v.findViewById(R.id.tv_mobile);
        TextView mPhoneNo = v.findViewById(R.id.tv_phone);
        TextView mAddress = v.findViewById(R.id.tv_address);
        TextView mParty = v.findViewById(R.id.tv_party);
        ImageView imageView = v.findViewById(R.id.imageView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final Municipality mObject = (Municipality) bundle.getSerializable("municipality");
            if (mObject != null) {
                mName.setText(mObject.getName());
                mDesignation.setText(mObject.getDesignation());
                mWardNo.setText(mObject.getWardNo());
                mWardName.setText(mObject.getWardName());
                mMobileNo.setText(mObject.getMobileNumber());
                mPhoneNo.setText(mObject.getPhoneNumber());
                mAddress.setText(mObject.getAddress());
                mParty.setText(mObject.getParty());

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.no_barner);


                Glide.with(getContext())
                        .load(mObject.getPhotoUrl())
                        .apply(requestOptions)
                        .into(imageView);

                TextView call = v.findViewById(R.id.tv_call);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Config.call(getContext(), mObject.getPhoneNumber());
                    }
                });
            }
        }


        return v;
    }

}
