package com.hudutech.myvalancery.admin_timing_and_booking;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.Bus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText mBusName;
    private TextInputEditText mStartingPoint;
    private TextInputEditText mEndingPoint;
    private TextInputEditText mWay;
    private TextInputEditText mDepartureTime;
    private TextInputEditText mArrivalTime;
    private ArrayList<Object> inputs;
    private ProgressDialog mProgress;
    private CollectionReference mRef;
    private Context mContext;


    public BusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus, container, false);
        mContext = getContext();

        mBusName = v.findViewById(R.id.txt_bus_name);
        mStartingPoint = v.findViewById(R.id.txt_starting_point);
        mEndingPoint = v.findViewById(R.id.txt_ending_point);
        mWay = v.findViewById(R.id.txt_way);
        mDepartureTime = v.findViewById(R.id.txt_departure_time);
        mArrivalTime = v.findViewById(R.id.txt_arrival_time);
        mProgress = new ProgressDialog(getContext());
        mRef = FirebaseFirestore.getInstance().collection("buses");

        v.findViewById(R.id.btn_submit).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        inputs = new ArrayList<>();
        inputs.add(mBusName);
        inputs.add(mStartingPoint);
        inputs.add(mEndingPoint);
        inputs.add(mWay);
        inputs.add(mArrivalTime);
        inputs.add(mDepartureTime);

        final int id = v.getId();
        if (id == R.id.btn_submit) {
            if (Config.validateInputs(mContext, inputs)) {

                submit(mBusName.getText().toString(), mStartingPoint.getText().toString(), mEndingPoint.getText().toString(), mWay.getText().toString(), mArrivalTime.getText().toString(), mDepartureTime.getText().toString());

            } else {
                Snackbar.make(v, "Fix the errors above", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void submit(String busName, String startPoint, String endPoint, String way, String arrivalTime, String departureTime) {
        mProgress.setMessage("Submitting please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        DocumentReference docRef = mRef.document();
        Bus bus = new Bus(
                docRef.getId(),
                busName,
                startPoint,
                endPoint,
                way,
                departureTime,
                arrivalTime,
                Config.isAdmin(mContext)
        );

        docRef.set(bus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(mContext, "Submitted successfully", Toast.LENGTH_SHORT).show();
                        Config.clearInputs(inputs);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
