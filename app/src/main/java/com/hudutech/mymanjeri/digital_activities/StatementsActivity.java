package com.hudutech.mymanjeri.digital_activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.MainActivity;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.digital_adapters.AccountProfileSectionAdapter;
import com.hudutech.mymanjeri.models.digital_models.SBankAccount;

public class StatementsActivity extends AppCompatActivity {

    private SBankAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        getSupportActionBar().setTitle("My Account Profile");

        account = (SBankAccount) getIntent().getSerializableExtra("account");

        ImageView profileImage = findViewById(R.id.img_sb_account_profile);
        TextView tvBalance = findViewById(R.id.tv_sb_account_balance);
        TextView tvHolderName = findViewById(R.id.tv_sb_account_profile_holdername);
        TextView tvAccountNo = findViewById(R.id.tv_sb_profile_account_no);
        TextView tvBatchNo = findViewById(R.id.tv_sb_profile_batch_no);

        String bal = "Acc Balance: INR "+account.getBalance();
        String holderName = "Holder Name: "+account.getCustomerName();
        String holderAccountNo = "Account No: "+account.getAccountNo();
        String holderBatchNo = "Batch No: "+account.getBatchNo();


        tvBalance.setText(bal);
        tvHolderName.setText(holderName);
        tvAccountNo.setText(holderAccountNo);
        tvBatchNo.setText(holderBatchNo);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.user_96);

        Glide.with(this)
                .load(account.getPhotoUrl())
                .apply(requestOptions)
                .into(profileImage);


        ViewPager mViewPager = findViewById(R.id.profile_view_pager);
        AccountProfileSectionAdapter mSectionsPagerAdapter = new AccountProfileSectionAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout mTabLayout = findViewById(R.id.account_profile_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
