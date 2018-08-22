package com.hudutech.myvalanchery.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.admin_banner_setting.EditBannerActivity;
import com.hudutech.myvalanchery.models.Banner;

import java.util.List;


public class BarnerAdminAdapter extends RecyclerView.Adapter<BarnerAdminAdapter.ViewHolder> {

    private static final String TAG = "BarnerAdminAdapter";
    private List<Banner> barnerList;
    private Context mContext;

    public BarnerAdminAdapter(Context mContext, List<Banner> barnerList) {
        this.barnerList = barnerList;
        this.mContext = mContext;

    }

    @Override
    public BarnerAdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.barner_item_admin, parent, false);
        return new BarnerAdminAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BarnerAdminAdapter.ViewHolder holder, int position) {
        final Banner barner = barnerList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(barner.getBannerUrl())
                .apply(requestOptions)
                .into(holder.mBarnerImage);

        holder.mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext.getApplicationContext(), EditBannerActivity.class)
                        .putExtra("barner", barner)
                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return barnerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mBarnerImage;
        Button mChange;


        public ViewHolder(final View itemView) {
            super(itemView);
            mBarnerImage = itemView.findViewById(R.id.barner_image_item_admin);
            mChange = itemView.findViewById(R.id.btn_delete_barner);


        }


    }


}

