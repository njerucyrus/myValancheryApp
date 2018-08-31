package com.hudutech.myvalancery.timing_and_booking_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.Film;

public class FilmDetailActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private Film film;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        getSupportActionBar().setTitle("Film");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mProgress = new ProgressDialog(this);

        film = (Film) getIntent().getSerializableExtra("film");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(this)
                .load(film.getPhotoUrl())
                .apply(requestOptions)
                .into((ImageView) findViewById(R.id.img_film_photo));

        TextView mTitle = findViewById(R.id.tv_title);
        mTitle.setText(film.getTitle());

        TextView mPlace = findViewById(R.id.tv_place);
        mPlace.setText(film.getPlace());

        TextView mAddress = findViewById(R.id.tv_address);
        mAddress.setText(film.getAddress());

        TextView mShows = findViewById(R.id.tv_shows);
        mShows.setText(film.getShows() + " Show(s)");


        TextView mTimes = findViewById(R.id.tv_show_times);
        StringBuilder times = new StringBuilder();
        for (String time : film.getTimes()) {
            times.append("\t").append(time);
        }
        mTimes.setText(times);

        TextView tvCall = findViewById(R.id.tv_call);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(FilmDetailActivity.this, film.getPhoneNumber());
            }
        });

        Button mButtonValidate = findViewById(R.id.btn_validate);
        Button mButtonInValidate = findViewById(R.id.btn_invalidate);
        Button mButtonDelete = findViewById(R.id.btn_delete);
        LinearLayout layoutControl = findViewById(R.id.layout_admin_control);

        if (Config.isAdmin(FilmDetailActivity.this)) {
            layoutControl.setVisibility(View.VISIBLE);
            if (film.isValidated()) {
                mButtonInValidate.setVisibility(View.VISIBLE);
                mButtonValidate.setVisibility(View.GONE);
            } else {
                mButtonInValidate.setVisibility(View.GONE);
                mButtonValidate.setVisibility(View.VISIBLE);
            }
        }

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmDetailActivity.this);
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Data will be lost permanently and cannot be recovered.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(film);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmDetailActivity.this);
                builder.setTitle("Are you sure you want to Validate?");
                builder.setMessage("Validating data means it will be displayed to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateIsValidated(film, true);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmDetailActivity.this);
                builder.setTitle("Are you sure you want to InValidate?");
                builder.setMessage("This item will become invisible to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateIsValidated(film, false);

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

    private void delete(final Film film) {
        /*
         * First remove the image from firebase storage then
         * delete item from reference. this helps to save on space
         * by not keeping image files for deleted items.
         */
        mProgress.setMessage("Deleting...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        CollectionReference ref = db.collection("films");
        ref.document(film.getDocKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(FilmDetailActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FilmDetailActivity.this, FilmActivity.class)
                                .putExtra("menuName", "Films")
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(FilmDetailActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void updateIsValidated(final Film filmObject, boolean isValidated) {
        mProgress.setMessage("Updating please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        db.collection("films")
                .document(filmObject.getDocKey())
                .update("validated", isValidated)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        db.collection("films")
                                .document(filmObject.getDocKey())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (mProgress.isShowing()) mProgress.dismiss();
                                        film = documentSnapshot.toObject(Film.class);

                                        Toast.makeText(FilmDetailActivity.this, "Updated successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (mProgress.isShowing()) mProgress.dismiss();
                                        Toast.makeText(FilmDetailActivity.this, "Error occurred! Update failed.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(FilmDetailActivity.this, "Error occurred! Update failed.", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
