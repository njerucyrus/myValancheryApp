package com.hudutech.myvalanchery.adapters.digital_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.models.digital_models.TransactionRecord;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionRecord> transactionRecordList;
    private Context mContext;

    public TransactionAdapter(Context mContext, List<TransactionRecord> transactionRecordList) {
        this.transactionRecordList = transactionRecordList;
        this.mContext = mContext;

    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sb_transaction_record_item, parent, false);
        return new TransactionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionRecord record = transactionRecordList.get(position);
        holder.mAmount.setText(record.getDesc());
        if (record.getTransactionType() == 1) {
            holder.mPurpose.setVisibility(View.VISIBLE);
            holder.mPurpose.setText(record.getPurpose());
        }

        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = mSimpleDateFormat.format(record.getDate());

        holder.mDate.setText(date);


    }


    @Override
    public int getItemCount() {
        return transactionRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAmount;
        TextView mPurpose;
        TextView mDate;
        View mView;


        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            mAmount = itemView.findViewById(R.id.txt_txn_amount);
            mPurpose = itemView.findViewById(R.id.txt_txn_purpose);
            mDate = itemView.findViewById(R.id.txt_txn_date);


        }


    }
}
