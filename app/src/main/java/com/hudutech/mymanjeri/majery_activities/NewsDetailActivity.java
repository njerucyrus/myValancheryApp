package com.hudutech.mymanjeri.majery_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.News;

public class NewsDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        News news = (News) getIntent().getSerializableExtra("news");

        getSupportActionBar().setTitle(news.getNewsHeading());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(this)
                .load(news.getImageUrl())
                .apply(requestOptions)
                .into((ImageView) findViewById(R.id.img_news_detail));

        TextView headline = findViewById(R.id.tv_news_detail_headline);
        headline.setText(news.getNewsHeading());

        TextView newsContent = findViewById(R.id.news_detail_content);
        newsContent.setText(news.getNews());

    }
}
