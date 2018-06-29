package com.hudutech.mymanjeri;

import android.annotation.TargetApi;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.mymanjeri.majery_activities.BloodBankActivity;
import com.hudutech.mymanjeri.models.majery_models.BloodDonor;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class AddBloodDonorAdminActivity extends AppCompatActivity {
    private static final String TAG = "AddBloodDonorAdminActiv";
    private static final int IMAGE_PICK = 1;
    private Spinner mSpinner;
    private TextInputEditText mFullName;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mAddress;
    private TextInputEditText mOtherInfo;
    private String bloodGroup;
    private DocumentReference bloodDonorsRef;
    private ProgressDialog mProgress;
    private ImageButton mButtonUploadImg;

    private Uri[] imageUris;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private String imageDownloadUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blood_donor_admin);
        getSupportActionBar().setTitle("Admin Add Blood Bank");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSpinner = findViewById(R.id.spinner_blood_group_admin);
        mFullName = findViewById(R.id.txt_fullname_admin);
        mPhoneNumber = findViewById(R.id.txt_phone_number_admin);
        mAddress = findViewById(R.id.txt_address);
        mOtherInfo = findViewById(R.id.txt_other_details);
        mButtonUploadImg = findViewById(R.id.btn_upload_avator);

        mProgress = new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mButtonUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show chooser
                openImageChooser();
            }
        });


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

        Button mDone = findViewById(R.id.btn_done_admin);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {

                    submitDonorData(
                            mFullName.getText().toString(),
                            mPhoneNumber.getText().toString().trim(),
                            bloodGroup,
                            mOtherInfo.getText().toString(),
                            mAddress.getText().toString(),
                            imageDownloadUrl,
                            true
                    );
                } else {
                    Snackbar.make(v, "Fix errors above", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void submitDonorData(String fullName, String phoneNumber, String bloodGroup, String otherInfo, String address, String avator, boolean isValidated) {
        BloodDonor donor = new BloodDonor(
                fullName,
                phoneNumber,
                bloodGroup,
                otherInfo,
                address,
                avator,
                isValidated
        );

        mProgress.setMessage("Working please wait...");
        mProgress.show();

        bloodDonorsRef
                .set(donor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddBloodDonorAdminActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), BloodBankActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddBloodDonorAdminActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
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


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                imageUris = new Uri[count];

                for (int i = 0; i < count; i++) {
                    imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris[i] = imageUri;
                }
                uploadImages(imageUris);

            } else if (data.getData() != null) {
                //only one image selected so set up the array for upload method to work
                //for both single and multiple images
                imageUris = new Uri[1];
                imageUris[0] = data.getData();
                uploadImages(imageUris);


            }
        } else {
            // No baner images selected selected
            Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImages(Uri[] uris) {

        mProgress.setMessage("Uploading please...");
        mProgress.setCanceledOnTouchOutside(false);

        mProgress.show();


        for (Uri uri : uris) {

            try {
                Bitmap bitmapImage = getBitmapFromUri(uri);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                final byte[] thumbnailBytes = baos.toByteArray();

                //get the filename. ensure to overwrite the existing file
                //with the same name this saves on firebase storage. by removing duplicates
                String fileName = getFileName(uri);

                UploadTask uploadTask = mStorageRef.child("images")
                        .child(fileName)
                        .putBytes(thumbnailBytes);


                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if (mProgress.isShowing()) mProgress.dismiss();
                        imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(AddBloodDonorAdminActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");


                        if (mProgress.isShowing()) mProgress.dismiss();

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
            }


        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getFileName(Uri uri) {
        String fileName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.

        try (Cursor cursor = AddBloodDonorAdminActivity.this.getContentResolver()
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
                getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
