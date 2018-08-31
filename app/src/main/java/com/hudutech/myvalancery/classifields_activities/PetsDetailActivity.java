package com.hudutech.myvalancery.classifields_activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.adapters.ImageViewPagerAdapter;
import com.hudutech.myvalancery.models.Banner;
import com.hudutech.myvalancery.models.classifields_models.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetsDetailActivity extends AppCompatActivity {
    LinearLayout dotsLayout;
    ViewPager viewPager;
    private int dotsCount;
    private ImageView[] dots;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_detail);
        pet = (Pet) getIntent().getSerializableExtra("pet");
        getSupportActionBar().setTitle(pet.getDescription());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView hotelName = findViewById(R.id.tv_name);
        hotelName.setText(pet.getDescription());

        TextView price = findViewById(R.id.tv_price);
        price.setText("INR " + pet.getAmount());


        TextView address = findViewById(R.id.tv_location);
        address.setText(pet.getLocation());


        TextView brand = findViewById(R.id.tv_breed);
        brand.setText(pet.getBreed());

        TextView model = findViewById(R.id.tv_age);
        model.setText(String.valueOf(pet.getAge()));


        List<Banner> bannerList = new ArrayList<>();
        for (String imageUrl : pet.getPhotoUrls()) {
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

        TextView tvCall = findViewById(R.id.tv_call);

        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(PetsDetailActivity.this, pet.getPhoneNumber());
            }
        });

    }
}
