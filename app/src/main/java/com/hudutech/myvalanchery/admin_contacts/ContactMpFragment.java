package com.hudutech.myvalanchery.admin_contacts;


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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.myvalanchery.Config;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.models.contact_models.Mp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactMpFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ContactMpFragment";
    private static final int IMAGE_PICK = 100;
    private TextInputEditText mName;
    private TextInputEditText mConstituency;
    private TextInputEditText mParty;
    private TextInputEditText mMobile;
    private TextInputEditText mPhone;
    private TextInputEditText mEmail;
    private TextInputEditText mAddress;

    private Context mContext;
    private CollectionReference mpRef;
    private ProgressDialog mProgress;
    private StorageReference mStorageRef;
    private ImageView mSelectedPhoto;
    private Uri photoUri;

    private ArrayList<Object> inputs;


    public ContactMpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_mp, container, false);

        mName = v.findViewById(R.id.txt_mp_name);
        mConstituency = v.findViewById(R.id.txt_mp_constituency);
        mParty = v.findViewById(R.id.txt_mp_party);
        mMobile = v.findViewById(R.id.txt_mp_mobile);
        mPhone = v.findViewById(R.id.txt_mp_phone);
        mEmail = v.findViewById(R.id.txt_mp_email);
        mAddress = v.findViewById(R.id.txt_mp_address);
        mSelectedPhoto = v.findViewById(R.id.img_mp_photo);
        v.findViewById(R.id.btn_upload_mp_photo).setOnClickListener(this);
        v.findViewById(R.id.btn_submit_mp).setOnClickListener(this);

        mContext = getContext();
        mpRef = FirebaseFirestore.getInstance().collection("mps");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(getContext());


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

            } else {
                mSelectedPhoto.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        inputs = new ArrayList<>();
        inputs.add(mName);
        inputs.add(mConstituency);
        inputs.add(mParty);
        inputs.add(mMobile);
        inputs.add(mPhone);
        inputs.add(mEmail);
        inputs.add(mAddress);


        if (id == R.id.btn_upload_mp_photo) {
            openImageChooser();
        } else if (id == R.id.btn_submit_mp) {
            if (Config.validateInputs(mContext, inputs)) {
                submitData(
                        photoUri,
                        mName.getText().toString(),
                        mConstituency.getText().toString(),
                        mParty.getText().toString(),
                        mMobile.getText().toString(),
                        mPhone.getText().toString(),
                        mEmail.getText().toString(),
                        mAddress.getText().toString()
                );
            } else {
                Snackbar.make(v, "Fix the errors above!", Snackbar.LENGTH_LONG).show();
            }
        }

    }


    private void submitData(
            Uri uri,
            final String name,
            final String constituency,
            final String party,
            final String mobile,
            final String phone,
            final String email,
            final String address
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
            String fileName = mpRef.document().getId();

            UploadTask uploadTask = mStorageRef.child("images")
                    .child(fileName)
                    .putBytes(thumbnailBytes);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String imageUrl = taskSnapshot.getDownloadUrl().toString();

                    DocumentReference docRef = mpRef.document("mp");

                    Mp mp = new Mp(
                            docRef.getId(),
                            imageUrl,
                            name,
                            constituency,
                            party,
                            mobile,
                            phone,
                            email,
                            address,
                            Config.isAdmin(mContext)
                    );

                    docRef.set(mp)
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
