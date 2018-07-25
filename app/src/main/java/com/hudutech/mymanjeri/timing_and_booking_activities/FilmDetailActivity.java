package com.hudutech.mymanjeri.timing_and_booking_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.Film;

public class FilmDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        getSupportActionBar().setTitle("Film");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Film film = (Film)getIntent().getSerializableExtra("film");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(this)
                .load(film.getPhotoUrl())
                .apply(requestOptions)
                .into((ImageView) findViewById(R.id.img_film_photo));

        TextView mTitle = findViewById(R.id.tv_title);
        mTitle.setText(film.getTitle());

        TextView mPlace = findViewById(R.id.tv_place);
        mPlace.setText(film.getPlace());

        TextView mAddress = findViewById(R.id.tv_address);
        mAddress.setText(film.getAddress());

        TextView mShows = findViewById(R.id.tv_shows);
        mShows.setText(film.getShows() +" Show(s)");


        TextView mTimes = findViewById(R.id.tv_show_times);
        StringBuilder times = new StringBuilder();
        for (String time : film.getTimes()) {
            times.append("\t").append(time);
        }
        mTimes.setText(times);

        TextView tvCall = findViewById(R.id.tv_call);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(FilmDetailActivity.this, film.getPhoneNumber());
            }
        });



    }
}
