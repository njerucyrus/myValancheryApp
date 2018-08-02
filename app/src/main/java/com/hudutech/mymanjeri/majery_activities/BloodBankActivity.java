package com.hudutech.mymanjeri.majery_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.admin_majery.AddBloodDonorAdminActivity;

public class BloodBankActivity extends AppCompatActivity implements View.OnClickListener {
    private String bloodGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView0 = findViewById(R.id.img_a_plus);
        ImageView imageView1 = findViewById(R.id.img_b_plus);
        ImageView imageView2 = findViewById(R.id.img_o_plus);
        ImageView imageView3 = findViewById(R.id.img_ab_plus);
        ImageView imageView4 = findViewById(R.id.img_a_minus);
        ImageView imageView5 = findViewById(R.id.img_b_minus);
        ImageView imageView6 = findViewById(R.id.img_o_minus);
        ImageView imageView7 = findViewById(R.id.img_ab_minus);
        ImageView imageView8 = findViewById(R.id.img_become_donor);

        imageView0.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.img_a_plus:
                bloodGroup = "A+";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;
            case R.id.img_b_plus:
                bloodGroup = "B+";
                showActivity(BloodGroupListActivity.class, bloodGroup);

                break;
            case R.id.img_o_plus:
                bloodGroup = "O+";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;
            case R.id.img_ab_plus:
                bloodGroup = "AB+";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;
            case R.id.img_a_minus:
                bloodGroup = "A-";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;
            case R.id.img_b_minus:
                bloodGroup = "B-";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;
            case R.id.img_o_minus:
                bloodGroup = "O-";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;
            case R.id.img_ab_minus:
                bloodGroup = "AB-";
                showActivity(BloodGroupListActivity.class, bloodGroup);
                break;

            case R.id.img_become_donor:
                showActivity(AddBloodDonorAdminActivity.class, "");
                break;


        }
    }

    private void showActivity(Class<?> klass, String bloodGroup) {
        startActivity(new Intent(this, klass)
                .putExtra("bloodGroup", bloodGroup)
        );
    }
}
