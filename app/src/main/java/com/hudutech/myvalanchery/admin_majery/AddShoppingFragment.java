package com.hudutech.myvalanchery.admin_majery;

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
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.adapters.SelectedImagesViewPagerAdapter;
import com.hudutech.myvalanchery.models.SelectedImage;
import com.hudutech.myvalanchery.models.majery_models.Shop;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddShoppingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddShoppingFragment";
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int IMAGE_PICK = 100;
    private TextInputEditText mShopName;
    private TextInputEditText mContact;
    private TextInputEditText mPlace;
    private String mLocation;
    private String mShopType;
    private AppCompatSpinner mSpinner;
    private Button mSubmit;
    private Button mPickLocation;
    private Context mContext;
    private TextView mSelectedPinPoint;
    private double lat = 0d;
    private double lng = 0d;
    private ProgressDialog mProgress;

    private Uri[] imageUris = new Uri[0];
    private ViewPager mImageViewPager;
    private TextView tvSelectedImgs;
    private List<String> imageDownloadUrls = new ArrayList<>();
    private StorageReference mStorageRef;

    private CollectionReference mShopsRef;

    public AddShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_shopping, container, false);
        mContext = getContext();

        mImageViewPager = view.findViewById(R.id.selected_restaurant_viewpager_images);
        tvSelectedImgs = view.findViewById(R.id.tv_restaurant_selected_imgs);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mShopsRef = FirebaseFirestore.getInstance().collection("shops");


        mProgress = new ProgressDialog(getContext());
        mSelectedPinPoint = view.findViewById(R.id.tv_selected_shop_location);
        mShopName = view.findViewById(R.id.txt_shop_name);
        mContact = view.findViewById(R.id.txt_shop_mobile);
        mPlace = view.findViewById(R.id.txt_place);
        mSpinner = view.findViewById(R.id.shop_type_spinner);
        mSubmit = view.findViewById(R.id.btn_add_shop);
        mPickLocation = view.findViewById(R.id.btn_select_shop_location);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    mShopType = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mShopType = null;

            }
        });

        mSubmit.setOnClickListener(this);
        mPickLocation.setOnClickListener(this);
        view.findViewById(R.id.btn_choose_restaurant_images).setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
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

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, mContext);
                mSelectedPinPoint.setVisibility(View.VISIBLE);
                mSelectedPinPoint.setText("Selected Shop Location : " + place.getName());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
                mLocation = place.getName().toString();

            }
        }
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_add_shop) {
            if (validateInputs()) {
                submitShop();
            } else {
                Snackbar.make(v, "Please fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.btn_select_shop_location) {
            showPlacePicker();
        } else if (id == R.id.btn_choose_restaurant_images) {
            openImageChooser();
        }
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mShopName.getText().toString().trim())) {
            valid = false;
            mShopName.setError("*Required");
        } else {
            mShopName.setError(null);
        }

        if (TextUtils.isEmpty(mContact.getText().toString().trim())) {
            valid = false;
            mContact.setError("*Required");
        } else {
            mContact.setError(null);
        }

        if (TextUtils.isEmpty(mPlace.getText().toString().trim())) {
            valid = false;
            mPlace.setError("*Required");
        } else {
            mPlace.setError(null);
        }

        if (TextUtils.isEmpty(mLocation)) {
            valid = false;
            Toast.makeText(getContext(), "Please select Location", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(mShopType)) {
            valid = false;
            Toast.makeText(getContext(), "Please select Location", Toast.LENGTH_SHORT).show();
        }

        return valid;
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


    private void submitShop() {

        mProgress.setMessage("Working please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        for (Uri uri : imageUris) {

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

                            DocumentReference docRef = mShopsRef.document();
                            //do the logic here
                            Shop shop = new Shop(
                                    mPlace.getText().toString().trim(),
                                    imageDownloadUrls,
                                    0d,
                                    mShopName.getText().toString().trim(),
                                    mContact.getText().toString().trim(),
                                    mLocation,
                                    mShopType,
                                    docRef.getId(),
                                    lat,
                                    lng,
                                    true
                            );

                            docRef.set(shop)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            Toast.makeText(mContext, "Shop added successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            Log.d(TAG, "onFailure: " + e.getMessage());
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
                        mProgress.setMessage(percent + "% uploading images please wait...");
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


}
