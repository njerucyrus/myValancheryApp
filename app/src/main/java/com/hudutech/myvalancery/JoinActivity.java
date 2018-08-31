package com.hudutech.myvalancery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.myvalancery.models.User;
import com.hudutech.myvalancery.user.UserPanelActivity;

public class JoinActivity extends AppCompatActivity {
    private TextInputEditText mFullName;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mPassword;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        getSupportActionBar().setTitle("Join");
        mFullName = findViewById(R.id.txt_fullname);
        mPhoneNumber = findViewById(R.id.txt_phone_number);
        mPassword = findViewById(R.id.txt_password);
        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseFirestore.getInstance().collection("users");

        watchInputs();

        Button mRegisterButton = findViewById(R.id.btn_join);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    signUp(mPhoneNumber.getText().toString() + "@test.com",
                            mFullName.getText().toString().trim(),
                            mPhoneNumber.getText().toString().trim(),
                            mPassword.getText().toString().trim());
                } else {
                    Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        TextView login = findViewById(R.id.tv_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinActivity.this, LoginUserActivity.class));
            }
        });

    }


    private void signUp(final String email, final String fullName, final String phoneNumber, String password) {

        mProgress.setTitle("Creating account.");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference docRef = usersRef.document(mAuth.getCurrentUser().getUid());

                            // create user account profile

                            User user = new User(
                                    email,
                                    fullName,
                                    phoneNumber,
                                    false,
                                    false
                            );

                            docRef.set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                                                    Context.MODE_PRIVATE);
                                            SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                                            sharedPrefEditor.putBoolean("isAdmin", false);
                                            sharedPrefEditor.putBoolean("isSBAdmin", false);
                                            sharedPrefEditor.apply();
                                            sharedPrefEditor.commit();
                                            FirebaseUser mUser = mAuth.getCurrentUser();
                                            updateUI(mUser);
                                        }
                                    })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            updateUI(null);
                                        }
                                    });
                        } else if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                updateUI(null);
                                mPassword.setError("Password too Weak.");
                                mPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                mFullName.setError("Account with this email already exists");
                                mFullName.requestFocus();
                                updateUI(null);
                            } catch (FirebaseTooManyRequestsException e) {

                                Snackbar.make(mFullName, "App Encountered internal error try again later",
                                        Snackbar.LENGTH_LONG).show();
                                updateUI(null);
                            } catch (Exception e) {
                                updateUI(null);
                                Log.e("REG_AUTH_ERROR", e.getMessage());
                            }
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }
        } else {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(JoinActivity.this, UserPanelActivity.class));
            finish();
        }
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

        if (TextUtils.isEmpty(mPassword.getText().toString().trim())) {
            mPassword.setError("This field is required");
            valid = false;
        } else {
            mPassword.setError(null);
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


        //watch email
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPassword.setError(null);
            }
        });
    }
}
