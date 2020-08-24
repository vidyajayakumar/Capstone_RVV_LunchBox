package com.vidya.lunchbox.view;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.adapter.GridAdapter;
import com.vidya.lunchbox.cart.Carteasy;
import com.vidya.lunchbox.model.Cart;
import com.vidya.lunchbox.model.Items;
import com.vidya.lunchbox.utils.CategoryNames;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Toolbar toolbar;
    private ArrayList<Cart> mItems;
    public static Carteasy cs;

    String[] mNamesArray;
    String[] mDescriptionArray;
    int[] mCostArray;
    TypedArray mimagesArray;
    ArrayList<Items> adapterItems;
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        newString = extras.getString("CategoryName");

        getSupportActionBar().hide();
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        addingCategoryItems(newString);

        adapterItems = new ArrayList<Items>();
        for (int i = 0; i < mCostArray.length; i++) {

            Items species = new Items();
            species.setProductid(getAlphaNumericString(4));
            Log.d("random string", species.getProductid());
            species.setName(mNamesArray[i]);
            species.setDescription(mDescriptionArray[i]);
            species.setPrice(mCostArray[i]);
            species.setThumbnail(mimagesArray.getResourceId(i, -1));
            adapterItems.add(species);
        }

        mAdapter = new GridAdapter(this, adapterItems);
        mRecyclerView.setAdapter(mAdapter);

        //Retrieve the cart details - begin
        Map<Integer, Map> data;
        cs = new Carteasy();
        data = cs.ViewAll(getApplicationContext());
        mItems = new ArrayList<Cart>();
        Cart cartitem = new Cart();

        //check if retrieved any size greater than 0 & not null
        if (data != null && data.size() > 0) {
            for (Map.Entry<Integer, Map> entry : data.entrySet()) {
                mItems.add(cartitem);
            }
        }

        //Set MyBag ( NoOfItems );
        TextView NoOfItems = (TextView) findViewById(R.id.no_of_items);
        NoOfItems.setText("MY BAG (" + Integer.toString(mItems.size()) + ")");

        //Navigate to Cart Activity
        LinearLayout myBag = (LinearLayout) findViewById(R.id.my_bag);
        myBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewCartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addingCategoryItems(String categoryName) {

        switch (categoryName) {
            case CategoryNames.MAINCOURSE_CATEGORY:

                mNamesArray = getResources().getStringArray(R.array.maincourse_names);
                mDescriptionArray = getResources().getStringArray(R.array.maincourse_desc);
                mCostArray = getResources().getIntArray(R.array.maincourse_cost);
                mimagesArray = getResources().obtainTypedArray(R.array.maincourse_imgs);

                break;
            case CategoryNames.DESERTS_CATEGORY:

                mNamesArray = getResources().getStringArray(R.array.apetizers_names);
                mDescriptionArray = getResources().getStringArray(R.array.apetizers_desc);
                mCostArray = getResources().getIntArray(R.array.apetizers_cost);
                mimagesArray = getResources().obtainTypedArray(R.array.apetizers_imgs);

                break;
            case CategoryNames.BEVARAGES_CATEGORY:

                mNamesArray = getResources().getStringArray(R.array.desert_names);
                mDescriptionArray = getResources().getStringArray(R.array.desert_desc);
                mCostArray = getResources().getIntArray(R.array.desert_cost);
                mimagesArray = getResources().obtainTypedArray(R.array.desert_imgs);

                break;
            case CategoryNames.APETIZERS_CATEGORY:

                mNamesArray = getResources().getStringArray(R.array.bevarages_names);
                mDescriptionArray = getResources().getStringArray(R.array.bevarages_desc);
                mCostArray = getResources().getIntArray(R.array.bevarages_cost);
                mimagesArray = getResources().obtainTypedArray(R.array.bevarages_imgs);

                break;
            default:
                break;
        }

    }

    // function to generate a random string of length n
    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}