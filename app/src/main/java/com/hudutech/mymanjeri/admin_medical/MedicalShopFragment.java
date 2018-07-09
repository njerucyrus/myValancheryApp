package com.hudutech.mymanjeri.admin_medical;


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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.hudutech.mymanjeri.models.medical_models.MedicalShop;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicalShopFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MedicalShopFragment";
    private static final int IMAGE_PICK = 100;
    private static final int PLACE_PICKER_REQUEST = 200;
    private TextInputEditText mHospitalName;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mPlace;
    private ImageView mSelectedPhoto;
    private CollectionReference mHospitalsRef;
    private ProgressDialog mProgress;
    private Context mContext;
    private StorageReference mStorageRef;
    private Uri photoUri;
    private String photoUploaded = null;
    private String mAdress;
    private TextView mSelectedPinPoint;
    private double lat;
    private double lng;

    private ArrayList<Object> inputs;

    public MedicalShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_medical_shop, container, false);
        mHospitalName = v.findViewById(R.id.txt_name);
        mPhoneNumber = v.findViewById(R.id.txt_phone_number);
        mPlace = v.findViewById(R.id.txt_place);
        mSelectedPhoto = v.findViewById(R.id.img_photo);
        mSelectedPinPoint = v.findViewById(R.id.tv_location);



        v.findViewById(R.id.btn_upload_photo).setOnClickListener(this);

        v.findViewById(R.id.btn_submit).setOnClickListener(this);
        v.findViewById(R.id.btn_select_location).setOnClickListener(this);


        //end of validated inputs


        mContext = getContext();
        mProgress = new ProgressDialog(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mHospitalsRef = FirebaseFirestore.getInstance().collection("medical_shops");

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

        if (requestCode == PLACE_PICKER_REQUEST) {
            //dismiss the progress dialog
            if (mProgress.isShowing()) mProgress.dismiss();

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, mContext);
                mSelectedPinPoint.setVisibility(View.VISIBLE);
                mSelectedPinPoint.setText("Selected Location : " + place.getName());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
                mAdress = place.getName().toString();

            }
        }
    }

    @Override
    public void onClick(View v) {
        //validated inputs
        inputs = new ArrayList<>();
        inputs.add(mHospitalName);
        inputs.add(mPhoneNumber);
        inputs.add(mPlace);
        inputs.add(mSelectedPhoto);
        HashMap<String, String> stringInputs = new HashMap<>();
        stringInputs.put("Photo", photoUploaded);
        stringInputs.put("Location", mAdress);
        inputs.add(stringInputs);


        final  int id = v.getId();

        if (id == R.id.btn_submit) {
            if (Config.validateInputs(mContext,inputs)){
                submitData(
                        photoUri,
                        mHospitalName.getText().toString(),
                        mPhoneNumber.getText().toString(),
                        mPlace.getText().toString(),
                        mAdress,
                        lat,
                        lng

                );
            }else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.btn_upload_photo){
            openImageChooser();
        } else if (id == R.id.btn_select_location) {
            showPlacePicker();
        }

    }

    private void submitData(
            Uri uri,
            final String shopName,
            final String phoneNumber,
            final String place,
            final String address,
            final double mLat,
            final double mLng
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
            String fileName = mHospitalsRef.document().getId();

            UploadTask uploadTask = mStorageRef.child("images")
                    .child(fileName)
                    .putBytes(thumbnailBytes);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String imageUrl = taskSnapshot.getDownloadUrl().toString();

                    DocumentReference docRef = mHospitalsRef.document();

                    MedicalShop medicalShop = new MedicalShop(
                            docRef.getId(),
                            imageUrl,
                            shopName,
                            phoneNumber,
                            place,
                            address,
                            mLat,
                            mLng,
                            Config.isAdmin(mContext)
                    );

                    docRef.set(medicalShop)
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
                    Log.e(TAG, "onFailure: "+e.getMessage() );

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
}
