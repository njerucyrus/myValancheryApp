package com.hudutech.myvalancery.adapters.contact_adapter;

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
import com.hudutech.myvalancery.Config;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.contact_models.Labourer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabourerListAdapter extends RecyclerView.Adapter<LabourerListAdapter.ViewHolder> {

    private List<Labourer> labourerList;
    private Context mContext;
    private FirebaseFirestore db;
    private ProgressDialog mProgress;

    public LabourerListAdapter(Context mContext, List<Labourer> labourerList) {
        this.labourerList = labourerList;
        this.mContext = mContext;
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public LabourerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_vehicle_item, parent, false);
        return new LabourerListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Labourer labourer = labourerList.get(position);
        mProgress = new ProgressDialog(mContext);
        //Show views accordingly
        if (Config.isAdmin(mContext)) {
            holder.layoutControl.setVisibility(View.VISIBLE);
            if (labourer.isValidated()) {
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
                        holder.deleteVehicle(labourer, holder.getAdapterPosition());

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
                        holder.updateIsValidated(labourer, true, holder.getAdapterPosition());

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
                        holder.updateIsValidated(labourer, false, holder.getAdapterPosition());

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

        holder.mName.setText(labourer.getName());
        holder.mPhoneNumber.setText(labourer.getPhoneNumber());
        holder.mLocation.setText(labourer.getPlace());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(labourer.getPhotoUrl())
                .apply(requestOptions)
                .into(holder.imageView);


        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String linkMessage = "\nDownload or rate MyValanchery App By visiting link below \n"+Config.APP_DOWNLOAD_URL;

                String desc = "Place " + labourer.getPlace() + " Contact " + labourer.getPhoneNumber()+linkMessage;
                Config.share(mContext, "Art And Culture", desc);
            }
        });

        holder.mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.call(mContext, labourer.getPhoneNumber());
            }
        });


    }


    @Override
    public int getItemCount() {
        return labourerList.size();
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

        public void deleteVehicle(final Labourer labourer, final int position) {
            /*
             * First remove the image from firebase storage then
             * delete item from reference. this helps to save on space
             * by not keeping image files for deleted items.
             */
            mProgress.setMessage("Deleting...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            CollectionReference ref = db.collection("labourers");
            ref.document(labourer.getDocKey())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            labourerList.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            if (mProgress.isShowing()) mProgress.dismiss();
                            Toast.makeText(mContext, "labourer deleted", Toast.LENGTH_SHORT).show();
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

        public void updateIsValidated(final Labourer labourer, boolean isValidated, final int position) {
            mProgress.setMessage("Updating please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            db.collection("labourers")
                    .document(labourer.getDocKey())
                    .update("validated", isValidated)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            db.collection("labourers")
                                    .document(labourer.getDocKey())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (mProgress.isShowing()) mProgress.dismiss();
                                            Labourer newItem = documentSnapshot.toObject(Labourer.class);
                                            labourerList.set(position, newItem);
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
