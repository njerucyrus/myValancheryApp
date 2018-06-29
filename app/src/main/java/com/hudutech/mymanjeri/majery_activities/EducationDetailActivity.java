package com.hudutech.mymanjeri.majery_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.Education;

public class EducationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_detail);
        Education education = (Education) getIntent().getSerializableExtra("education");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(this)
                .load(education.getImageUrl())
                .apply(requestOptions)
                .into((ImageView) findViewById(R.id.img_education_detail_icon));

        TextView mTitle = findViewById(R.id.tv_education_detail_title);
        mTitle.setText(education.getPlaceName());

        TextView mContent = findViewById(R.id.tv_education_content);
        mContent.setText(education.getDescription());
    }
}
