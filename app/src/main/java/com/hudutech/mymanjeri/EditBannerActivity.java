package com.hudutech.mymanjeri;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.annotation.RequiresApi;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.mymanjeri.models.Banner;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditBannerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EditBannerActivity";
    private Banner barner;
    private ProgressDialog mProgress;
    private CollectionReference mRootRef;


    private static final int IMAGE_PICK = 1;
    private Uri[] imageUris;
    private Uri imageUri;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_banner);

        getSupportActionBar().setTitle("Edit Banner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView bannerImage = findViewById(R.id.img_edit_banner);

        Button delete = findViewById(R.id.btn_edit_delete_banner);
        Button change = findViewById(R.id.btn_edit_change_banner);

        delete.setOnClickListener(this);
        change.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);
        barner = (Banner)getIntent().getSerializableExtra("barner");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(this)
                .load(barner.getBarnerUrl())
                .apply(requestOptions)
                .into(bannerImage);

        mRootRef = FirebaseFirestore.getInstance().collection("barners");
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_edit_change_banner){
            //change image
            openImageChooser();
        } else if (id == R.id.btn_edit_delete_banner) {
            //delete barner from storage and from db

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to delete?");
            builder.setMessage("The file will be lost permanently" );
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteBarner(barner.getBarnerUrl());
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

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

        mProgress.setMessage("Uploading please wait...");
        mProgress.setCanceledOnTouchOutside(false);

        mProgress.show();
        final List<String> imageDownloadUrls = new ArrayList<>();


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

                        imageDownloadUrls.add(taskSnapshot.getDownloadUrl().toString());

                        DocumentReference docRef = mRootRef.document(barner.getDocKey());
                        Banner barner1 = new Banner(taskSnapshot.getDownloadUrl().toString(), barner.getDocKey(), 0);

                        docRef.set(barner1);

                        if (imageDownloadUrls.size() == imageUris.length) {
                            Toast.makeText(EditBannerActivity.this, "banners uploaded", Toast.LENGTH_LONG).show();
                            if (mProgress.isShowing()) mProgress.dismiss();

                            startActivity(new Intent(EditBannerActivity.this, AdminBarnersActivity.class));

                        }


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



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getFileName(Uri uri) {
        String fileName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.

        try (Cursor cursor = EditBannerActivity.this.getContentResolver()
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


    private void deleteBarner(String downloadUrl) {

        mProgress.setMessage("Deleting....");
        mProgress.show();
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                deleteFromDb();
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mProgress.isShowing()) mProgress.dismiss();
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });

    }

    private void deleteFromDb() {
        DocumentReference docRef = mRootRef.document(barner.getDocKey());
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(EditBannerActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditBannerActivity.this, AdminBarnersActivity.class));
                        finish();
                        barner = new Banner();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();

                    }
                });
    }
}
