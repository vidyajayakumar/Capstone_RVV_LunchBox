package com.vidya.lunchbox.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.vidya.lunchbox.model.ItemMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ItemsActivity extends AppCompatActivity {

    public static Carteasy cs;
    //    String[] mNamesArray;
//    String[] mDescriptionArray;
//    int[] mCostArray;
//    TypedArray mimagesArray;
    ArrayList<ItemMenu> adapterItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Toolbar toolbar;
    private ArrayList<Cart> mItems;
    //    String newString;
    private String categoryId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Bundle extras = getIntent().getExtras();
        categoryId = extras.getString("CategoryId");
        adapterItems = new ArrayList<>();
        getItemMenus();

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

//        addingCategoryItems(newString);
//
//        adapterItems = new ArrayList<Items>();
//        for (int i = 0; i < mCostArray.length; i++) {
//
//            Items species = new Items();
//            species.setProductid(getAlphaNumericString(4));
//            Log.d("random string", species.getProductid());
//            species.setName(mNamesArray[i]);
//            species.setDescription(mDescriptionArray[i]);
//            species.setPrice(mCostArray[i]);
//            species.setThumbnail(mimagesArray.getResourceId(i, -1));
//            adapterItems.add(species);
//        }

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

    private void getItemMenus() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("itemMenus");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ItemMenu menu = postSnapshot.getValue(ItemMenu.class);
                    if (menu.getCatId().equals(categoryId))
                        adapterItems.add(menu);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
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

}
