package com.hudutech.mymanjeri;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminLiveTvActivity extends AppCompatActivity {

    private DocumentReference mRef;
    private ProgressDialog mProgress;
    private TextInputEditText mLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_live_tv);
        getSupportActionBar().setTitle("Live Tv Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgress = new ProgressDialog(this);
        mLink = findViewById(R.id.txt_link);

        mRef = FirebaseFirestore.getInstance().collection("youtube").document("live_tv");

        Button mButtonSubmit = findViewById(R.id.btn_submit);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mLink.getText().toString().trim())) {
                    HashMap<String, String> mapLink = new HashMap<>();
                    mapLink.put("link", mLink.getText().toString().trim());
                    mProgress.setMessage("Loading please wait");
                    mProgress.show();
                    mRef.set(mapLink)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(AdminLiveTvActivity.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                                    mLink.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(AdminLiveTvActivity.this, "Error occurred while submitting data please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}
