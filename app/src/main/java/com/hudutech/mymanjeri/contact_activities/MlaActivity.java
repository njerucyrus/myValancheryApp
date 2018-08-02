package com.hudutech.mymanjeri.contact_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.contact_models.Mla;

public class MlaActivity extends AppCompatActivity {
    private static final String TAG = "MpActivity";
    private TextView tvName;
    private TextView tvConstituency;
    private TextView tvParty;
    private TextView tvMobile;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvFax;
    private TextView tvAddress;
    private ImageView imgPhoto;
    private CollectionReference mRef;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tv_name);
        tvConstituency = findViewById(R.id.tv_constituency);
        tvParty = findViewById(R.id.tv_party);
        tvMobile = findViewById(R.id.tv_mobile);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvFax = findViewById(R.id.tv_fax);
        imgPhoto = findViewById(R.id.img_passport);
        tvAddress = findViewById(R.id.tv_address);

        mRef = FirebaseFirestore.getInstance().collection("mlas");
        mProgress = new ProgressDialog(this);
        displayData();
    }

    private void displayData() {
        mProgress.setMessage("loading...");
        mProgress.show();
        mRef.document("mla")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        mProgress.dismiss();
                        if (snapshot.exists()) {
                            final Mla mla = snapshot.toObject(Mla.class);
                            if (mla != null) {
                                tvName.setText(mla.getName());
                                tvConstituency.setText(mla.getConstituency());
                                tvParty.setText(mla.getParty());
                                tvMobile.setText(mla.getMobile());
                                tvPhone.setText(mla.getPhone());
                                tvEmail.setText(mla.getEmail());
                                tvFax.setText("");
                                tvAddress.setText(mla.getAddress());
                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.drawable.user_96);

                                Glide.with(MlaActivity.this)
                                        .load(mla.getPhotoUrl())
                                        .apply(requestOptions)
                                        .into(imgPhoto);

                                TextView call = findViewById(R.id.tv_call);
                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Config.call(MlaActivity.this, mla.getPhone());
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                    }
                });
    }
}

