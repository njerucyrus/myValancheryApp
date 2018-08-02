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
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import com.hudutech.mymanjeri.adapters.SelectedImagesViewPagerAdapter;
import com.hudutech.mymanjeri.models.Hotel;
import com.hudutech.mymanjeri.models.SelectedImage;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddHotelFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddHotelFragment";
    private static final int IMAGE_PICK = 100;
    private static final int PLACE_PICKER_REQUEST = 200;

    private Context mContext;
    private Uri[] imageUris = new Uri[0];
    private ViewPager mImageViewPager;
    private TextView tvSelectedImgs;
    private ProgressDialog mProgress;
    private List<String> imageDownloadUrls;
    private StorageReference mStorageRef;
    private CollectionReference mHotelsRef;
    private TextView mSelectedPinPoint;

    private TextInputEditText mHotelName;
    private TextInputEditText mPlaceName;
    private RatingBar mRatingBar;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mMoreDetails;
    private float rating = 0f;
    private double lat = 0d;
    private double lng = 0d;
    private String address;

    public AddHotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_hotel, container, false);
        mImageViewPager = v.findViewById(R.id.selected_hotel_viewpager_images);
        tvSelectedImgs = v.findViewById(R.id.tv_hotel_selected_imgs);

        mHotelName = v.findViewById(R.id.txt_hotel_name);
        mPlaceName = v.findViewById(R.id.txt_hotel_place_name);
        mRatingBar = v.findViewById(R.id.hotel_rating_bar);
        mPhoneNumber = v.findViewById(R.id.txt_hotel_phone_number);
        mMoreDetails = v.findViewById(R.id.txt_hotel_more_details);
        mSelectedPinPoint = v.findViewById(R.id.selected_hotel_map_location);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float mRating, boolean fromUser) {
                rating = mRating;
            }
        });

        v.findViewById(R.id.btn_choose_hotel_images).setOnClickListener(this);
        v.findViewById(R.id.btn_submit_hotel).setOnClickListener(this);
        v.findViewById(R.id.btn_select_hotel_location).setOnClickListener(this);

        mContext = getContext();
        mProgress = new ProgressDialog(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mHotelsRef = FirebaseFirestore.getInstance().collection("hotels");

        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_choose_hotel_images) {
            openImageChooser();
        } else if (id == R.id.btn_select_hotel_location) {
            showPlacePicker();
        } else if (id == R.id.btn_submit_hotel) {
            if (validateInputs()) {
                submitData(imageUris, mHotelName.getText().toString(), mPlaceName.getText().toString(), lat, lng, address, rating, mPhoneNumber.getText().toString(), mMoreDetails.getText().toString());
            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {


            if (data.getClipData() != null) {
                if (data.getClipData().getItemCount() > 5) {
                    // do needful here
                    Toast.makeText(mContext, "Only Max of 5 images allowed", Toast.LENGTH_LONG).show();
                    return;
                }

                int count = data.getClipData().getItemCount();
                imageUris = new Uri[count];
                for (int i = 0; i < count; i++) {
                    imageUris[i] = data.getClipData().getItemAt(i).getUri();
                }
            } else {
                imageUris = new Uri[1];
                imageUris[0] = data.getData();
            }
            showSelectedImages(imageUris);
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            //dismiss the progress dialog
            if (mProgress.isShowing()) mProgress.dismiss();

            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, mContext);
                mSelectedPinPoint.setVisibility(View.VISIBLE);
                mSelectedPinPoint.setText("Selected Location : " + place.getName());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
                address = place.getName().toString();

            }
        }

    }

    private void showSelectedImages(Uri[] imageUris) {
        tvSelectedImgs.setVisibility(View.VISIBLE);
        mImageViewPager.setVisibility(View.VISIBLE);

        List<SelectedImage> imageList = new ArrayList<>();


        SelectedImagesViewPagerAdapter mAdapter = new SelectedImagesViewPagerAdapter(mContext, imageList);
        mImageViewPager.setAdapter(mAdapter);
        for (Uri uri : imageUris) {

            imageList.add(new SelectedImage(uri));
        }
        mAdapter.notifyDataSetChanged();
    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
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

    private void submitData(
            Uri[] uris,
            final String hotelName,
            final String placeName,
            final double mLat,
            final double mLng,
            final String mAddress,
            final float starRating,
            final String phoneNumber,
            final String moreDetails
    ) {

        mProgress.setMessage("Working please wait...");
        mProgress.setCanceledOnTouchOutside(false);

        mProgress.show();
        imageDownloadUrls = new ArrayList<>();

        for (Uri uri : uris) {

            try {
                Bitmap bitmapImage = getBitmapFromUri(uri);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                final byte[] thumbnailBytes = baos.toByteArray();

                //get the filename. ensure to overwrite the existing file
                //with the same name this saves on firebase storage. by removing duplicates
                //get the filename. ensure to overwrite the existing file

                String fileName = getFileName(uri);

                UploadTask uploadTask = mStorageRef.child("images")
                        .child(fileName)
                        .putBytes(thumbnailBytes);


                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageDownloadUrls.add(taskSnapshot.getDownloadUrl().toString());

                        if (imageDownloadUrls.size() == imageUris.length) {
                            mProgress.setMessage("Saving other details..");

                            DocumentReference docRef = mHotelsRef.document();

                            Hotel hotel = new Hotel(
                                    docRef.getId(),
                                    imageDownloadUrls,
                                    hotelName,
                                    starRating,
                                    mLat,
                                    mLng,
                                    placeName,
                                    mAddress,
                                    phoneNumber,
                                    moreDetails,
                                    Config.isAdmin(mContext)
                            );

                            docRef.set(hotel)
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
                                            Log.e(TAG, "onFailure: " + e.getMessage());
                                            Toast.makeText(mContext, "Error occurred while saving detail please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(mContext, "Error occurred while uploading images", Toast.LENGTH_SHORT).show();
                        if (mProgress.isShowing()) mProgress.dismiss();

                    }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        long percent = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        // if (percent == 100) mProgress.dismiss();
                        mProgress.setMessage(String.valueOf(percent) + "% uploading images please wait...");
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            mProgress.setMessage("Opening Map Please wait...");
            mProgress.show();

            Intent intent = builder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            if (mProgress.isShowing()) mProgress.dismiss();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            if (mProgress.isShowing()) mProgress.dismiss();
        }
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (imageUris.length <= 0) {
            valid = false;
            Toast.makeText(mContext, "You must select at least one photo", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(mHotelName.getText().toString().trim())) {
            valid = false;
            mHotelName.setError("*Required!");
        } else {
            mHotelName.setError(null);
        }

        if (TextUtils.isEmpty(mPlaceName.getText().toString().trim())) {
            valid = false;
            mPlaceName.setError("*Required!");
        } else {
            mPlaceName.setError(null);
        }

        if (TextUtils.isEmpty(mSelectedPinPoint.getText().toString())) {
            valid = false;
            Toast.makeText(mContext, "Please select location from map to continue.", Toast.LENGTH_SHORT).show();
        }

        if (rating <= 0) {
            valid = false;
            Toast.makeText(mContext, "Please select hotel star rating to continue.", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())) {
            valid = false;
            mPhoneNumber.setError("*Required!");
        } else {
            mPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(mMoreDetails.getText().toString().trim())) {
            valid = false;
            mMoreDetails.setError("*Required!");
        } else {
            mMoreDetails.setError(null);
        }
        return valid;
    }


}
