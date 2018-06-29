package com.hudutech.mymanjeri;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.mymanjeri.digital_activities.SBAdminPanelActivity;
import com.hudutech.mymanjeri.digital_activities.StatementsActivity;
import com.hudutech.mymanjeri.models.User;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("LOGIN");
        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mUsername = findViewById(R.id.txt_username);
        mPassword = findViewById(R.id.txt_password);

        Button mLoginButton = findViewById(R.id.btn_login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    signIn();
                } else {
                    Snackbar.make(view, "Please correct the errors above to continue",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        TextView tvRegister = findViewById(R.id.tv_create_account);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });


        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUsername.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUsername.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mUsername.setError(null);
            }
        });


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

    private void signIn() {


        mProgress.setTitle("Authenticating");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mAuth.signInWithEmailAndPassword(mUsername.getText().toString().trim(), mPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //login here
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            Snackbar.make(mPassword, "Login Successful", Snackbar.LENGTH_LONG).show();
                            initAccount(firebaseUser);

                        } else if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Snackbar.make(mPassword, "Invalid Email/Password", Snackbar.LENGTH_LONG).show();
                            } catch (FirebaseTooManyRequestsException e) {
                                Snackbar.make(mPassword, "Error! Unable to complete authentication please try again later", Snackbar.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Snackbar.make(mPassword, "Unable to authenticate your account please try again later", Snackbar.LENGTH_LONG).show();

                                Log.e(TAG, e.getMessage());
                            }
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }

            if (getIntent().getStringExtra("menuName").equals("S Bank")) {

                if (isAdmin() || isSBAdmin()) {
                    startActivity(new Intent(this, SBAdminPanelActivity.class)
                            .putExtra("menuName", getIntent().getStringExtra("menuName")));

                } else {
                    startActivity(new Intent(this, StatementsActivity.class));

                }

            }

        } else {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }
        }
    }

    private boolean validateInput() {
        boolean valid = true;

        if (TextUtils.isEmpty(mUsername.getText().toString().trim())) {
            mUsername.setError("This field is required");
            valid = false;
        } else {
            mUsername.setError(null);
        }
        if (TextUtils.isEmpty(mPassword.getText().toString().trim())) {
            mUsername.setError("This field is required");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();

        updateUI(mCurrentUser);


    }

    private boolean isAdmin() {

        SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isAdmin", false);

    }

    private boolean isSBAdmin() {
        SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isSBAdmin", false);
    }


    private void initAccount(final FirebaseUser firebaseUser) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {

                            SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                            sharedPrefEditor.putBoolean("isAdmin", user.isMainAdmin());
                            sharedPrefEditor.putBoolean("isSBAdmin", user.isSBAdmin());
                            sharedPrefEditor.apply();
                            sharedPrefEditor.commit();
                            updateUI(firebaseUser);


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
