package com.hudutech.myvalancery.adapters.digital_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.digital_activities.EnterPasswordActivity;
import com.hudutech.myvalancery.models.digital_models.SBankAccount;

import java.util.List;

public class SBAccountsAdapter extends RecyclerView.Adapter<SBAccountsAdapter.ViewHolder> {

    private List<SBankAccount> sBankAccountList;
    private Context mContext;

    public SBAccountsAdapter(Context mContext, List<SBankAccount> sBankAccountList) {
        this.sBankAccountList = sBankAccountList;
        this.mContext = mContext;

    }

    @Override
    public SBAccountsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sb_account_item, parent, false);
        return new SBAccountsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SBankAccount account = sBankAccountList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.user_96);

        Glide.with(mContext)
                .load(account.getPhotoUrl())
                .apply(requestOptions)
                .into(holder.mAccountPhoto);

        String accountNo = "Account No: " + account.getAccountNo();
        String batchNo = "Batch No: " + account.getBatchNo();
        String name = "Holder Name: " + account.getCustomerName();

        holder.mAccountNo.setText(accountNo);
        holder.mBatchNo.setText(batchNo);
        holder.mHolderName.setText(name);
        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to enter password to delete activity.
                mContext.startActivity(new Intent(mContext, EnterPasswordActivity.class)
                        .putExtra("account", account)
                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return sBankAccountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAccountNo;
        TextView mHolderName;
        TextView mBatchNo;
        ImageView mAccountPhoto;
        Button mButtonDelete;
        View mView;


        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            mAccountNo = itemView.findViewById(R.id.tv_holder_account_no);
            mHolderName = itemView.findViewById(R.id.tv_sb_account_holder_name);
            mBatchNo = itemView.findViewById(R.id.tv_holder_batch_no);
            mAccountPhoto = itemView.findViewById(R.id.img_account_photo);
            mButtonDelete = itemView.findViewById(R.id.btn_delete_sb_account);


        }


    }
}
