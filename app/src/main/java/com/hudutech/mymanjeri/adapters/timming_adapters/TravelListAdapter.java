package com.hudutech.mymanjeri.adapters.timming_adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hudutech.mymanjeri.models.Travel;
import com.hudutech.mymanjeri.timing_and_booking_activities.HotelDetailActivity;
import com.hudutech.mymanjeri.timing_and_booking_activities.TravelDetailActivity;

import java.util.List;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.ViewHolder> {

    private List<Travel> travelList;
    private Context mContext;
    private FirebaseFirestore db;
    private ProgressDialog mProgress;

    public TravelListAdapter(Context mContext, List<Travel> travelList) {
        this.travelList = travelList;
        this.mContext = mContext;
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public TravelListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_vehicle_item, parent, false);
        return new TravelListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,  int position) {
        final Travel travel = travelList.get(position);
        mProgress = new ProgressDialog(mContext);
        //Show views accordingly
        holder.imageView.setVisibility(View.VISIBLE);
        holder.mCall.setVisibility(View.GONE);
        holder.mShare.setVisibility(View.GONE);
        holder.mPhoneNumber.setVisibility(View.GONE);
        
        if (Config.isAdmin(mContext)) {
            holder.layoutControl.setVisibility(View.VISIBLE);
            if (travel.isValidated()) {
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
                        holder.delete(travel, holder.getAdapterPosition());

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
                        holder.updateIsValidated(travel, true, holder.getAdapterPosition());

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
                        holder.updateIsValidated(travel, false, holder.getAdapterPosition());

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

        holder.mName.setText(travel.getName());
        holder.mPhoneNumber.setText(travel.getPhoneNumber());
        holder.mLocation.setText(travel.getAddress() +", "+travel.getPlaceName());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(travel.getPhotoUrls().get(0))
                .apply(requestOptions)
                .into(holder.imageView);


        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = "Place "+travel.getPlaceName() +" Contact "+travel.getPhoneNumber();
                Config.share(mContext, travel.getName(),desc);
            }
        });

        holder.mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(mContext, travel.getPhoneNumber());
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, TravelDetailActivity.class)
                        .putExtra("travel", travel)
                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        TextView mName;
        ImageView imageView;
        TextView mPhoneNumber;
        TextView mLocation;
        TextView mShare;
        TextView mCall;
        RelativeLayout layoutContent;
        LinearLayout layoutControl;
        Button mButtonValidate;
        Button mButtonInValidate;
        Button mButtonDelete;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.tv_contact_vehicle_name);
            mPhoneNumber = itemView.findViewById(R.id.tv_contact_vehicle_phone);
            mLocation = itemView.findViewById(R.id.tv_contact_vehicle_location);
            mShare = itemView.findViewById(R.id.tv_contact_vehicle_share);
            mCall = itemView.findViewById(R.id.tv_contact_vehicle_call);
            imageView = itemView.findViewById(R.id.img_contact_vehicle);
            mButtonValidate = itemView.findViewById(R.id.btn_contact_vehicle_validate);
            mButtonInValidate = itemView.findViewById(R.id.btn_contact_vehicle_invalidate);
            mButtonDelete = itemView.findViewById(R.id.btn_contact_vehicle_delete);
            layoutContent = itemView.findViewById(R.id.layout_contact_vehicle_content);
            layoutControl = itemView.findViewById(R.id.layout_contact_vehicle_admin_control);
        }

        public void delete(final Travel travel, final int position) {
            /*
             * First remove the image from firebase storage then
             * delete item from reference. this helps to save on space
             * by not keeping image files for deleted items.
             */
            mProgress.setMessage("Deleting...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            StorageReference photoRef = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(travel.getPhotoUrls().get(0));
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CollectionReference ref = db.collection("travel");
                    ref.document(travel.getDocKey())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    travelList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    if (mProgress.isShowing()) mProgress.dismiss();
                                    Toast.makeText(mContext, "Travel deleted", Toast.LENGTH_SHORT).show();
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

        public void updateIsValidated(final Travel travel, boolean isValidated,  final int position) {
            mProgress.setMessage("Updating please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            db.collection("travel")
                    .document(travel.getDocKey())
                    .update("validated", isValidated)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            db.collection("travel")
                                    .document(travel.getDocKey())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            Travel newItem = documentSnapshot.toObject(Travel.class);
                                            travelList.set(position, newItem);
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
