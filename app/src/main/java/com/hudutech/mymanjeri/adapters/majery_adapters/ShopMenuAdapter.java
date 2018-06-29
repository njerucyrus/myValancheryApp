package com.hudutech.mymanjeri.adapters.majery_adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.models.majery_models.ShopMenu;

import java.util.List;

public class ShopMenuAdapter extends RecyclerView.Adapter<ShopMenuAdapter.ViewHolder> {

    private List<ShopMenu> shopMenuList;
    private Context mContext;

    public ShopMenuAdapter(Context mContext, List<ShopMenu> shopMenuList) {
        this.shopMenuList = shopMenuList;
        this.mContext = mContext;

    }

    @Override
    public ShopMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shop_menu_item, parent, false);
        return new ShopMenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShopMenu shopMenu = shopMenuList.get(position);

        Drawable drawable = mContext.getResources().getDrawable(shopMenu.getResId());
        holder.imageView.setImageDrawable(drawable);

        holder.mTitle.setText(shopMenu.getMenuTitle());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, shopMenu.getGotoClass())
                        .putExtra("shopType", shopMenu.getMenuTitle())
                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return shopMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView imageView;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = itemView.findViewById(R.id.tv_shop_menu_title);
            imageView = itemView.findViewById(R.id.img_shop_menu_item);


        }


    }

}