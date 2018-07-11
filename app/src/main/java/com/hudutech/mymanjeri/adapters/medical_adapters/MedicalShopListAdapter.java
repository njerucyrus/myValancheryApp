package com.hudutech.mymanjeri.adapters.medical_adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.medical_models.MedicalShop;
import com.hudutech.mymanjeri.models.medical_models.MedicalShop;

import java.util.List;

public class MedicalShopListAdapter extends RecyclerView.Adapter<MedicalShopListAdapter.ViewHolder> {

    private List<MedicalShop> medicalShopList;
    private Context mContext;
    private FirebaseFirestore db;
    private ProgressDialog mProgress;

    public MedicalShopListAdapter(Context mContext, List<MedicalShop> medicalShopList) {
        this.medicalShopList = medicalShopList;
        this.mContext = mContext;
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public MedicalShopListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_medical_item, parent, false);
        return new MedicalShopListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MedicalShop medicalShop = medicalShopList.get(position);
        mProgress = new ProgressDialog(mContext);

        holder.mMap.setVisibility(View.VISIBLE);
        //Show views accordingly
        if (Config.isAdmin(mContext)) {
            holder.layoutControl.setVisibility(View.VISIBLE);
            if (medicalShop.isValidated()) {
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
                        holder.delete(medicalShop, holder.getAdapterPosition());

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
                        holder.updateIsValidated(medicalShop, true, holder.getAdapterPosition());

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
                        holder.updateIsValidated(medicalShop, false, holder.getAdapterPosition());

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

        holder.mName.setText(medicalShop.getShopName());
        holder.mPhoneNumber.setText(medicalShop.getPhoneNumber());
        holder.mLocation.setText(medicalShop.getPlace());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(medicalShop.getPhotoUrl())
                .apply(requestOptions)
                .into(holder.imageView);



    }


    @Override
    public int getItemCount() {
        return medicalShopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        TextView mName;
        ImageView imageView;
        TextView mPhoneNumber;
        TextView mLocation;
        TextView mShare;
        TextView mCall;
        TextView mMap;
        RelativeLayout layoutContent;
        LinearLayout layoutControl;
        Button mButtonValidate;
        Button mButtonInValidate;
        Button mButtonDelete;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.tv_name);
            mPhoneNumber = itemView.findViewById(R.id.tv_phone);
            mLocation = itemView.findViewById(R.id.tv_location);
            mShare = itemView.findViewById(R.id.tv_share);
            mCall = itemView.findViewById(R.id.tv_call);
            mMap = itemView.findViewById(R.id.tv_map);
            imageView = itemView.findViewById(R.id.img_item);
            mButtonValidate = itemView.findViewById(R.id.btn_validate);
            mButtonInValidate = itemView.findViewById(R.id.btn_invalidate);
            mButtonDelete = itemView.findViewById(R.id.btn_delete);
            layoutContent = itemView.findViewById(R.id.layout_content);
            layoutControl = itemView.findViewById(R.id.layout_admin_control);
        }

        public void delete(final MedicalShop medicalShop, final int position) {
            /*
             * First remove the image from firebase storage then
             * delete item from reference. this helps to save on space
             * by not keeping image files for deleted items.
             */
            mProgress.setMessage("Deleting...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            StorageReference photoRef = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(medicalShop.getPhotoUrl());
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CollectionReference ref = db.collection("labs");
                    ref.document(medicalShop.getDocKey())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    medicalShopList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(mContext, " deleted", Toast.LENGTH_SHORT).show();
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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (mProgress.isShowing()) mProgress.dismiss();
                    // Uh-oh, an error occurred!
                    Log.d(TAG, "onFailure: did not delete file");
                    Toast.makeText(mContext, "Error occurred. data not deleted", Toast.LENGTH_SHORT).show();
                }
            });

        }

        public void updateIsValidated(final MedicalShop medicalShop, boolean isValidated, final int position) {
            mProgress.setMessage("Updating please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            db.collection("medical_shops")
                    .document(medicalShop.getDocKey())
                    .update("validated", isValidated)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            db.collection("medical_shops")
                                    .document(medicalShop.getDocKey())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            MedicalShop newItem = documentSnapshot.toObject(MedicalShop.class);
                                            medicalShopList.set(position, newItem);
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
