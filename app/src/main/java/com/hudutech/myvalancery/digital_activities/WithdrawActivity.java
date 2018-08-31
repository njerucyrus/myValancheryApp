package com.hudutech.myvalancery.digital_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.digital_models.SBankAccount;
import com.hudutech.myvalancery.models.digital_models.TransactionRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WithdrawActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WithdrawActivity";
    private static final int OPTION_WITHDRAW = 1;
    private TextInputEditText mAccountNo;
    private TextInputEditText mAmount;
    private TextInputEditText mPurpose;
    private TextInputEditText mDate;
    private TextView tvHolderName;
    private Button mButtonWithdraw;
    private SBankAccount account;
    private ProgressDialog mProgress;
    private long delay = 1000; // 1 seconds after user stops typing
    private long lastTextEdit = 0;
    private Handler handler = new Handler();
    private Runnable inputFinishChecker;

    private CollectionReference mAccountRef;
    private CollectionReference mTransactionsRef;
    private SimpleDateFormat mSimpleDateFormat;
    private Calendar mCalendar;
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));

        }
    };
    /* Define the onClickListener, and start the DatePickerDialog with users current time */
    private final View.OnClickListener textListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCalendar = Calendar.getInstance();
            new DatePickerDialog(WithdrawActivity.this, mDateDataSet, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        mAccountRef = FirebaseFirestore.getInstance().collection("sb_accounts");
        mTransactionsRef = FirebaseFirestore.getInstance().collection("sb_transactions");
        mAccountNo = findViewById(R.id.txt_sb_withdraw_account_no);
        mAmount = findViewById(R.id.txt_sb_withdraw_amount);
        mDate = findViewById(R.id.txt_sb_withdraw_date);
        mPurpose = findViewById(R.id.txt_sb_withdraw_purpose);
        tvHolderName = findViewById(R.id.tv_sb_withdraw_account_holder);
        mButtonWithdraw = findViewById(R.id.btn_withdraw);
        mProgress = new ProgressDialog(this);


        mButtonWithdraw.setOnClickListener(this);
        mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mDate.setOnClickListener(textListener);
        mDate.addTextChangedListener(new WithdrawActivity.DateTextWatcher());

        Date initialDate = new Date();
        String formattedDate = mSimpleDateFormat.format(initialDate);
        mDate.setText(formattedDate);

        watchAccountNo();


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_withdraw) {
            if (validateInputs()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Transaction");
                builder.setMessage("Withdraw INR " + mAmount.getText().toString().trim());
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new Date(mDate.getText().toString());
                        makeWithdraw(mAccountNo.getText().toString().trim(), Float.parseFloat(mAmount.getText().toString().trim()), mPurpose.getText().toString().trim(), date);


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void makeWithdraw(String accountNo, float amount, String purpose, Date date) {
        final float newBalance = account.getBalance() - amount;

        DocumentReference docRef = mTransactionsRef.document();
        String desc = "Withdraw INR " + amount;
        TransactionRecord record = new TransactionRecord(
                docRef.getId(),
                account.getUserUid(),
                accountNo,
                OPTION_WITHDRAW,
                purpose,
                amount,
                desc,
                date

        );

        mProgress.setMessage("Processing request please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        docRef.set(record)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update the account balance.
                        if (mProgress.isShowing()) mProgress.dismiss();
                        mAccountRef.document(account.getDocKey()).update("balance", newBalance);
                        Toast.makeText(getApplicationContext(), "Transaction Completed Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(WithdrawActivity.this, SBAdminPanelActivity.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(WithdrawActivity.this, "Error occurred please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void watchAccountNo() {
        inputFinishChecker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (lastTextEdit + delay - 500)) {
                    checkAccount();
                }
            }
        };

        mAccountNo.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before,
                                              int count) {
                        //You need to remove this to run only once
                        handler.removeCallbacks(inputFinishChecker);

                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        //avoid triggering event when text is empty
                        if (s.length() > 0) {
                            lastTextEdit = System.currentTimeMillis();
                            handler.postDelayed(inputFinishChecker, delay);
                        } else {

                        }
                    }
                }
        );
    }

    private void checkAccount() {
        String query = mAccountNo.getText().toString().trim();
        if (!TextUtils.isEmpty(query))
            mAccountRef.whereEqualTo("accountNo", query)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.getDocuments().size() > 0) {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    account = snapshot.toObject(SBankAccount.class);
                                    tvHolderName.setVisibility(View.VISIBLE);
                                    if (account != null)
                                        tvHolderName.setText(account.getCustomerName());
                                }
                            } else {
                                tvHolderName.setVisibility(View.GONE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: " + e.getMessage());
                        }
                    });


    }

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mAccountNo.getText().toString().trim())) {
            mAccountNo.setError("*Required");
            valid = false;
        } else {
            mAccountNo.setError(null);
        }

        if (TextUtils.isEmpty(mAmount.getText().toString().trim())) {
            mAmount.setError("*Required");
            valid = false;
        } else {
            mAmount.setError(null);
        }

        if (TextUtils.isEmpty(mDate.getText().toString().trim())) {
            mDate.setError("*Required");
            valid = false;
        } else {
            mDate.setError(null);
        }

        if (TextUtils.isEmpty(tvHolderName.getText().toString().trim())) {
            mAccountNo.setError("*Invalid account no");
            valid = false;
        } else {
            mAccountNo.setError(null);
        }

        if (TextUtils.isEmpty(mPurpose.getText().toString().trim())) {
            mPurpose.setError("*Invalid account no");
            valid = false;
        } else {
            mPurpose.setError(null);
        }

        return valid;

    }

    // FOR FORMATTING DATE INPUT TO AVOID APP CRASHING DUE OT INVALID DATES
    public class DateTextWatcher implements TextWatcher {

        public static final int MAX_FORMAT_LENGTH = 8;
        public static final int MIN_FORMAT_LENGTH = 3;

        private String updatedText;
        private boolean editing;


        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
            if (text.toString().equals(updatedText) || editing) return;

            String digitsOnly = text.toString().replaceAll("\\D", "");
            int digitLen = digitsOnly.length();

            if (digitLen < MIN_FORMAT_LENGTH || digitLen > MAX_FORMAT_LENGTH) {
                updatedText = digitsOnly;
                return;
            }

            if (digitLen <= 4) {
                String month = digitsOnly.substring(0, 2);
                String day = digitsOnly.substring(2);

                updatedText = String.format(Locale.US, "%s/%s", month, day);
            } else {
                String month = digitsOnly.substring(0, 2);
                String day = digitsOnly.substring(2, 4);
                String year = digitsOnly.substring(4);

                updatedText = String.format(Locale.US, "%s/%s/%s", month, day, year);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editing) return;

            editing = true;

            editable.clear();
            editable.insert(0, updatedText);

            editing = false;
        }
    }
}
