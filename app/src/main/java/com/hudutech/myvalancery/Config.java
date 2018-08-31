package com.hudutech.myvalancery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tylersuehr.chips.ChipsInputLayout;

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

    public static final String APP_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.hudutech.myvalancery";

    public static boolean isAdmin(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isAdmin", false);
    }

    public static boolean isSBAdmin(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isSBAdmin", false);
    }


    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public static boolean validateInputs(Context context, ArrayList<Object> objects) {
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
                        Toast.makeText(context, "Please fill in " + entry.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (object.getClass() == ChipsInputLayout.class) {
                ChipsInputLayout chipsInputLayout = (ChipsInputLayout) object;
                if (chipsInputLayout.getSelectedChips().size() <= 0) {
                    valid = false;
                    Toast.makeText(context, "Please enter show times.", Toast.LENGTH_SHORT).show();
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
                ((CheckBox) view).setChecked(false);
            } else if (view.getClass() == ImageView.class) {
                ImageView imageView = (ImageView) view;
                imageView.setVisibility(View.GONE);
            }

        }

    }

    public static void share(Context context, String title, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, "Share via"));

    }

    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }


}
