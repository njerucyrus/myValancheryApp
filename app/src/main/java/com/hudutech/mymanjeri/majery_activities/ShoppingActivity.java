package com.hudutech.mymanjeri.majery_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.adapters.majery_adapters.ShopMenuAdapter;
import com.hudutech.mymanjeri.models.majery_models.ShopMenu;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private List<ShopMenu> shopMenuList;
    private ShopMenuAdapter mAdapter;


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
        loadShopMenus();


    }

    private void loadShopMenus() {
        ShopMenu iceCream = new ShopMenu(
                "Ice Cream",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(iceCream);

        ShopMenu superMarket = new ShopMenu(
                "Super Market",
                R.drawable.no_icon_48,
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
                R.drawable.no_icon_48,
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
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(electronics);

        ShopMenu builders = new ShopMenu(
                "Builders",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );
        shopMenuList.add(builders);


        ShopMenu flex_and_print = new ShopMenu(
                "Flex & Print",
                R.drawable.flex_and_print_64,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(flex_and_print);


        ShopMenu furniture = new ShopMenu(
                "Furniture",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(furniture);


        ShopMenu generalStores = new ShopMenu(
                "General Stores",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(generalStores);

        ShopMenu jewellery = new ShopMenu(
                "Jewellery",
                R.drawable.no_icon_48,
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
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(homeAppliances);


        ShopMenu electricalShops = new ShopMenu(
                "Electrical Shop",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(electricalShops);


        ShopMenu butcherShops = new ShopMenu(
                "Butcher Shop",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(butcherShops);


        ShopMenu hardware_and_tools = new ShopMenu(
                "Hardware&Tools",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );


        shopMenuList.add(hardware_and_tools);


        ShopMenu stationary = new ShopMenu(
                "Stationary",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(stationary);


        ShopMenu paints = new ShopMenu(
                "Paints",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(paints);


        ShopMenu mobileShop = new ShopMenu(
                "Mobile Shop",
                R.drawable.no_icon_48,
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
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(computerShop);


        ShopMenu sportShop = new ShopMenu(
                "Sport Shop",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(sportShop);


        ShopMenu studio = new ShopMenu(
                "Studio",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(studio);


        ShopMenu tiles_and_sanitary = new ShopMenu(
                "Tiles&Sanitary",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(tiles_and_sanitary);


        ShopMenu catering = new ShopMenu(
                "Catering",
                R.drawable.no_icon_48,
                ShopMenuDetailActivity.class
        );

        shopMenuList.add(catering);

        mAdapter.notifyDataSetChanged();

    }
}
