package com.hudutech.mymanjeri.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.BloodDonor;

import java.util.List;


public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {

    private List<BloodDonor> bloodDonorList;
    private Context mContext;

    public BloodDonorAdapter(Context mContext, List<BloodDonor> bloodDonorList) {
        this.bloodDonorList = bloodDonorList;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donor_person, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BloodDonor bloodDonor = bloodDonorList.get(position);
        holder.mDesc.setText(bloodDonor.getFullName()+"\n"+bloodDonor.getPhoneNumber()+"\n"+bloodDonor.getAddress());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.default_user);


        Glide.with(mContext)
                .load(bloodDonor.getAvator())
                .apply(requestOptions)
                .into(holder.mBloodDonorImage);



    }


    @Override
    public int getItemCount() {
        return bloodDonorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDesc;
        ImageButton btnShare;
        ImageButton btnCall;


        ImageView mBloodDonorImage;


        public ViewHolder(final View itemView) {
            super(itemView);
           mDesc = itemView.findViewById(R.id.txt_donor_desc);
           btnShare = itemView.findViewById(R.id.btn_share);
           btnCall = itemView.findViewById(R.id.btn_call);
           mBloodDonorImage = itemView.findViewById(R.id.image_blood_donor);


        }

        @Override
        public void onClick(View view) {
            // [HANDLE CLICKS OF THE VIEWS]
        }
    }


}
