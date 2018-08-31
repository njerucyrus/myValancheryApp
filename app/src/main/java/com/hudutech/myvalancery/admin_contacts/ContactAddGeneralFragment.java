package com.hudutech.myvalancery.admin_contacts;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.contact_models.General;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactAddGeneralFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ContactAddGeneralFragme";
    private static final int IMAGE_PICK = 100;
    private TextInputEditText mName;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mPlace;
    private CollectionReference mGeneralRef;
    private Context mContext;
    private ProgressDialog mProgress;

    private Uri photoUri;
    private String photoUploaded = null;
    private StorageReference mStorageRef;
    private ImageView mSelectedPhoto;

    public ContactAddGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_general, container, false);

        mContext = getContext();
        mProgress = new ProgressDialog(getContext());

        mName = v.findViewById(R.id.txt_general_name);
        mPhoneNumber = v.findViewById(R.id.txt_general_phone_number);
        mPlace = v.findViewById(R.id.txt_general_place);
        mGeneralRef = FirebaseFirestore.getInstance().collection("general");

        mSelectedPhoto = v.findViewById(R.id.img_art_photo);
        mStorageRef = FirebaseStorage.getInstance().getReference();



        v.findViewById(R.id.btn_upload_art_photo).setOnClickListener(this);


        v.findViewById(R.id.btn_submit_general).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_submit_general) {
            if (validateInputs()) {
                submitData(photoUri, mName.getText().toString(), mPhoneNumber.getText().toString(), mPlace.getText().toString());
            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
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

                Glide.with(mContext)
                        .load(photoUri)
                        .apply(requestOptions)
                        .into(mSelectedPhoto);

                photoUploaded = "uploaded";

            } else {
                mSelectedPhoto.setVisibility(View.GONE);
            }
        }
    }




    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            valid = false;
            mName.setError("*Required!");
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mPlace.getText().toString().trim())) {
            valid = false;
            mPlace.setError("*Required!");
        } else {
            mPlace.setError(null);
        }

        if (TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())) {
            valid = false;
            mPhoneNumber.setError("*Required!");
        } else {
            mPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(photoUploaded)) {
            valid = false;
            Toast.makeText(mContext, "Please Select a photo first to continue", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private void submitData(Uri uri, final String name, final String phoneNumber, final String placeName) {


        try {
            Bitmap bitmapImage = Config.getBitmapFromUri(mContext, uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            final byte[] thumbnailBytes = baos.toByteArray();

            //get the filename. ensure to overwrite the existing file
            //with the same name this saves on firebase storage. by removing duplicates
            String fileName = mGeneralRef.document().getId();

            UploadTask uploadTask = mStorageRef.child("images")
                    .child(fileName)
                    .putBytes(thumbnailBytes);

            mProgress.setMessage("Submitting please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DocumentReference docRef = mGeneralRef.document();
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    General general = new General(
                            docRef.getId(),
                            name,
                            placeName,
                            phoneNumber,
                            Config.isAdmin(mContext),
                            downloadUrl
                    );


                    docRef.set(general)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(mContext, "Data Submitted Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(mContext, "Error occurred while submitting data!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Toast.makeText(mContext, "Failed to upload photo Reason => " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            if (mProgress.isShowing()) mProgress.dismiss();
            Toast.makeText(mContext, "Error occurred Reason => " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




}
