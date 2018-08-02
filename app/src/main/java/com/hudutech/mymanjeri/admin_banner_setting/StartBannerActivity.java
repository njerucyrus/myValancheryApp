package com.hudutech.mymanjeri.admin_banner_setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.Banner;

import java.io.ByteArrayOutputStream;

public class StartBannerActivity extends AppCompatActivity {
    private static final String TAG = "StartBannerActivity";
    private static final int IMAGE_PICK = 100;
    private ImageView mSelectedPhoto;
    private ProgressDialog mProgress;
    private StorageReference mStorageRef;
    private DocumentReference startBannerRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_banner);
        getSupportActionBar().setTitle("Start Banner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnUpload = findViewById(R.id.btn_upload_photo);
        mSelectedPhoto = findViewById(R.id.imageView);

        mProgress = new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        startBannerRef = FirebaseFirestore.getInstance().collection("start_banners").document("banner");
        loadPhoto();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {

            if (data.getData() != null) {
                Uri photoUri = data.getData();
                mSelectedPhoto.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.no_barner);

                Glide.with(this)
                        .load(photoUri)
                        .apply(requestOptions)
                        .into(mSelectedPhoto);

                uploadPhoto(photoUri);


            } else {
                mSelectedPhoto.setVisibility(View.GONE);
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK);
    }


    private void uploadPhoto(Uri uri) {

        mProgress.setMessage("Uploading please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        try {
            Bitmap bitmapImage = Config.getBitmapFromUri(StartBannerActivity.this, uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            final byte[] thumbnailBytes = baos.toByteArray();

            //get the filename. ensure to overwrite the existing file
            //with the same name this saves on firebase storage. by removing duplicates
            String fileName = startBannerRef.getId();

            UploadTask uploadTask = mStorageRef.child("images")
                    .child(fileName)
                    .putBytes(thumbnailBytes);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Banner banner = new Banner(
                            taskSnapshot.getDownloadUrl().toString(),
                            startBannerRef.getId(),
                            0,
                            true
                    );
                    startBannerRef.set(banner)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(StartBannerActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(StartBannerActivity.this, "Error occurred pleas try again later", Toast.LENGTH_SHORT).show();

                                }
                            });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if (mProgress.isShowing()) mProgress.dismiss();
                    Toast.makeText(StartBannerActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
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
            if (mProgress.isShowing()) mProgress.dismiss();

        }

    }

    private void loadPhoto() {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("start_banners").document("banner");
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Banner banner = documentSnapshot.toObject(Banner.class);
                            mSelectedPhoto.setVisibility(View.VISIBLE);
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.no_barner);

                            Glide.with(StartBannerActivity.this)
                                    .load(banner.getBannerUrl())
                                    .apply(requestOptions)
                                    .into(mSelectedPhoto);
                        }
                    }
                });
    }

}