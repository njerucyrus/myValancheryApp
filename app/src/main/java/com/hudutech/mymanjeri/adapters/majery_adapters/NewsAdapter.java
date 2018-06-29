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
import com.hudutech.mymanjeri.majery_activities.NewsDetailActivity;
import com.hudutech.mymanjeri.models.majery_models.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private Context mContext;

    public NewsAdapter(Context mContext, List<News> newsList) {
        this.newsList = newsList;
        this.mContext = mContext;

    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, parent, false);
        return new NewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final News news = newsList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(news.getImageUrl())
                .apply(requestOptions)
                .into(holder.imageView);

        holder.mHeadline.setText(news.getNewsHeading());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, NewsDetailActivity.class)
                        .putExtra("news",news)
                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHeadline;
        ImageView imageView;
        TextView mNews;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mHeadline = itemView.findViewById(R.id.tv_news_headline);
            mNews = itemView.findViewById(R.id.tv_news_content);
            imageView = itemView.findViewById(R.id.img_news_icon);


        }


    }
}
