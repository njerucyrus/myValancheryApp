package com.hudutech.mymanjeri.adapters.classifields_adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.classifields_activities.RealEstateDetailActivity;
import com.hudutech.mymanjeri.models.classifields_models.RealEstate;

import java.util.List;

public class RealEstateListAdapter extends RecyclerView.Adapter<RealEstateListAdapter.ViewHolder> {

    private List<RealEstate> realEstateList;
    private Context mContext;
    private FirebaseFirestore db;
    private ProgressDialog mProgress;

    public RealEstateListAdapter(Context mContext, List<RealEstate> realEstateList) {
        this.realEstateList = realEstateList;
        this.mContext = mContext;
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public RealEstateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_classifieds_item, parent, false);
        return new RealEstateListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final RealEstate realEstate = realEstateList.get(position);
        mProgress = new ProgressDialog(mContext);
        //Show views accordingly
        if (Config.isAdmin(mContext)) {
            holder.layoutControl.setVisibility(View.VISIBLE);
            if (realEstate.isValidated()) {
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
                        holder.deleteVehicle(realEstate, holder.getAdapterPosition());

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
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to Validate?");
                builder.setMessage("Validating data means it will be displayed to the app users.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.updateIsValidated(realEstate, true, holder.getAdapterPosition());

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
                        holder.updateIsValidated(realEstate, false, holder.getAdapterPosition());

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
        String amount = "INR " + realEstate.getAmount();
        holder.tvTitle.setText(realEstate.getDescription());
        holder.tvPrice.setText(amount);


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(realEstate.getPhotoUrls().get(0))
                .apply(requestOptions)
                .into(holder.imageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, RealEstateDetailActivity.class)
                        .putExtra("realEstate", realEstate)

                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return realEstateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";

        LinearLayout layoutContent;
        LinearLayout layoutControl;
        TextView tvTitle;
        TextView tvPrice;
        Button mButtonValidate;
        Button mButtonInValidate;
        Button mButtonDelete;
        ImageView imageView;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mButtonValidate = itemView.findViewById(R.id.btn_validate);
            mButtonInValidate = itemView.findViewById(R.id.btn_invalidate);
            mButtonDelete = itemView.findViewById(R.id.btn_delete);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imageView = itemView.findViewById(R.id.img_classified);
            layoutContent = itemView.findViewById(R.id.layout_content);
            layoutControl = itemView.findViewById(R.id.layout_admin_control);
        }

        public void deleteVehicle(final RealEstate realEstate, final int position) {
            /*
             * First remove the image from firebase storage then
             * delete item from reference. this helps to save on space
             * by not keeping image files for deleted items.
             */
            mProgress.setMessage("Deleting...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            CollectionReference ref = db.collection("classifields_real_estate");
            ref.document(realEstate.getDocKey())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            realEstateList.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Toast.makeText(mContext, "record deleted", Toast.LENGTH_SHORT).show();
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

        public void updateIsValidated(final RealEstate realEstate, boolean isValidated, final int position) {
            mProgress.setMessage("Updating please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            db.collection("classifields_real_estate")
                    .document(realEstate.getDocKey())
                    .update("validated", isValidated)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            db.collection("classifields_real_estate")
                                    .document(realEstate.getDocKey())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            RealEstate newItem = documentSnapshot.toObject(RealEstate.class);
                                            realEstateList.set(position, newItem);
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
