package com.hudutech.mymanjeri.adapters.majery_adapters;

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
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.majery_activities.TourismDetailActivity;
import com.hudutech.mymanjeri.models.majery_models.TourismPlace;

import java.util.List;

public class TourismListAdapter extends RecyclerView.Adapter<TourismListAdapter.ViewHolder> {

    private List<TourismPlace> tourismPlaceList;
    private Context mContext;

    public TourismListAdapter(Context mContext, List<TourismPlace> tourismPlaceList) {
        this.tourismPlaceList = tourismPlaceList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public TourismListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_film_item, parent, false);
        return new TourismListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TourismPlace tourismPlace = tourismPlaceList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(tourismPlace.getImageUrl())
                .apply(requestOptions)
                .into(holder.imageView);

        holder.textView.setText(tourismPlace.getPlaceName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, TourismDetailActivity.class)
                        .putExtra("tourismPlace", tourismPlace)
                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return tourismPlaceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);

        }

    }
}
