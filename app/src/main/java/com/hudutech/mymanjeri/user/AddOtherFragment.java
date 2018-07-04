package com.hudutech.mymanjeri.user;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.SelectedImagesViewPagerAdapter;
import com.hudutech.mymanjeri.models.SelectedImage;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOtherFragment extends Fragment {

    private static final String TAG = "AddOtherFragment";
    private static final int IMAGE_PICK = 100;
    private Context mContext;
    private Uri[] imageUris = new Uri[0];
    private ViewPager mImageViewPager;
    private TextView tvSelectedImgs;
    private ProgressDialog mProgress;
    private List<String> imageDownloadUrls;
    private StorageReference mStorageRef;
    private CollectionReference mHotelsRef;
    private TextView mSelectedPinPoint;

    public AddOtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_other, container, false);
        mContext = getContext();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {


            if (data.getClipData() != null) {
                if (data.getClipData().getItemCount() > 5) {
                    // do needful here
                    Toast.makeText(mContext, "Only Max of 5 images allowed", Toast.LENGTH_LONG).show();
                    return;
                }

                int count = data.getClipData().getItemCount();
                imageUris = new Uri[count];
                for (int i = 0; i < count; i++) {
                    imageUris[i] = data.getClipData().getItemAt(i).getUri();
                }
            } else {
                imageUris = new Uri[1];
                imageUris[0] = data.getData();
            }
            showSelectedImages(imageUris);
        }

    }

    private void showSelectedImages(Uri[] imageUris) {
        tvSelectedImgs.setVisibility(View.VISIBLE);
        mImageViewPager.setVisibility(View.VISIBLE);

        List<SelectedImage> imageList = new ArrayList<>();


        SelectedImagesViewPagerAdapter mAdapter = new SelectedImagesViewPagerAdapter(mContext, imageList);
        mImageViewPager.setAdapter(mAdapter);
        for (Uri uri : imageUris) {

            imageList.add(new SelectedImage(uri));
        }
        mAdapter.notifyDataSetChanged();
    }



    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getFileName(Uri uri) {
        String fileName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.

        try (Cursor cursor = mContext.getContentResolver()
                .query(uri, null, null, null, null, null)) {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                fileName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + fileName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
            }
        }

        return fileName;
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                mContext.getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
