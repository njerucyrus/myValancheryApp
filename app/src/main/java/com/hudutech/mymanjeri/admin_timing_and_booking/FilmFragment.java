package com.hudutech.mymanjeri.admin_timing_and_booking;


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
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.Film;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipsInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "FilmFragment";
    private static final int IMAGE_PICK = 100;
    private TextInputEditText mTitle;
    private TextInputEditText mShows;
    private TextInputEditText mPlace;
    private TextInputEditText mAddress;
    private TextInputEditText mContactNo;
    private ChipsInputLayout timeChips;
    private ImageView mSelectedPhoto;
    private CollectionReference mRef;
    private StorageReference mStorageRef;
    private ProgressDialog mProgress;
    private Context mContext;
    private Uri photoUri;
    private ArrayList<Object> inputs;
    private String photoSelected = null;

    public FilmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_film, container, false);
        mContext = getContext();

        mTitle = v.findViewById(R.id.txt_title);
        mShows = v.findViewById(R.id.txt_shows);
        mPlace = v.findViewById(R.id.txt_place);
        mAddress = v.findViewById(R.id.txt_address);
        mContactNo = v.findViewById(R.id.txt_phone_number);
        timeChips = v.findViewById(R.id.chips_input);
        mSelectedPhoto = v.findViewById(R.id.img_selected_photo);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseFirestore.getInstance().collection("films");
        mProgress = new ProgressDialog(getContext());

        v.findViewById(R.id.btn_upload_photo).setOnClickListener(this);
        v.findViewById(R.id.btn_submit).setOnClickListener(this);

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

                photoSelected = "selected";

            } else {
                mSelectedPhoto.setVisibility(View.GONE);
                photoSelected = null;
            }
        }

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        inputs = new ArrayList<>();
        inputs.add(mTitle);
        inputs.add(mAddress);
        inputs.add(mShows);
        inputs.add(mPlace);
        inputs.add(mContactNo);
        inputs.add(timeChips);

        HashMap<String, String> stringInput = new HashMap<>();
        stringInput.put("Photo", photoSelected);
        inputs.add(stringInput);
        if (id == R.id.btn_submit) {
            if (Config.validateInputs(mContext, inputs)) {
                List<String> times = new ArrayList<>();
                for (Chip chip: timeChips.getSelectedChips()) {
                    times.add(chip.getTitle());
                }
                submitData(
                        photoUri,
                        mTitle.getText().toString(),
                        Integer.parseInt(mShows.getText().toString()),
                        mPlace.getText().toString(),
                        mAddress.getText().toString(),
                        mContactNo.getText().toString(),
                        times
                );

            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG);
            }
        } else if (id == R.id.btn_upload_photo) {
            openImageChooser();
        }
    }

    private void submitData(
            Uri uri,
            final String title,
            final int shows,
            final String place,
            final String address,
            final String contactNo,
            final List<String> showTimes

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
            String fileName = mRef.document().getId();

            UploadTask uploadTask = mStorageRef.child("images")
                    .child(fileName)
                    .putBytes(thumbnailBytes);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String imageUrl = taskSnapshot.getDownloadUrl().toString();

                    DocumentReference docRef = mRef.document();

                    Film film = new Film(
                            docRef.getId(),
                            imageUrl,
                            title,
                            place,
                            address,
                            contactNo,
                            shows,
                            showTimes,
                            0.0,
                            0.0,
                            Config.isAdmin(mContext)

                    );


                    docRef.set(film)
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
