package com.hudutech.myvalanchery.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.hudutech.myvalanchery.Config;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.models.majery_models.BloodDonor;

import java.util.List;


public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {

    private List<BloodDonor> bloodDonorList;
    private Context mContext;
    private ProgressDialog mProgress;
    private FirebaseFirestore db;

    public BloodDonorAdapter(Context mContext, List<BloodDonor> bloodDonorList) {
        this.bloodDonorList = bloodDonorList;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donor_person, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BloodDonor bloodDonor = bloodDonorList.get(position);
        holder.mDesc.setText(bloodDonor.getFullName() + "\n" + bloodDonor.getPhoneNumber() + "\n" + bloodDonor.getAddress());

        mProgress = new ProgressDialog(mContext);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.default_user);


        Glide.with(mContext)
                .load(bloodDonor.getAvator())
                .apply(requestOptions)
                .into(holder.mBloodDonorImage);

        if (Config.isAdmin(mContext)) {
            holder.layoutControl.setVisibility(View.VISIBLE);
            if (bloodDonor.isValidated()) {
                holder.mButtonInValidate.setVisibility(View.VISIBLE);
                holder.mButtonValidate.setVisibility(View.GONE);
            } else {
                holder.mButtonInValidate.setVisibility(View.GONE);
                holder.mButtonValidate.setVisibility(View.VISIBLE);
            }
        }

        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Data will be lost permanently and cannot be recovered.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.delete(bloodDonor, holder.getAdapterPosition());

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

        holder.mButtonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to Validate?");
                builder.setMessage("Validating data means it will be displayed to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.updateIsValidated(bloodDonor, true, holder.getAdapterPosition());

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

        holder.mButtonInValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to InValidate?");
                builder.setMessage("This item will become invisible to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.updateIsValidated(bloodDonor, false, holder.getAdapterPosition());

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

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = "Name " + bloodDonor.getFullName() + " Blood Group " + bloodDonor.getBloodGroup() + " Location " + bloodDonor.getAddress();
                Config.share(mContext, "Blood Donor", desc);
            }
        });

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(mContext, bloodDonor.getPhoneNumber());
            }
        });


    }


    @Override
    public int getItemCount() {
        return bloodDonorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "ViewHolder";
        TextView mDesc;
        ImageButton btnShare;
        ImageButton btnCall;
        ImageView mBloodDonorImage;
        LinearLayout layoutControl;
        Button mButtonValidate;
        Button mButtonInValidate;
        Button mButtonDelete;


        public ViewHolder(final View itemView) {
            super(itemView);
            mDesc = itemView.findViewById(R.id.txt_donor_desc);
            btnShare = itemView.findViewById(R.id.btn_share);
            btnCall = itemView.findViewById(R.id.btn_call);
            mBloodDonorImage = itemView.findViewById(R.id.image_blood_donor);

            mButtonValidate = itemView.findViewById(R.id.btn_validate);
            mButtonInValidate = itemView.findViewById(R.id.btn_invalidate);
            mButtonDelete = itemView.findViewById(R.id.btn_delete);
            layoutControl = itemView.findViewById(R.id.layout_admin_control);


        }

        @Override
        public void onClick(View view) {
            // [HANDLE CLICKS OF THE VIEWS]
        }


        public void delete(final BloodDonor bloodDonor, final int position) {
            /*
             * First remove the image from firebase storage then
             * delete item from reference. this helps to save on space
             * by not keeping image files for deleted items.
             */
            mProgress.setMessage("Deleting...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            CollectionReference ref = db.collection("donors");
            ref.document(bloodDonor.getDocKey())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            bloodDonorList.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Toast.makeText(mContext, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    });


        }

        public void updateIsValidated(final BloodDonor bloodDonor, boolean isValidated, final int position) {
            mProgress.setMessage("Updating please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            db.collection("donors")
                    .document(bloodDonor.getDocKey())
                    .update("validated", isValidated)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            db.collection("donors")
                                    .document(bloodDonor.getDocKey())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            BloodDonor newItem = documentSnapshot.toObject(BloodDonor.class);
                                            bloodDonorList.set(position, newItem);
                                            notifyItemChanged(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(mContext, "Updated successfully.", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            Toast.makeText(mContext, "Error occurred! Update failed.", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Toast.makeText(mContext, "Error occurred! Update failed.", Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }


}
