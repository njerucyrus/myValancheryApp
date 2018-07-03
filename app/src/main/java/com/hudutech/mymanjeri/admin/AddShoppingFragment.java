package com.hudutech.mymanjeri.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.Shop;

import static android.app.Activity.RESULT_OK;


public class AddShoppingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddShoppingFragment";
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextInputEditText mShopName;
    private TextInputEditText mContact;
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

    private CollectionReference mShopsRef;

    public AddShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_shopping, container, false);
        mContext = getContext();

        mShopsRef = FirebaseFirestore.getInstance().collection("shops");

        mProgress = new ProgressDialog(getContext());
        mSelectedPinPoint = view.findViewById(R.id.tv_selected_shop_location);
        mShopName = view.findViewById(R.id.txt_shop_name);
        mContact = view.findViewById(R.id.txt_shop_mobile);
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

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
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
        if (id == R.id.btn_add_shop){
            if (validateInputs()) {
                submitShop();
            }else {
                Snackbar.make(v, "Please fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.btn_select_shop_location) {
            showPlacePicker();
        }
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mShopName.getText().toString().trim())) {
            valid = false;
            mShopName.setError("*Required");
        }else {
            mShopName.setError(null);
        }

        if (TextUtils.isEmpty(mContact.getText().toString().trim())) {
            valid = false;
            mContact.setError("*Required");
        }else {
            mContact.setError(null);
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

    private void submitShop() {
        DocumentReference docRef = mShopsRef.document();
        //do the logic here
        Shop shop = new Shop(
                mShopName.getText().toString().trim(),
                mContact.getText().toString().trim(),
                mLocation,
                mShopType,
                docRef.getId(),
                lat,
                lng,
                true
        );

        mProgress.setMessage("Working please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

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
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });
    }


}
