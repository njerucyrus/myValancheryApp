package com.hudutech.mymanjeri.user;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.digital_activities.FindPartnerActivity;
import com.hudutech.mymanjeri.models.digital_models.Partner;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindPartnerFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "FindPartnerFragment";
    private static final int IMAGE_PICK = 1;
    private Context mContext;
    private static final String MALE = "Male";
    private static final String FEMALE = "Female";
    private Button mUploadPhoto;
    private RadioGroup mRadioGroupGender;
    private TextInputEditText mName;
    private TextInputEditText mPlace;
    private TextInputEditText mReligion;
    private TextInputEditText mAge;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mMoreDetails;
    private Button mSubmit;
    private ImageView mSelectedPhoto;
    private Uri photoUri;
    private ProgressDialog mProgress;

    private StorageReference mStorageRef;
    private CollectionReference mPartnersRef;
    private FirebaseUser mCurrentUser;
    private String gender;

    public FindPartnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_find_partner, container, false);
        mContext = getContext();
        mUploadPhoto = v.findViewById(R.id.btn_upload_findpartner_photo);
        mRadioGroupGender = v.findViewById(R.id.radio_group_findpartner_gender);
        mName = v.findViewById(R.id.txt_findpartner_name);
        mPlace = v.findViewById(R.id.txt_findpartner_place);
        mAge = v.findViewById(R.id.txt_findpartner_age);
        mReligion = v.findViewById(R.id.txt_findpartner_religion);
        mPhoneNumber = v.findViewById(R.id.txt_findpartner_phonenumber);
        mMoreDetails = v.findViewById(R.id.txt_findpartner_moredetails);
        mSelectedPhoto = v.findViewById(R.id.img_findpartner_selected_image);
        mSubmit = v.findViewById(R.id.btn_findpartner_submit);

        mProgress = new ProgressDialog(getContext());
        mSubmit.setOnClickListener(this);
        mUploadPhoto.setOnClickListener(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPartnersRef = FirebaseFirestore.getInstance().collection("partners");

        mRadioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_male) {
                    gender = MALE;
                } else if (checkedId == R.id.radio_female) {
                    gender = FEMALE;
                }
            }
        });
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {

            if (data.getData() != null) {
                photoUri = data.getData();
                mSelectedPhoto.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.user_96);

                Glide.with(this)
                        .load(photoUri)
                        .apply(requestOptions)
                        .into(mSelectedPhoto);

            } else {
                mSelectedPhoto.setVisibility(View.GONE);
            }
        }

    }

    private void submitData(Uri photoUri, final String mGender,
                            final String name, final String place,
                            final String religion, final int age,
                            final String phoneNumber,
                            final String moreDetails) {

        mProgress.setMessage("Submitting data please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


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

                    String imageUrl = taskSnapshot.getDownloadUrl().toString();
                    DocumentReference docRef = mPartnersRef.document();
                    Partner partner = new Partner(
                            imageUrl,
                            mGender,
                            name,
                            place,
                            religion,
                            age,
                            phoneNumber,
                            moreDetails,
                            mCurrentUser.getUid(),
                            docRef.getId(),
                            false
                    );

                    docRef.set(partner)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(mContext, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Log.e(TAG, "onFailure: "+e.getMessage());
                                    Toast.makeText(mContext, "Error occurred! please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

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

        try (Cursor cursor = mContext.getContentResolver()
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
                mContext.getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_upload_findpartner_photo) {
            openImageChooser();
        } else if (id == R.id.btn_findpartner_submit) {
            if (validateInputs()) {
                submitData(
                        photoUri,
                        gender,
                        mName.getText().toString().trim(),
                        mPlace.getText().toString().trim(),
                        mReligion.getText().toString().trim(),
                        Integer.parseInt(mAge.getText().toString()),
                        mPhoneNumber.getText().toString(),
                        mMoreDetails.getText().toString()
                );
            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            valid = false;
            mName.setError("*Required");
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mPlace.getText().toString().trim())) {
            valid = false;
            mPlace.setError("*Required");
        } else {
            mPlace.setError(null);
        }

        if (TextUtils.isEmpty(mReligion.getText().toString().trim())) {
            valid = false;
            mReligion.setError("*Required");
        } else {
            mReligion.setError(null);
        }

        if (TextUtils.isEmpty(mAge.getText().toString().trim())) {
            valid = false;
            mAge.setError("*Required");
        } else {
            mAge.setError(null);
        }

        if (TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())) {
            valid = false;
            mPhoneNumber.setError("*Required");
        } else {
            mPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(mMoreDetails.getText().toString().trim())) {
            valid = false;
            mMoreDetails.setError("*Required");
        } else {
            mMoreDetails.setError(null);
        }

        if (TextUtils.isEmpty(gender)) {
            valid = false;
            Toast.makeText(mContext, "Select Gender", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

}
