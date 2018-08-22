package com.hudutech.myvalanchery.digital_activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hudutech.myvalanchery.MainActivity;
import com.hudutech.myvalanchery.R;

public class SBAdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbadmin_panel);
        getSupportActionBar().setTitle("SBank Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


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
                Toast.makeText(SBAdminPanelActivity.this, "You Are logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SBAdminPanelActivity.this, MainActivity.class));
                finish();

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
