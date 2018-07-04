package com.hudutech.mymanjeri;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.hudutech.mymanjeri.admin.AdminPanelActivity;
import com.hudutech.mymanjeri.admin.DataEntryActivity;
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
    private List<CategoryMenu> manjeryMenuList;
    private List<CategoryMenu> contact1MenuList;
    private List<CategoryMenu> contact2MenuList;
    private List<CategoryMenu> timingAndBookingMenuList;
    private List<CategoryMenu> classfiledMenuList;
    private List<CategoryMenu> medicalMenuList;
    private List<CategoryMenu> digitalMenuList;
    private ImageViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<Banner> barnerList;
    private FirebaseFirestore db;
    private CollectionReference mRootRef;
    private CollectionReference mUsersRef;
    private FirebaseUser mCurrentUser;
    private int[] pagerIndex = {-1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("\t\t\t\tMy Valanchery");
        getSupportActionBar().setIcon(R.drawable.ic_person_white_24dp);


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
        manjeryMenuList = menuHashMap.get(CATEGORY_MAJERY);
        contact1MenuList = menuHashMap.get(CATEGORY_CONTACTS_1);
        contact2MenuList = menuHashMap.get(CATEGORY_CONTACTS_2);
        timingAndBookingMenuList = menuHashMap.get(CATEGORY_TIMING_AND_BOOKING);
        classfiledMenuList = menuHashMap.get(CATEGORY_CLASSIFIEDS);
        medicalMenuList = menuHashMap.get(CATEGORY_MEDICAL);
        digitalMenuList = menuHashMap.get(CATEGORY_DIGITAL);


        //MENU ADAPTERS
        CustomMenuAdapter majeryAdapter = new CustomMenuAdapter(this, manjeryMenuList);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (isAdmin()) {
                showActivity(AddBloodDonorAdminActivity.class);
            } else if (!isAdmin() && mCurrentUser == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You Need Admin Privileges to access this menu");
                builder.setMessage("Do you have admin account ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showActivity(LoginActivity.class);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showActivity(RegisterActivity.class);
                    }
                });

                builder.show();
            } else if (!isAdmin() && mCurrentUser != null) {
                Toast.makeText(getApplicationContext(), "You are not Authorised to use this menu", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_add_barner) {

            if (isAdmin()) {
                showActivity(AdminBarnersActivity.class);
            } else if (!isAdmin() && mCurrentUser == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You Need Admin Privileges to access this menu");
                builder.setMessage("Do you have admin account ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showActivity(LoginActivity.class);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showActivity(RegisterActivity.class);
                    }
                });

                builder.show();
            } else if (!isAdmin() && mCurrentUser != null) {
                Toast.makeText(getApplicationContext(), "You are not Authorised to use this menu", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.action_enter_data) {
            showActivity(DataEntryActivity.class);
        } else if (id == R.id.action_logout) {
            signOut();
        } else if (id == R.id.action_test_user_panel) {
            showActivity(UserPanelActivity.class);
        } else if (id == R.id.action_test_admin_panel) {

            showActivity(AdminPanelActivity.class);
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
                Toast.makeText(MainActivity.this, "You Are logged out", Toast.LENGTH_SHORT).show();

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

    //[CATEGORY MENUS LOGIC HERE]


}
