package com.hudutech.mymanjeri.adapters.contact_adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.mymanjeri.Config;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.contact_models.Vehicle;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

    private List<Vehicle> vehicleList;
    private Context mContext;

    public VehicleListAdapter(Context mContext, List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
        this.mContext = mContext;

    }

    @Override
    public VehicleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_vehicle_item, parent, false);
        return new VehicleListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Vehicle vehicle = vehicleList.get(position);

        holder.mName.setText(vehicle.getName());
        holder.mPhoneNumber.setText(vehicle.getPhoneNumber());
        holder.mLocation.setText(vehicle.getPlace());

        //
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(vehicle.getPhotoUrl())
                .apply(requestOptions)
                .into(holder.imageView);

        if (new Config(mContext).isAdmin()) {
            holder.layoutControl.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView imageView;
        TextView mPhoneNumber;
        TextView mLocation;
        TextView mShare;
        TextView mCall;
        RelativeLayout layoutContent;
        LinearLayout layoutControl;
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
            layoutContent = itemView.findViewById(R.id.layout_contact_vehicle_content);
            layoutControl = itemView.findViewById(R.id.layout_contact_vehicle_admin_control);

        }


    }
}
