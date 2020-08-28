package com.vidya.lunchbox.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.adapter.ViewCartAdapter;
import com.vidya.lunchbox.cart.Carteasy;
import com.vidya.lunchbox.model.Cart;
import com.vidya.lunchbox.utils.DeleteClickListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Map;


public class ViewCartActivity extends AppCompatActivity {

    ArrayList<Cart> mItems;
    private Toolbar toolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TextView NoOfItems;
    private MaterialButton continuebutton, checkout;
    private ImageView emptyCart;
    private TextView emptyCartText, total_price;
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        getSupportActionBar().hide();
        //Set a Toolbar to replace the ActionBar.
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

        //Retrieve the cart details - begin

        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(getApplicationContext());

        emptyCart = (ImageView) findViewById(R.id.empty_cart);
        emptyCartText = (TextView) findViewById(R.id.empty_cart_text);
        if (data == null || data.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.VISIBLE);
        }
        total_price = (TextView) findViewById(R.id.total_price);


        mItems = new ArrayList<Cart>();
        Cart cartitem = new Cart();
        if (data != null && data.size() != 0) {
            for (Map.Entry<Integer, Map> entry : data.entrySet()) {

                //Retrieve the values of the Map by starting from index 0 - zero

                cartitem = new Cart();
                //Get the sub values of the Map
                Map<String, String> innerdata = entry.getValue();
                for (Map.Entry<String, String> innerEntry : innerdata.entrySet()) {
                    System.out.println(innerEntry.getKey() + "=" + innerEntry.getValue());

                    String product = innerEntry.getKey();
                    switch (product) {
                        case "product id":
                            cartitem.setProductid(innerEntry.getValue());
                            break;
                        case "product name":
                            cartitem.setName(innerEntry.getValue());
                            break;
                        case "product desc":
                            cartitem.setDescription(innerEntry.getValue());
                            break;
                        case "product qty":
                            cartitem.setQuantity(Integer.parseInt(innerEntry.getValue()));
                            break;
//                        case "product strength":
//                            cartitem.setmStrength(innerEntry.getValue());
//                            break;
                        case "product price":
                            cartitem.setPrice(Double.parseDouble(innerEntry.getValue()));
                            break;
                        case "product thumbnail":
                            cartitem.setThumbnail(innerEntry.getValue());
                            break;
                    }
                    Log.e("Total", total + "");

                }
                total = total + (cartitem.getPrice() * cartitem.getQuantity());
                total_price.setText("Total : $" + total);
                mItems.add(cartitem);
            }
        }

        //Set MyBag ( NoOfItems );
        NoOfItems = (TextView) findViewById(R.id.no_of_items);
        NoOfItems.setText("MY BAG (" + Integer.toString(mItems.size()) + ")");

        //Retrieve the cart details - end


        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new ViewCartAdapter(ViewCartActivity.this, mItems, new DeleteClickListener() {
            @Override
            public void onClick(Cart item) {
                total = total - (item.getPrice() * item.getQuantity());
                total_price.setText("Total : $" + total);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        //Navigate the User back to the display activity
        continuebutton = findViewById(R.id.continueshopping);
        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total = 0;
                Context context = v.getContext();
                Intent intent = new Intent(context, CategoryListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
            }
        });

        //Navigate the User back to the display activity
        checkout = findViewById(R.id.checkoutbutton);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItems.size() > 0) {

//                    showAlertDialog();
                    ItemsActivity.cs.clearCart(ViewCartActivity.this);
                    Intent i = new Intent(ViewCartActivity.this, PaymentActivity.class);
                    i.putParcelableArrayListExtra("items",mItems);
                    // set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish(); // call this to finish the current activity
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showAlertDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(
                ViewCartActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Add Address");

        // Setting Dialog Message
        alertDialog.setMessage("Please add Address to deliver the products");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.address);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //ItemsActivity.cs.clearCart(ViewCartActivity.this);
                Intent i = new Intent(ViewCartActivity.this, AddressActivity.class);
                i.putParcelableArrayListExtra("items", mItems);
                startActivity(i);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}