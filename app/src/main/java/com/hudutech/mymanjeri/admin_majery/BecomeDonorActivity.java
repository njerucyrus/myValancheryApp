package com.hudutech.mymanjeri.admin_majery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.majery_activities.BloodBankActivity;
import com.hudutech.mymanjeri.models.majery_models.BloodDonor;

public class BecomeDonorActivity extends AppCompatActivity {
    private AppCompatSpinner mSpinner;
    private TextInputEditText mFullName;
    private TextInputEditText mPhoneNumber;
    private String bloodGroup;
    private DocumentReference bloodDonorsRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_donor);
        getSupportActionBar().setTitle("Become  A Donor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinner = findViewById(R.id.spinner_blood_group);
        mFullName = findViewById(R.id.txt_fullname);
        mPhoneNumber = findViewById(R.id.txt_phone_number);

        watchInputs();

        mProgress = new ProgressDialog(this);
        bloodDonorsRef = FirebaseFirestore.getInstance().collection("donors").document();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    bloodGroup = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bloodGroup = null;

            }
        });

        Button mDone = findViewById(R.id.btn_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    submitDonorData(mFullName.getText().toString(), mPhoneNumber.getText().toString().trim(), bloodGroup, true);
                } else {
                    Snackbar.make(v, "Fix errors above", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void submitDonorData(String fullName, String phoneNumber, String bloodGroup, boolean isValidated) {
        BloodDonor donor = new BloodDonor(
                bloodDonorsRef.getId(),
                fullName,
                phoneNumber,
                bloodGroup,
                "",
                "",
                "",
                true
        );

        mProgress.setMessage("Working please wait...");
        mProgress.show();

        bloodDonorsRef
                .set(donor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BecomeDonorActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), BloodBankActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BecomeDonorActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private boolean validateInput() {
        boolean valid = true;

        if (TextUtils.isEmpty(mFullName.getText().toString().trim())) {
            mFullName.setError("This field is required");
            valid = false;
        } else {
            mFullName.setError(null);
        }
        if (TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())) {
            mPhoneNumber.setError("This field is required");
            valid = false;
        } else {
            mPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(bloodGroup)) {
            valid = false;
            Toast.makeText(this, "Select Blood Group", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private void watchInputs() {
        mFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mFullName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mFullName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mFullName.setError(null);
            }
        });


        //watch email
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPhoneNumber.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPhoneNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPhoneNumber.setError(null);
            }
        });
    }
}
