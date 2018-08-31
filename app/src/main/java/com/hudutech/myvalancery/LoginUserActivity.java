package com.hudutech.myvalancery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.hudutech.myvalancery.user.UserPanelActivity;

public class LoginUserActivity extends AppCompatActivity {
    private static final String TAG = "LoginUserActivity";
    private FirebaseAuth mAuth;
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mUsername = findViewById(R.id.txt_username);
        mPassword = findViewById(R.id.txt_password);

        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
                finish();
            }
        });

        Button mLoginButton = findViewById(R.id.btn_login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    if (validateInput()) {
                        signIn();
                    } else {
                        Snackbar.make(view, "Please correct the errors above to continue",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginUserActivity.this, "No internet connection please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView tvRegister = findViewById(R.id.tv_create_account);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUserActivity.this, JoinActivity.class));

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

                            SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                            sharedPrefEditor.putBoolean("isAdmin", false);
                            sharedPrefEditor.putBoolean("isSBAdmin", false);
                            sharedPrefEditor.apply();
                            sharedPrefEditor.commit();

                            startActivity(new Intent(LoginUserActivity.this, UserPanelActivity.class));

                        } else if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Snackbar.make(mPassword, "Invalid Email/Password", Snackbar.LENGTH_LONG).show();
                            } catch (FirebaseTooManyRequestsException e) {
                                Snackbar.make(mPassword, "Error! Unable to complete authentication please try again later", Snackbar.LENGTH_LONG).show();
                            } catch (Exception e) {
                                if (mProgress.isShowing()) mProgress.dismiss();
                                Snackbar.make(mPassword, "Unable to authenticate your account please try again later", Snackbar.LENGTH_LONG).show();

                                Log.e(TAG, e.getMessage());
                            }

                        }
                    }
                });

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


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
