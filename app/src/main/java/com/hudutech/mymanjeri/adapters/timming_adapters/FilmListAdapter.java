package com.hudutech.mymanjeri.adapters.timming_adapters;

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
import com.hudutech.mymanjeri.models.Film;
import com.hudutech.mymanjeri.timing_and_booking_activities.FilmDetailActivity;

import java.util.List;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {

    private List<Film> filmList;
    private Context mContext;

    public FilmListAdapter(Context mContext, List<Film> filmList) {
        this.filmList = filmList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public FilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_film_item, parent, false);
        return new FilmListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,  int position) {
        final Film film = filmList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(film.getPhotoUrl())
                .apply(requestOptions)
                .into(holder.imageView);

        holder.textView.setText(film.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, FilmDetailActivity.class)
                        .putExtra("film",film)
                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return filmList.size();
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
