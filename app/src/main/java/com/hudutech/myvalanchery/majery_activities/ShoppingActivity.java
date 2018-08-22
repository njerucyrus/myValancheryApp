package com.hudutech.myvalanchery.majery_activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudutech.myvalanchery.R;
import com.hudutech.myvalanchery.adapters.ImageViewPagerAdapter;
import com.hudutech.myvalanchery.adapters.majery_adapters.ShopMenuAdapter;
import com.hudutech.myvalanchery.models.Banner;
import com.hudutech.myvalanchery.models.majery_models.ShopMenu;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private static final String TAG = "ShoppingActivity";
    final int delay = 10000; // seconds to delay
    Handler handler = new Handler();
    Runnable runnable;
    private List<ShopMenu> shopMenuList;
    private ShopMenuAdapter mAdapter;
    private ImageViewPagerAdapter imageAdapter;
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
        setContentView(R.layout.activity_shopping);
        getSupportActionBar().setTitle(getIntent().getStringExtra("menuName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shopMenuList = new ArrayList<>();
        mAdapter = new ShopMenuAdapter(this, shopMenuList);
        RecyclerView mRecyclerView = findViewById(R.id.shop_menu_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mRootRef = FirebaseFirestore.getInstance().collection("barners");

        barnerList = new ArrayList<>();
        mViewPager = findViewById(R.id.banner_viewpager);
        imageAdapter = new ImageViewPagerAdapter(this, barnerList);
        mViewPager.setAdapter(imageAdapter);

        loadBanners();

        loadShopMenus();


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
                            imageAdapter.notifyDataSetChanged();
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


    private void loadShopMenus() {
        ShopMenu iceCream = new ShopMenu(
                "Ice Cream",
                R.drawable.ice_cream64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(iceCream);

        ShopMenu superMarket = new ShopMenu(
                "Super Market",
                R.drawable.super_market64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(superMarket);


        ShopMenu cafe = new ShopMenu(
                "Cafe",
                R.drawable.cafe_64,
                ShopMenuDetailActivity.class
        );
        shopMenuList.add(cafe);

        ShopMenu textile = new ShopMenu(
                "Textile",
                R.drawable.textlile_64,
                ShopMenuDetailActivity.class
        );
        shopMenuList.add(textile);

        ShopMenu vegetables = new ShopMenu(
                "Vegetables",
                R.drawable.vegitable_48,
                ShopMenuDetailActivity.class
        );
        shopMenuList.add(vegetables);


        ShopMenu bakery = new ShopMenu(
                "Bakery",
                R.drawable.bakery_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(bakery);


        ShopMenu electronics = new ShopMenu(
                "Electronics",
                R.drawable.electronics_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(electronics);

        ShopMenu builders = new ShopMenu(
                "Builders",
                R.drawable.builders_64,
                ShopMenuDetailActivity.class
        );
        shopMenuList.add(builders);


        ShopMenu flex_and_print = new ShopMenu(
                "Flex&Print",
                R.drawable.flex_and_print_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(flex_and_print);


        ShopMenu furniture = new ShopMenu(
                "Furniture",
                R.drawable.furniture_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(furniture);


        ShopMenu generalStores = new ShopMenu(
                "General Stores",
                R.drawable.general_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(generalStores);

        ShopMenu jewellery = new ShopMenu(
                "Jewellery",
                R.drawable.jewllery_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(jewellery);


        ShopMenu fancyWear = new ShopMenu(
                "Fancy&Foot-Wear",
                R.drawable.fancy_footware_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(fancyWear);


        ShopMenu homeAppliances = new ShopMenu(
                "Home Appliances",
                R.drawable.home_appliances64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(homeAppliances);


        ShopMenu electricalShops = new ShopMenu(
                "Electrical Shop",
                R.drawable.electicals_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(electricalShops);


        ShopMenu butcherShops = new ShopMenu(
                "Butcher Shop",
                R.drawable.butcher_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(butcherShops);


        ShopMenu hardware_and_tools = new ShopMenu(
                "Hardware&Tools",
                R.drawable.hardwares_48,
                ShopMenuDetailActivity.class
        );


        shopMenuList.add(hardware_and_tools);


        ShopMenu stationary = new ShopMenu(
                "Stationary",
                R.drawable.stationery_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(stationary);


        ShopMenu paints = new ShopMenu(
                "Paints",
                R.drawable.paint_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(paints);


        ShopMenu mobileShop = new ShopMenu(
                "Mobile Shop",
                R.drawable.mobile_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(mobileShop);


        ShopMenu beauty_parlour_and_salon = new ShopMenu(
                "Beauty Parlour&Salon",
                R.drawable.beuty_and_pauler_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(beauty_parlour_and_salon);


        ShopMenu computerShop = new ShopMenu(
                "Computer Shop",
                R.drawable.computer_shop48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(computerShop);


        ShopMenu sportShop = new ShopMenu(
                "Sport Shop",
                R.drawable.sports_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(sportShop);


        ShopMenu studio = new ShopMenu(
                "Studio",
                R.drawable.studio_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(studio);


        ShopMenu tiles_and_sanitary = new ShopMenu(
                "Tiles&Sanitary",
                R.drawable.tiles_and_sanitary48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(tiles_and_sanitary);


        ShopMenu catering = new ShopMenu(
                "Catering",
                R.drawable.catering_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(catering);

        mAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onStart() {
        handler.postDelayed(new Runnable() {
                                public void run() {
                                    pagerIndex[0]++;
                                    if (pagerIndex[0] >= imageAdapter.getCount()) {
                                        pagerIndex[0] = 0;
                                    }

                                    mViewPager.setCurrentItem(pagerIndex[0]);
                                    runnable = this;

                                    handler.postDelayed(runnable, delay);
                                }
                            }
                , delay);
        super.onStart();
    }
}
