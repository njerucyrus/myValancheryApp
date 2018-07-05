

package com.hudutech.mymanjeri.admin_banner_setting;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.BarnerAdminAdapter;
import com.hudutech.mymanjeri.models.Banner;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminBarnersActivity extends AppCompatActivity {
    private static final String TAG = "AdminBarnersActivity";

   private static final int IMAGE_PICK = 1;
   private Uri[] imageUris;
   private Uri imageUri;
   private ProgressDialog mProgress;
   private StorageReference mStorageRef;
   private CollectionReference mBarnersRef;
   private RecyclerView mRecylerView;
   private BarnerAdminAdapter mAdapter;
   private List<Banner> barnerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_barners);
        getSupportActionBar().setTitle("Banners");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mBarnersRef = FirebaseFirestore.getInstance().collection("barners");
        barnerList = new ArrayList<>();
        mAdapter = new BarnerAdminAdapter(this, barnerList);
        mRecylerView = findViewById(R.id.admin_barner_recylerview);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setItemAnimator(new DefaultItemAnimator());
        mRecylerView.setAdapter(mAdapter);
        mRecylerView.setHasFixedSize(true);
        loadBarners();

        ImageButton btnUploadImage = (ImageButton)findViewById(R.id.btn_choose_img_admin);

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    private void loadBarners() {
        mBarnersRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                                Banner barner = snapshot.toObject(Banner.class);
                                barnerList.add(barner);
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });
    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), IMAGE_PICK);
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
                barnerList = new ArrayList<>();
                loadBarners();

            } else if (data.getData() != null) {
                //only one image selected so set up the array for upload method to work
                //for both single and multiple images
                imageUris = new Uri[1];
                imageUris[0] = data.getData();
                uploadImages(imageUris);
                barnerList = new ArrayList<>();
                loadBarners();



            }
        } else {
           // No baner images selected selected
            Toast.makeText(this, "No images selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImages(Uri[] uris) {

        mProgress.setMessage("Uploading please...");
        mProgress.setCanceledOnTouchOutside(false);

        mProgress.show();
        final List<String>  imageDownloadUrls = new ArrayList<>();


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

                        DocumentReference docRef = mBarnersRef.document();
                        Banner barner = new Banner(taskSnapshot.getDownloadUrl().toString(), docRef.getId(), 0);

                        docRef.set(barner);

                        if (imageDownloadUrls.size() == imageUris.length) {
                            Toast.makeText(AdminBarnersActivity.this, "banners uploaded", Toast.LENGTH_LONG).show();
                            if (mProgress.isShowing()) mProgress.dismiss();

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



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getFileName(Uri uri) {
        String fileName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.

        try (Cursor cursor = AdminBarnersActivity.this.getContentResolver()
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
