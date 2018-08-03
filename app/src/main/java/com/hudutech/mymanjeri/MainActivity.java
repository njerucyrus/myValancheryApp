package com.hudutech.mymanjeri;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.mymanjeri.adapters.CustomMenuAdapter;
import com.hudutech.mymanjeri.adapters.ImageViewPagerAdapter;
import com.hudutech.mymanjeri.models.Banner;
import com.hudutech.mymanjeri.models.CategoryMenu;
import com.hudutech.mymanjeri.models.MenuHolder;
import com.hudutech.mymanjeri.models.User;
import com.hudutech.mymanjeri.user.UserPanelActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String CATEGORY_MAJERY = "manjery";
    private static final String CATEGORY_CALL_US = "call_us";
    private static final String CATEGORY_CONTACTS_1 = "contacts_1";
    private static final String CATEGORY_CONTACTS_2 = "contacts_2";
    private static final String CATEGORY_TIMING_AND_BOOKING = "timing_and_booking";
    private static final String CATEGORY_CLASSIFIEDS = "classifieds";
    private static final String CATEGORY_MEDICAL = "medical";
    private static final String CATEGORY_DIGITAL = "digital";
    final int delay = 10000; //8 second
    HashMap<String, List<CategoryMenu>> menuHashMap;
    Handler handler = new Handler();
    Runnable runnable;
    private ImageViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<Banner> barnerList;
    private FirebaseFirestore db;
    private CollectionReference mRootRef;
    private CollectionReference mUsersRef;
    private FirebaseUser mCurrentUser;
    private int[] pagerIndex = {-1};

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("\t\t\t\tMy Valanchery");

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        showStartBanner();


        mRootRef = FirebaseFirestore.getInstance().collection("barners");
        mUsersRef = FirebaseFirestore.getInstance().collection("users");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        barnerList = new ArrayList<>();
        mViewPager = findViewById(R.id.banner_viewpager);
        mAdapter = new ImageViewPagerAdapter(this, barnerList);
        mViewPager.setAdapter(mAdapter);
        loadBanners();

        loadMenus();


    }

    private void loadMenus() {

        //Menu HOLDER CLASS INITIALIZATION
        MenuHolder holder = new MenuHolder();
        //[GET THE Hashmap containing the menu data list]
        menuHashMap = holder.getMenuHashMap();


        //MENU DATA LIST

        List<CategoryMenu> manjeryMenuList = menuHashMap.get(CATEGORY_MAJERY);
        List<CategoryMenu> contact1MenuList = menuHashMap.get(CATEGORY_CONTACTS_1);
        List<CategoryMenu> contact2MenuList = menuHashMap.get(CATEGORY_CONTACTS_2);
        List<CategoryMenu> timingAndBookingMenuList = menuHashMap.get(CATEGORY_TIMING_AND_BOOKING);
        List<CategoryMenu> classfiledMenuList = menuHashMap.get(CATEGORY_CLASSIFIEDS);
        List<CategoryMenu> medicalMenuList = menuHashMap.get(CATEGORY_MEDICAL);
        List<CategoryMenu> digitalMenuList = menuHashMap.get(CATEGORY_DIGITAL);
        List<CategoryMenu> contactUsMenuList = menuHashMap.get(CATEGORY_CALL_US);


        //MENU ADAPTERS
        CustomMenuAdapter majeryAdapter = new CustomMenuAdapter(this, manjeryMenuList);
        CustomMenuAdapter contactUsAdapter = new CustomMenuAdapter(this, contactUsMenuList);
        CustomMenuAdapter contact1Adapter = new CustomMenuAdapter(this, contact1MenuList);
        CustomMenuAdapter contact2Adapter = new CustomMenuAdapter(this, contact2MenuList);
        CustomMenuAdapter timmingAndBookingAdapter = new CustomMenuAdapter(this, timingAndBookingMenuList);
        CustomMenuAdapter classfiledMenuAdapter = new CustomMenuAdapter(this, classfiledMenuList);
        CustomMenuAdapter medicalAdapter = new CustomMenuAdapter(this, medicalMenuList);
        CustomMenuAdapter digitalAdapter = new CustomMenuAdapter(this, digitalMenuList);


        //MAJERY MENU
        RecyclerView majeryRecyclerView = findViewById(R.id.majery_recyclerview);
        majeryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        majeryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        majeryRecyclerView.setAdapter(majeryAdapter);
        majeryRecyclerView.setHasFixedSize(true);

        //CONTACT US MENU
        RecyclerView contactUsRecyclerView = findViewById(R.id.contact_us_recyclerview);
        contactUsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        contactUsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactUsRecyclerView.setAdapter(contactUsAdapter);
        contactUsRecyclerView.setHasFixedSize(true);

        //CONTACT MENU 1st ROW
        RecyclerView contact1RecyclerView = findViewById(R.id.contacts_recyclerview1);
        contact1RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        contact1RecyclerView.setItemAnimator(new DefaultItemAnimator());
        contact1RecyclerView.setAdapter(contact1Adapter);
        contact1RecyclerView.setHasFixedSize(true);

        //CONTACT MENU 2nd ROW
        RecyclerView contact2RecyclerView = findViewById(R.id.contacts_recyclerview2);
        contact2RecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        contact2RecyclerView.setItemAnimator(new DefaultItemAnimator());
        contact2RecyclerView.setAdapter(contact2Adapter);
        contact2RecyclerView.setHasFixedSize(true);

        //TIMMING AND BOOKING MENU
        RecyclerView timingAndBookingRecyclerView = findViewById(R.id.timing_and_booking_recyclerview);
        timingAndBookingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        timingAndBookingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        timingAndBookingRecyclerView.setAdapter(timmingAndBookingAdapter);
        timingAndBookingRecyclerView.setHasFixedSize(true);

        //CLASSFIELD MENU
        RecyclerView classifiedRecyclerView = findViewById(R.id.classifieds_recyclerview);
        classifiedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        classifiedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        classifiedRecyclerView.setAdapter(classfiledMenuAdapter);
        classifiedRecyclerView.setHasFixedSize(true);


        //MEDICAL MENU
        RecyclerView medicalRecyclerView = findViewById(R.id.medical_recyclerview);
        medicalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        medicalRecyclerView.setItemAnimator(new DefaultItemAnimator());
        medicalRecyclerView.setAdapter(medicalAdapter);
        medicalRecyclerView.setHasFixedSize(true);

        //DIGITAL MENU
        RecyclerView digitalRecyclerView = findViewById(R.id.digital_recyclerview);
        digitalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        digitalRecyclerView.setItemAnimator(new DefaultItemAnimator());
        digitalRecyclerView.setAdapter(digitalAdapter);
        digitalRecyclerView.setHasFixedSize(true);

    }


    private void loadBanners() {
        mRootRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Banner barner = snapshot.toObject(Banner.class);
                                barnerList.add(barner);

                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login = menu.findItem(R.id.action_logout);

        if (mCurrentUser == null) {
            login.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        if (id == R.id.action_logout) {
            signOut();
        } else if (id == R.id.action_user_panel) {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(MainActivity.this, UserPanelActivity.class));
            } else {
                //CREATE ACCOUNT//
                startActivity(new Intent(MainActivity.this, JoinActivity.class));
            }


        }

        return super.onOptionsItemSelected(item);
    }

    private void showActivity(Class<?> klass) {
        startActivity(new Intent(this, klass));
    }

    @Override
    protected void onStart() {
        handler.postDelayed(new Runnable() {
                                public void run() {
                                    pagerIndex[0]++;
                                    if (pagerIndex[0] >= mAdapter.getCount()) {
                                        pagerIndex[0] = 0;
                                    }

                                    mViewPager.setCurrentItem(pagerIndex[0]);
                                    runnable = this;

                                    handler.postDelayed(runnable, delay);
                                }
                            }
                , delay);


        if (mCurrentUser != null) {
            //setup account
            initAccount();
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void initAccount() {
        DocumentReference docRef = mUsersRef.document(mCurrentUser.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {

                            SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                            sharedPrefEditor.putBoolean("isAdmin", user.isMainAdmin());
                            sharedPrefEditor.putBoolean("isSBAdmin", user.isSBAdmin());
                            sharedPrefEditor.apply();
                            sharedPrefEditor.commit();


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    private boolean isAdmin() {

        SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isAdmin", false);

    }

    private boolean isSBAdmin() {
        SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isSBAdmin", false);
    }


    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to logout?");
        builder.setMessage("You will be logged out");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                SharedPreferences sharedPrefs = getSharedPreferences("AUTH_DATA",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
                sharedPrefEditor.putBoolean("isAdmin", false);
                sharedPrefEditor.putBoolean("isSBAdmin", false);
                sharedPrefEditor.apply();
                sharedPrefEditor.commit();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                Toast.makeText(MainActivity.this, "You Are logged out", Toast.LENGTH_SHORT).show();
                finish();

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

    private void showStartBanner() {
        TextView txtClose;

        dialog.setContentView(R.layout.layout_start_banner);
        txtClose = (TextView) dialog.findViewById(R.id.txt_close);
        final ImageView mSelectedPhoto = (ImageView) dialog.findViewById(R.id.imageView);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("start_banners").document("banner");
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Banner banner = documentSnapshot.toObject(Banner.class);
                            mSelectedPhoto.setVisibility(View.VISIBLE);
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.no_barner);

                            Glide.with(MainActivity.this)
                                    .load(banner.getBannerUrl())
                                    .apply(requestOptions)
                                    .into(mSelectedPhoto);
                        }
                    }
                });


        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
