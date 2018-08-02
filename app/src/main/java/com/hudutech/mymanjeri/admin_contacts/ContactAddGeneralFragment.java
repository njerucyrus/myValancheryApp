package com.hudutech.mymanjeri.admin_contacts;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.contact_models.General;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactAddGeneralFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ContactAddGeneralFragme";
    private TextInputEditText mName;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mPlace;
    private CollectionReference mGeneralRef;
    private Context mContext;
    private ProgressDialog mProgress;

    public ContactAddGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_general, container, false);

        mContext = getContext();
        mProgress = new ProgressDialog(getContext());

        mName = v.findViewById(R.id.txt_general_name);
        mPhoneNumber = v.findViewById(R.id.txt_general_phone_number);
        mPlace = v.findViewById(R.id.txt_general_place);
        mGeneralRef = FirebaseFirestore.getInstance().collection("general");
        v.findViewById(R.id.btn_submit_general).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_submit_general) {
            if (validateInputs()) {
                submitData(mName.getText().toString(), mPhoneNumber.getText().toString(), mPlace.getText().toString());
            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            valid = false;
            mName.setError("*Required!");
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mPlace.getText().toString().trim())) {
            valid = false;
            mPlace.setError("*Required!");
        } else {
            mPlace.setError(null);
        }

        if (TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())) {
            valid = false;
            mPhoneNumber.setError("*Required!");
        } else {
            mPhoneNumber.setError(null);
        }

        return valid;
    }

    private void submitData(String name, String phoneNumber, String placeName) {
        DocumentReference docRef = mGeneralRef.document();
        General general = new General(
                docRef.getId(),
                name,
                placeName,
                phoneNumber,
                Config.isAdmin(mContext)
        );

        mProgress.setMessage("Submitting please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        docRef.set(general)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(mContext, "Data Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(mContext, "Error occurred while submitting data!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }
}
