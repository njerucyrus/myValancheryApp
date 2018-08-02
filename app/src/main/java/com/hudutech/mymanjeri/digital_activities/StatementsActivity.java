package com.hudutech.mymanjeri.digital_activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.hudutech.mymanjeri.MainActivity;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.digital_adapters.AccountProfileSectionAdapter;
import com.hudutech.mymanjeri.models.digital_models.SBankAccount;

public class StatementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        getSupportActionBar().setTitle("My Account Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SBankAccount account = (SBankAccount) getIntent().getSerializableExtra("account");

        ImageView profileImage = findViewById(R.id.img_sb_account_profile);
        TextView tvBalance = findViewById(R.id.tv_sb_account_balance);
        TextView tvHolderName = findViewById(R.id.tv_sb_account_profile_holdername);
        TextView tvAccountNo = findViewById(R.id.tv_sb_profile_account_no);
        TextView tvBatchNo = findViewById(R.id.tv_sb_profile_batch_no);

        String bal = "Acc Balance: INR " + account.getBalance();
        String holderName = "Holder Name: " + account.getCustomerName();
        String holderAccountNo = "Account No: " + account.getAccountNo();
        String holderBatchNo = "Batch No: " + account.getBatchNo();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_user_panel);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        if (id == R.id.action_logout) {
            signOut();
        }


        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to logout?");
        builder.setMessage("You will be logged out");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                sharedPrefEditor.putBoolean("isAdmin", false);
                sharedPrefEditor.putBoolean("isSBAdmin", false);
                sharedPrefEditor.apply();
                sharedPrefEditor.commit();
                Toast.makeText(StatementsActivity.this, "You Are logged out", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }
}
