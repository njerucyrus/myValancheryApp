package com.hudutech.mymanjeri.majery_activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.History;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    private History history;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        loadData();


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
            String message = "Place Name " + history.getPlaceName() + "More Info " + history.getDescription();
            Config.share(HistoryActivity.this, "Historical  Place", message);
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete(final History history) {
        /*
         * First remove the image from firebase storage then
         * delete item from reference. this helps to save on space
         * by not keeping image files for deleted items.
         */
        mProgress.setMessage("Deleting...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        StorageReference photoRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl(history.getImageUrl());
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CollectionReference ref = FirebaseFirestore.getInstance().collection("history");
                ref.document(history.getDocKey())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                if (mProgress.isShowing()) mProgress.dismiss();
                                Toast.makeText(HistoryActivity.this, "History record deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (mProgress.isShowing()) mProgress.dismiss();
                                Toast.makeText(HistoryActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mProgress.isShowing()) mProgress.dismiss();
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
                Toast.makeText(HistoryActivity.this, "Error occurred. data not deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateIsValidated(final History history, boolean isValidated) {
        mProgress.setMessage("Updating please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("history")
                .document(history.getDocKey())
                .update("validated", isValidated)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(HistoryActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        Toast.makeText(HistoryActivity.this, "Error occurred! Update failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadData() {
        mProgress.setMessage("loading please wait...");
        mProgress.show();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("history").document("history_item");
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                        if (documentSnapshot.exists()) {
                            history = documentSnapshot.toObject(History.class);

                            ImageView imageView = findViewById(R.id.imageView);
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.no_barner);


                            Glide.with(HistoryActivity.this)
                                    .load(history.getImageUrl())
                                    .apply(requestOptions)
                                    .into(imageView);

                            TextView textView = findViewById(R.id.textView);
                            textView.setText(history.getDescription());
                            LinearLayout layoutControl = findViewById(R.id.layout_admin_control);
                            Button mButtonInValidate = findViewById(R.id.btn_invalidate);
                            Button mButtonValidate = findViewById(R.id.btn_validate);
                            Button mButtonDelete = findViewById(R.id.btn_delete);

                            if (Config.isAdmin(HistoryActivity.this)) {
                                layoutControl.setVisibility(View.VISIBLE);
                                if (history.isValidated()) {
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setMessage("Data will be lost permanently and cannot be recovered.");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            delete(history);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                    builder.setTitle("Are you sure you want to Validate?");
                                    builder.setMessage("Validating data means it will be displayed to the app users.");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateIsValidated(history, false);

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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                                    builder.setTitle("Are you sure you want to Validate?");
                                    builder.setMessage("Validating data means it will be displayed to the app users.");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateIsValidated(history, true);

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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mProgress.isShowing()) mProgress.dismiss();
                    }
                });
    }
}
