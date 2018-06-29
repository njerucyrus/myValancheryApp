package com.hudutech.mymanjeri.digital_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hudutech.mymanjeri.MainActivity;
import com.hudutech.mymanjeri.R;

public class StatementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        getSupportActionBar().setTitle("Transaction Statements");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
