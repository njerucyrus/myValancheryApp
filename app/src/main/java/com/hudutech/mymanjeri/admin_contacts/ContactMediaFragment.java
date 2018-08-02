package com.hudutech.mymanjeri.admin_contacts;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.contact_models.Media;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactMediaFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ContactMediaFragment";
    private static final int IMAGE_PICK = 100;
    private TextInputEditText mName;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mMediaName;
    private String mMediaType;
    private ImageView mSelectedPhoto;
    private CollectionReference mMediaRef;
    private ProgressDialog mProgress;
    private Context mContext;
    private StorageReference mStorageRef;
    private Uri photoUri;
    private String photoUploaded = null;
    private ArrayList<Object> inputs;
    private Spinner spinner;

    public ContactMediaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_media, container, false);
        mName = v.findViewById(R.id.txt_media_name);
        mPhoneNumber = v.findViewById(R.id.txt_media_phone);
        mMediaName = v.findViewById(R.id.txt_name_of_media);
        mSelectedPhoto = v.findViewById(R.id.img_media_photo);

        spinner = v.findViewById(R.id.spinner_media_type);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                mMediaType = position > 0 ? adapter.getItemAtPosition(position).toString() : null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMediaType = null;
            }
        });

        v.findViewById(R.id.btn_upload_media_photo).setOnClickListener(this);

        v.findViewById(R.id.btn_submit_media).setOnClickListener(this);


        //end of validated inputs


        mContext = getContext();
        mProgress = new ProgressDialog(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mMediaRef = FirebaseFirestore.getInstance().collection("media");

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

    @Override
    public void onClick(View v) {
        //validated inputs
        inputs = new ArrayList<>();
        inputs.add(mName);
        inputs.add(mPhoneNumber);
        inputs.add(mMediaName);
        inputs.add(mSelectedPhoto);
        inputs.add(spinner);
        HashMap<String, String> stringInputs = new HashMap<>();
        stringInputs.put("Media Type", mMediaType);
        stringInputs.put("Media Logo", photoUploaded);
        inputs.add(stringInputs);


        final int id = v.getId();

        if (id == R.id.btn_submit_media) {
            if (Config.validateInputs(mContext, inputs)) {
                submitData(
                        photoUri,
                        mName.getText().toString(),
                        mPhoneNumber.getText().toString(),
                        mMediaName.getText().toString(),
                        mMediaType
                );
            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.btn_upload_media_photo) {
            openImageChooser();
        }

    }

    private void submitData(
            Uri uri,
            final String name,
            final String phoneNumber,
            final String mediaName,
            final String mediaTye

    ) {

        mProgress.setMessage("Submitting data please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        try {
            Bitmap bitmapImage = Config.getBitmapFromUri(mContext, uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            final byte[] thumbnailBytes = baos.toByteArray();

            //get the filename. ensure to overwrite the existing file
            //with the same name this saves on firebase storage. by removing duplicates
            String fileName = mMediaRef.document().getId();

            UploadTask uploadTask = mStorageRef.child("images")
                    .child(fileName)
                    .putBytes(thumbnailBytes);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String imageUrl = taskSnapshot.getDownloadUrl().toString();

                    DocumentReference docRef = mMediaRef.document();

                    Media media = new Media(
                            imageUrl,
                            docRef.getId(),
                            name,
                            phoneNumber,
                            mediaName,
                            mediaTye,
                            Config.isAdmin(mContext)
                    );

                    docRef.set(media)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Config.clearInputs(inputs);
                                    mSelectedPhoto.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(mContext, "Error occurred! please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if (mProgress.isShowing()) mProgress.dismiss();
                    Toast.makeText(mContext, "Error occurred.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: " + e.getMessage());

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


}
