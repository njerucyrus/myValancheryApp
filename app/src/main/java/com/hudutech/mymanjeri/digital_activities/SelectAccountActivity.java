package com.hudutech.mymanjeri.digital_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.digital_models.SBankAccount;


public class SelectAccountActivity extends AppCompatActivity {
    private TextInputEditText mSelectedAccount;
    private Button mButtonSubmit;
    private SBankAccount account;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        getSupportActionBar().setTitle("Select Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);

        mSelectedAccount = findViewById(R.id.txt_select_account_no);
        mButtonSubmit = findViewById(R.id.btn_select_account);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mSelectedAccount.getText().toString().trim())) {

                    goToAccount(mSelectedAccount.getText().toString().trim());

                } else {
                    Snackbar.make(v, "Account Number Required", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goToAccount(String accountNo) {
        mProgress.setMessage("getting account data please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        CollectionReference ref = FirebaseFirestore.getInstance().collection("sb_accounts");
        ref.whereEqualTo("accountNo", accountNo)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {

                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                account = snapshot.toObject(SBankAccount.class);
                            }

                            SharedPreferences sharedPrefs = getSharedPreferences("SELECTED_ACC_USER",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                            sharedPrefEditor.putString("userUid", account.getUserUid());
                            sharedPrefEditor.apply();
                            sharedPrefEditor.commit();


                            startActivity(new Intent(getApplicationContext(), StatementsActivity.class)
                                    .putExtra("account", account)
                            );


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                    }
                });
    }
}
