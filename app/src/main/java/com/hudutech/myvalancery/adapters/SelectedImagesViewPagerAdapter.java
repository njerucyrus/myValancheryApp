package com.hudutech.myvalancery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hudutech.myvalancery.R;
import com.hudutech.myvalancery.models.SelectedImage;

import java.util.List;

public class SelectedImagesViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    //reuse  the banner model
    private List<SelectedImage> selectedImageList;

    public SelectedImagesViewPagerAdapter(Context mContext, List<SelectedImage> selectedImageList) {
        this.mContext = mContext;
        this.selectedImageList = selectedImageList;
    }

    @Override
    public int getCount() {
        return selectedImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_selected_image_item, container, false);
        ImageView imageView = view.findViewById(R.id.selected_image_item);
        SelectedImage image = selectedImageList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_barner);

        Glide.with(mContext)
                .load(image.getImageUri())
                .apply(requestOptions)
                .into(imageView);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
