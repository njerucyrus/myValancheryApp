package com.hudutech.mymanjeri.contact_activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.contact_models.Mp;

public class MpActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_mp);
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

        mRef = FirebaseFirestore.getInstance().collection("mps");
        mProgress = new ProgressDialog(this);
        displayData();
    }

    private void displayData() {
        mProgress.setMessage("loading...");
        mProgress.show();
        mRef.document("mp")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {

                        mProgress.dismiss();
                            if (snapshot.exists()) {
                                Mp mp = snapshot.toObject(Mp.class);
                                if (mp != null) {
                                    tvName.setText(mp.getName());
                                    tvConstituency.setText(mp.getConstituency());
                                    tvParty.setText(mp.getParty());
                                    tvMobile.setText(mp.getMobile());
                                    tvPhone.setText(mp.getPhone());
                                    tvEmail.setText(mp.getEmail());
                                    tvFax.setText("");
                                    tvAddress.setText(mp.getAddress());
                                    RequestOptions requestOptions = new RequestOptions()
                                            .placeholder(R.drawable.user_96);

                                    Glide.with(MpActivity.this)
                                            .load(mp.getPhotoUrl())
                                            .apply(requestOptions)
                                            .into(imgPhoto);
                                }else {
                                    Toast.makeText(MpActivity.this, "No data!", Toast.LENGTH_SHORT).show();
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
