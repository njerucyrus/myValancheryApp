package com.hudutech.mymanjeri.timing_and_booking_activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.ImageViewPagerAdapter;
import com.hudutech.mymanjeri.models.Banner;
import com.hudutech.mymanjeri.models.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelDetailActivity extends AppCompatActivity {
    LinearLayout dotsLayout;
    ViewPager viewPager;
    private int dotsCount;
    private ImageView[] dots;
    private Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        getSupportActionBar().setTitle(hotel.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView hotelName = findViewById(R.id.tv_name);
        hotelName.setText(hotel.getName());

        TextView address = findViewById(R.id.tv_address);
        address.setText(hotel.getPlaceName());

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(hotel.getStarRating());
        ratingBar.setIsIndicator(false);

        TextView details = findViewById(R.id.tv_details);
        details.setText(hotel.getMoreInfo());

        TextView call = findViewById(R.id.tv_call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(HotelDetailActivity.this, hotel.getPhoneNumber());
            }
        });


        List<Banner> bannerList = new ArrayList<>();
        for (String imageUrl : hotel.getPhotoUrls()) {
            bannerList.add(new Banner(imageUrl, "", 0, false));
        }
        viewPager = findViewById(R.id.viewPager);
        ImageViewPagerAdapter viewPagerAdapter = new ImageViewPagerAdapter(this, bannerList);
        viewPager.setAdapter(viewPagerAdapter);

        dotsLayout = findViewById(R.id.slider_dots);
        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dot_inactive));
            dots[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dot_active));
        dots[0].setScaleType(ImageView.ScaleType.CENTER_CROP);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dot_inactive));

                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dot_active));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_share) {
            String message = hotel.getStarRating() + "-Star Hotel, Name " + hotel.getName() + " Address " + hotel.getAddress() + ", " + hotel.getPlaceName();
            Config.share(HotelDetailActivity.this, "Hotel", message);
        }

        return super.onOptionsItemSelected(item);
    }
}
