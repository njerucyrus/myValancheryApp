package com.hudutech.myvalanchery.digital_activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.models.User;
import com.hudutech.myvalanchery.models.digital_models.SBankAccount;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateAccountActivity";
    private static final int IMAGE_PICK = 1;

    private TextInputEditText mAccountNo;
    private TextInputEditText mBatchNo;
    private TextInputEditText mCustomerName;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextInputEditText mConfirmPassword;
    private ImageView mSelectedPhoto;


    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    private CollectionReference usersRef;

    private ProgressDialog mProgress;
    private Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseFirestore.getInstance().collection("users");


        Button mButtonCreateAcc = findViewById(R.id.btn_create_sb_account);
        Button mButtonSelectPhoto = findViewById(R.id.btn_select_sb_photo);

        mSelectedPhoto = findViewById(R.id.img_selected_sb_photo);
        mAccountNo = findViewById(R.id.txt_sb_account_no);
        mBatchNo = findViewById(R.id.txt_sb_batch_no);
        mCustomerName = findViewById(R.id.txt_sb_customer_name);
        mEmail = findViewById(R.id.txt_sb_email);
        mPassword = findViewById(R.id.txt_sb_password);
        mConfirmPassword = findViewById(R.id.txt_sb_confirm_password);

        mButtonCreateAcc.setOnClickListener(this);
        mButtonSelectPhoto.setOnClickListener(this);

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

        watchInputs();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {

            if (data.getData() != null) {
                photoUri = data.getData();
                mSelectedPhoto.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.no_barner);

                Glide.with(this)
                        .load(photoUri)
                        .apply(requestOptions)
                        .into(mSelectedPhoto);

            } else {
                mSelectedPhoto.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_select_sb_photo) {
            openImageChooser();

        } else if (id == R.id.btn_create_sb_account) {
            if (validateInputs()) {
                createSBAccount(photoUri, mBatchNo.getText().toString(), mCustomerName.getText().toString(), mAccountNo.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
            } else {
                Snackbar.make(v, "Fix the errors above!.", Snackbar.LENGTH_LONG).show();
            }
        }

    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getFileName(Uri uri) {
        String fileName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.

        try (Cursor cursor = CreateAccountActivity.this.getContentResolver()
                .query(uri, null, null, null, null, null)) {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                fileName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + fileName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
            }
        }

        return fileName;
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                CreateAccountActivity.this.getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    private void createSBAccount(final Uri photoUri, final String batchNo, final String customerName, final String accountNo, final String email, String password) {

        mProgress.setTitle("Creating account.");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mAuth2.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference docRef = usersRef.document(mAuth2.getCurrentUser().getUid());

                            // create user account profile

                            User user = new User(
                                    email,
                                    "",
                                    "",
                                    false,
                                    false
                            );

                            docRef.set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            //[CREATE SBACCOUNT HERE]

                                            try {
                                                Bitmap bitmapImage = getBitmapFromUri(photoUri);

                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                                                final byte[] thumbnailBytes = baos.toByteArray();

                                                //get the filename. ensure to overwrite the existing file
                                                //with the same name this saves on firebase storage. by removing duplicates
                                                String fileName = getFileName(photoUri);

                                                UploadTask uploadTask = mStorageRef.child("images")
                                                        .child(fileName)
                                                        .putBytes(thumbnailBytes);


                                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                        String photoUrl = taskSnapshot.getDownloadUrl().toString();

                                                        DocumentReference docRef = FirebaseFirestore.getInstance()
                                                                .collection("sb_accounts")
                                                                .document();
                                                        String docKey = docRef.getId();

                                                        SBankAccount account = new SBankAccount(
                                                                docKey,
                                                                mAuth2.getCurrentUser().getUid(),
                                                                photoUrl,
                                                                batchNo,
                                                                customerName,
                                                                accountNo,
                                                                email,
                                                                0f

                                                        );

                                                        docRef.set(account)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        if (mProgress.isShowing())
                                                                            mProgress.dismiss();
                                                                        Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                                        mAuth2.signOut();
                                                                        startActivity(new Intent(CreateAccountActivity.this, SBAdminPanelActivity.class));
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(CreateAccountActivity.this, "Error occurred while creating account.Please try again later", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        if (mProgress.isShowing())
                                                            mProgress.dismiss();

                                                    }

                                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                        long percent = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                                                        // if (percent == 100) mProgress.dismiss();
                                                    }
                                                });


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                mAuth2.signOut();
                                            }


                                        }
                                    })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                        }
                                    });
                        } else if (!task.isSuccessful()) {
                            try {
                                if (mProgress.isShowing()) mProgress.dismiss();
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                if (mProgress.isShowing()) mProgress.dismiss();

                                mPassword.setError("Password too Weak.");
                                mPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                if (mProgress.isShowing()) mProgress.dismiss();

                                mEmail.setError("Account with this email already exists");
                                mEmail.requestFocus();

                            } catch (FirebaseTooManyRequestsException e) {
                                if (mProgress.isShowing()) mProgress.dismiss();

                                Snackbar.make(mEmail, "App Encountered internal error try again later",
                                        Snackbar.LENGTH_LONG).show();
                            } catch (Exception e) {
                                if (mProgress.isShowing()) mProgress.dismiss();

                                Snackbar.make(mEmail, "Unable to create account please try again",
                                        Snackbar.LENGTH_LONG).show();
                                Log.e("REG_AUTH_ERROR", e.getMessage());
                            }
                        }
                    }
                });

        //[END HERE']

    }

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mBatchNo.getText().toString().trim())) {
            mBatchNo.setError("*Required");
            valid = false;
        } else {
            mBatchNo.setError(null);
        }

        if (TextUtils.isEmpty(mCustomerName.getText().toString().trim())) {
            mCustomerName.setError("*Required");
            valid = false;
        } else {
            mCustomerName.setError(null);
        }

        if (TextUtils.isEmpty(mAccountNo.getText().toString().trim())) {
            mAccountNo.setError("*Required");
            valid = false;
        } else {
            mAccountNo.setError(null);
        }

        if (TextUtils.isEmpty(mEmail.getText().toString().trim())) {
            mEmail.setError("*Required");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (TextUtils.isEmpty(mPassword.getText().toString().trim())) {
            mPassword.setError("*Required");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (TextUtils.isEmpty(mConfirmPassword.getText().toString().trim())) {
            mConfirmPassword.setError("*Required");
            valid = false;
        } else {
            mConfirmPassword.setError(null);
        }


        if (!TextUtils.equals(mPassword.getText().toString().trim(), mConfirmPassword.getText().toString().trim())) {
            mConfirmPassword.setError("Password Do not match!");
            mConfirmPassword.requestFocus();
            valid = false;
        } else {
            mConfirmPassword.setError(null);
        }

        return valid;
    }

    private void watchInputs() {
        mBatchNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mBatchNo.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBatchNo.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBatchNo.setError(null);
            }
        });

        mCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCustomerName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCustomerName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCustomerName.setError(null);
            }
        });


        mAccountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mAccountNo.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAccountNo.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAccountNo.setError(null);
            }
        });


        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mEmail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEmail.setError(null);
            }
        });


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


        mConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mConfirmPassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConfirmPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mConfirmPassword.setError(null);
            }
        });


    }

}
