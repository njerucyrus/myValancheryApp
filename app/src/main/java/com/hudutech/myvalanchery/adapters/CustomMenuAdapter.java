package com.hudutech.myvalanchery.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hudutech.myvalanchery.AdminPanelActivity;
import com.hudutech.myvalanchery.Config;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.auth.LoginActivity;
import com.hudutech.myvalanchery.models.CategoryMenu;

import java.util.List;

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.ViewHolder> {

    private static final String TAG = "CustomMenuAdapter";
    private List<CategoryMenu> categoryMenuList;
    private Context mContext;
    private FirebaseUser user;

    public CustomMenuAdapter(Context mContext, List<CategoryMenu> categoryMenuList) {
        this.categoryMenuList = categoryMenuList;
        this.mContext = mContext;
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public CustomMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_menu_item, parent, false);
        return new CustomMenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomMenuAdapter.ViewHolder holder, int position) {
        final CategoryMenu menu = categoryMenuList.get(position);
        Drawable drawable = mContext.getResources().getDrawable(menu.getIconResId());
        holder.mMenuIcon.setImageDrawable(drawable);
        holder.tvMenuText.setText(menu.getMenuName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menu.getGotoGlass() == AdminPanelActivity.class) {

                    if (user != null) {
                        if (Config.isAdmin(mContext)) {
                            mContext.startActivity(new Intent(mContext, menu.getGotoGlass())
                                    .putExtra("menuName", menu.getMenuName())
                            );
                        } else {
                            Toast.makeText(mContext, "You are not authorised to view this page", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (user == null) {
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }

                mContext.startActivity(new Intent(mContext, menu.getGotoGlass())
                        .putExtra("menuName", menu.getMenuName())
                        .putExtra("url", menu.getOptionalUrl())
                );
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mMenuIcon;
        TextView tvMenuText;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mMenuIcon = itemView.findViewById(R.id.img_menu_icon);
            tvMenuText = itemView.findViewById(R.id.tv_menu_text);
        }


    }
}
