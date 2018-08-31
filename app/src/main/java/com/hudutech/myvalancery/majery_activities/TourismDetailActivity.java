package com.hudutech.myvalancery.majery_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.majery_models.TourismPlace;

public class TourismDetailActivity extends AppCompatActivity {
    private static final String TAG = "TourismDetailActivity";
    private TourismPlace tourismPlace;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tourismPlace = (TourismPlace) getIntent().getSerializableExtra("tourismPlace");
        getSupportActionBar().setTitle(tourismPlace.getPlaceName());

        mProgress = new ProgressDialog(this);

        ImageView imageView = findViewById(R.id.imageView);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(this)
                .load(tourismPlace.getImageUrl())
                .apply(requestOptions)
                .into(imageView);

        TextView textView = findViewById(R.id.textView);
        textView.setText(tourismPlace.getDescription());
        LinearLayout layoutControl = findViewById(R.id.layout_admin_control);
        Button mButtonInValidate = findViewById(R.id.btn_invalidate);
        Button mButtonValidate = findViewById(R.id.btn_validate);
        Button mButtonDelete = findViewById(R.id.btn_delete);

        if (Config.isAdmin(this)) {
            layoutControl.setVisibility(View.VISIBLE);
            if (tourismPlace.isValidated()) {
                mButtonInValidate.setVisibility(View.VISIBLE);
                mButtonValidate.setVisibility(View.GONE);
            } else {
                mButtonInValidate.setVisibility(View.GONE);
                mButtonValidate.setVisibility(View.VISIBLE);
            }
        } else {
            layoutControl.setVisibility(View.GONE);
        }


        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TourismDetailActivity.this);
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Data will be lost permanently and cannot be recovered.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(tourismPlace);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        mButtonInValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TourismDetailActivity.this);
                builder.setTitle("Are you sure you want to Validate?");
                builder.setMessage("Validating data means it will be displayed to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateIsValidated(tourismPlace, false);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        mButtonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TourismDetailActivity.this);
                builder.setTitle("Are you sure you want to Validate?");
                builder.setMessage("Validating data means it will be displayed to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateIsValidated(tourismPlace, true);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_share) {
            String message = "Place Name " + tourismPlace.getPlaceName() + "More Info " + tourismPlace.getDescription();
            Config.share(TourismDetailActivity.this, "Tourism Place", message);
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete(final TourismPlace tourismPlace) {
        /*
         * First remove the image from firebase storage then
         * delete item from reference. this helps to save on space
         * by not keeping image files for deleted items.
         */
        mProgress.setMessage("Deleting...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        CollectionReference ref = FirebaseFirestore.getInstance().collection("tourism");
        ref.document(tourismPlace.getDocKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(TourismDetailActivity.this, "Tourism record deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TourismDetailActivity.this, TourismActivity.class)
                                .putExtra("menuName", "Tourism")
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(TourismDetailActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void updateIsValidated(final TourismPlace tourismPlace, boolean isValidated) {
        mProgress.setMessage("Updating please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tourism")
                .document(tourismPlace.getDocKey())
                .update("validated", isValidated)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(TourismDetailActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(TourismDetailActivity.this, "Error occurred! Update failed.", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
