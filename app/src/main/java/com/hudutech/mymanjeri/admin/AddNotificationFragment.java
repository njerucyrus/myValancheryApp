package com.hudutech.mymanjeri.admin;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.CustomNotification;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNotificationFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "AddNotificationFragment";
    private TextInputEditText mNotification;
    private Button mSubmit;
    private Context mContext;
    private ProgressDialog mProgress;
    private CollectionReference mNotifRef;
    public AddNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_notification, container, false);
        mContext = getContext();
        mProgress = new ProgressDialog(getContext());
        mNotification = view.findViewById(R.id.txt_add_notification);
        mSubmit = view.findViewById(R.id.btn_submit_notification);
        mNotifRef = FirebaseFirestore.getInstance().collection("notifications");
        mSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_submit_notification) {
            if (validateInputs()){
                submitData(mNotification.getText().toString().trim());
            }else {
                Snackbar.make(v, "Fix the errors above",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void submitData(String notifMsg) {
        DocumentReference docRef = mNotifRef.document();
        CustomNotification notification = new CustomNotification(
                docRef.getId(),
                notifMsg,
                true,
                new Date()
        );
        mProgress.setMessage("Submitting data please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        docRef.set(notification)
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

    private boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(mNotification.getText().toString().trim())){
            valid = false;
            mNotification.setError("*Required");
        }else {
            mNotification.setError(null);
        }
        return valid;
    }
}
