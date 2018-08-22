package com.hudutech.myvalanchery.digital_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.models.digital_models.SBankAccount;

public class EnterPasswordActivity extends AppCompatActivity {
    TextInputEditText mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    private FirebaseFirestore db;
    private CollectionReference mUsersRef;
    private CollectionReference mAccountsRef;
    private SBankAccount account;
    private ProgressDialog mProgress;
    private Button mButtonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        getSupportActionBar().setTitle("Verify Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUsersRef = db.collection("users");
        mAccountsRef = db.collection("sb_accounts");
        mProgress = new ProgressDialog(this);
        account = (SBankAccount) getIntent().getSerializableExtra("account");
        mPassword = findViewById(R.id.txt_enter_password_to_delete);
        mButtonDelete = findViewById(R.id.btn_confirm_delete_account);


        //used to get the instace of firebase auth that we can sign out after creating account
        //this ensures admin is not logged out.
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://mymanjeri-c6d88.firebaseio.com")
                .setApiKey("AIzaSyA6Bmd4GH-F5lr_Z3OTlfms5kgZ8PhzZ8M")
                .setApplicationId("mymanjeri-c6d88").build();

        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }
        //Watch password field to clear error on typing.
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword.setError(null);
            }
        });


        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPassword.getText().toString().trim())) {
                    String desc = "Account Holder " + account.getCustomerName() + " Account No " + account.getAccountNo() + " Will be deleted permanently";
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterPasswordActivity.this);
                    builder.setTitle("Are you sure you want delete this account?");
                    builder.setMessage(desc);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAccount(mPassword.getText().toString());
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
                    mPassword.setError("*Required");
                    mPassword.requestFocus();
                    Snackbar.make(v, "Password Cannot be empty", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void deleteAccount(String password) {
        mProgress.setMessage("Deleting account please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        AuthCredential credential = EmailAuthProvider.getCredential(account.getEmail(), password);
        mAuth2.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        user.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        //Delete the record on users and sbaccount refs
                                        mAccountsRef.document(account.getDocKey()).delete();
                                        mUsersRef.document(account.getUserUid()).delete();
                                        if (mProgress.isShowing()) mProgress.dismiss();
                                        startActivity(new Intent(EnterPasswordActivity.this, ManageSBAccountsActivity.class));
                                        finish();
                                        Toast.makeText(EnterPasswordActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (mProgress.isShowing()) mProgress.dismiss();
                                        Toast.makeText(EnterPasswordActivity.this, "Error Occurred! Failed to delete account", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(EnterPasswordActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                            Snackbar.make(mButtonDelete, "Invalid Password!", Snackbar.LENGTH_LONG).show();
                            mPassword.setError("Invalid Password");

                        } else {
                            Toast.makeText(EnterPasswordActivity.this, "Unable to validate this account. please try again later", Toast.LENGTH_SHORT).show();
                            Snackbar.make(mButtonDelete, "Unable to validate this account. please try again later", Snackbar.LENGTH_LONG).show();

                        }

                    }
                });
    }
}


