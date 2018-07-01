package com.hudutech.mymanjeri.digital_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.hudutech.mymanjeri.MainActivity;
import com.hudutech.mymanjeri.R;

public class SBAdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbadmin_panel);
        getSupportActionBar().setTitle("SBank Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView createAccount = findViewById(R.id.card_view_sb_create_acc);
        createAccount.setOnClickListener(this);

        CardView withdraw = findViewById(R.id.card_view_sb_withdraw);
        withdraw.setOnClickListener(this);

        CardView deposit = findViewById(R.id.card_view_sb_deposit);
        deposit.setOnClickListener(this);

        CardView balances = findViewById(R.id.card_view_sb_balances);
        balances.setOnClickListener(this);

        CardView statements = findViewById(R.id.card_view_sb_statements);
        statements.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.card_view_sb_create_acc:
                showActivity(CreateAccountActivity.class);
                break;
            case R.id.card_view_sb_deposit:
                showActivity(DepositActivity.class);
                break;
            case R.id.card_view_sb_withdraw:
                showActivity(WithdrawActivity.class);
                break;
            case R.id.card_view_sb_balances:
                showActivity(ManageSBAccountsActivity.class);
                break;
            case R.id.card_view_sb_statements:
                showActivity(SelectAccountActivity.class);
                break;
        }
    }

    private void showActivity(Class<?> klass) {
        startActivity(new Intent(this, klass));
    }
}
