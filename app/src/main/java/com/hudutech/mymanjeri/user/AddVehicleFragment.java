package com.hudutech.mymanjeri.user;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
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
import com.hudutech.mymanjeri.models.contact_models.Vehicle;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVehicleFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddVehicleFragment";
    private static final int IMAGE_PICK = 100;
    private Button mSubmit;
    private Button mChooseImage;
    private TextInputEditText mPlace;
    private TextInputEditText mName;
    private TextInputEditText mPhoneNumber;
    private String mVehicleType;
    private ImageView mSelectedPhoto;
    private Context mContext;
    private ProgressDialog mProgress;
    private StorageReference mStorageRef;
    private CollectionReference mVehiclesRef;
    private DocumentReference docRef;
    private Uri photoUri;

    public AddVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        mContext = getContext();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mVehiclesRef = FirebaseFirestore.getInstance().collection("contact_vehicles");

        mProgress = new ProgressDialog(getContext());

        mChooseImage = view.findViewById(R.id.btn_upload_vehicle_photo);
        mPlace = view.findViewById(R.id.txt_vehicle_place);
        mName = view.findViewById(R.id.txt_vehicle_name);
        mPhoneNumber = view.findViewById(R.id.txt_vehicle_phone_number);
        mSelectedPhoto = view.findViewById(R.id.img_vehicle_photo);
        mSubmit = view.findViewById(R.id.btn_submit_vehicle);

        Spinner vehicle = view.findViewById(R.id.spinner_vehicle_type);
        vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mVehicleType = position > 0 ? adapterView.getItemAtPosition(position).toString(): null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_upload_vehicle_photo) {
            openImageChooser();
        } else if (id == R.id.btn_submit_vehicle) {
            if (validateInputs()) {
                submitData(
                        photoUri,
                        mPlace.getText().toString().trim(),
                        mName.getText().toString().trim(),
                        mPhoneNumber.getText().toString().trim(),
                        mVehicleType
                );
            } else {
                Snackbar.make(v, "Fix the errors above",Snackbar.LENGTH_LONG).show();
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

            } else {
                mSelectedPhoto.setVisibility(View.GONE);
            }
        }

    }


    private void submitData(
            Uri photoUri,
            final String place,
            final String name,
            final String phoneNumber,
            final String vehicleType
    ) {

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
                    .child(mVehiclesRef.document().getId())
                    .putBytes(thumbnailBytes);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String imageUrl = taskSnapshot.getDownloadUrl().toString();
                    DocumentReference docRef = mVehiclesRef.document();
                    Vehicle vehicle = new Vehicle(
                            docRef.getId(),
                            name,
                            place,
                            phoneNumber,
                            vehicleType,
                            imageUrl,
                            Config.isAdmin(mContext)
                    );


                    docRef.set(vehicle)
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
        startActivityForResult(Intent.createChooser(intent, "Select Images"), IMAGE_PICK);
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

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mName.getText().toString().trim())){
            valid = false;
            mName.setError("*Required!");
        }else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mPlace.getText().toString().trim())){
            valid = false;
            mPlace.setError("*Required!");
        }else {
            mPlace.setError(null);
        }

        if (TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())){
            valid = false;
            mPhoneNumber.setError("*Required!");
        }else {
           mPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(mVehicleType)){
            valid = false;
            Toast.makeText(mContext, "Select vehicle type", Toast.LENGTH_SHORT).show();

        }

        return valid;
    }



}
