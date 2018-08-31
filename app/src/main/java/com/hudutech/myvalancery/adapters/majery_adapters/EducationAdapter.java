package com.hudutech.myvalancery.adapters.majery_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.majery_activities.EducationDetailActivity;
import com.hudutech.myvalancery.models.majery_models.Education;

import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    private List<Education> educationList;
    private Context mContext;

    public EducationAdapter(Context mContext, List<Education> educationList) {
        this.educationList = educationList;
        this.mContext = mContext;

    }

    @Override
    public EducationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_education_item, parent, false);
        return new EducationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Education education = educationList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(education.getImageUrl())
                .apply(requestOptions)
                .into(holder.imageView);

        holder.mTitle.setText(education.getPlaceName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, EducationDetailActivity.class)
                        .putExtra("education", education)
                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return educationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView imageView;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = itemView.findViewById(R.id.tv_education_title);
            imageView = itemView.findViewById(R.id.img_education_icon);


        }


    }

}