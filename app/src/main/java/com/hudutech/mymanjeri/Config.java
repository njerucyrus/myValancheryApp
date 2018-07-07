package com.hudutech.mymanjeri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is meant to put together all the
 * shared code between activities and fragments
 * to avoid repeating your self writing same code over
 * and over again
 */
@SuppressLint("Registered")
public class Config extends AppCompatActivity {

    public static boolean isAdmin(Context mContext) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isAdmin", false);
    }

    public static boolean isSBAdmin(Context mContext) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isSBAdmin", false);
    }


    public static Bitmap getBitmapFromUri(Context mContext, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                mContext.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public static boolean validateInputs(Context mContext, ArrayList<Object> objects) {
        boolean valid = true;
        for (Object object : objects) {

            if (object.getClass() == TextInputEditText.class) {
                TextInputEditText view = (TextInputEditText) object;
                if (TextUtils.isEmpty(view.getText().toString().trim())) {
                    view.setError("*Required!");
                    valid = false;
                } else {
                    view.setError(null);
                }
            } else if (object.getClass() == EditText.class) {
                EditText view = (EditText) object;
                if (TextUtils.isEmpty(view.getText().toString().trim())) {
                    view.setError("*Required!");
                    valid = false;
                } else {
                    view.setError(null);
                }
            } else if (object.getClass() == HashMap.class) {
                HashMap<String, String> inputSet = (HashMap) object;
                for (Map.Entry<String, String> entry : inputSet.entrySet()) {
                    if (TextUtils.isEmpty(entry.getValue())) {
                        valid = false;
                        Toast.makeText(mContext, "Please fill in " + entry.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return valid;
    }

    public static void clearInputs(ArrayList<Object> objects) {
        for (Object view : objects) {
            if (view.getClass() == TextInputEditText.class) {
                ((TextInputEditText) view).setText(null);
            } else if (view.getClass() == EditText.class) {
                ((EditText) view).setText(null);
            } else if (view.getClass() == Spinner.class) {
                Spinner spinner = (Spinner) view;
                spinner.setSelection(0, true);
            } else if (view.getClass() == CheckBox.class) {
                ((CheckBox)view).setChecked(false);
            } else if (view.getClass() == ImageView.class){
                ImageView imageView = (ImageView)view;
                imageView.setVisibility(View.GONE);
            }

        }

    }


}
