package com.vidya.lunchbox.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.cart.Carteasy;
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.model.Items;

import java.util.ArrayList;
import java.util.Base64;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView imgThumbnail;
    public TextView name;
    public TextView description;
    public TextView price;
    private Button addtocart;
    private Toolbar toolbar;
    private ItemMenu mItem;
    private ArrayList<String> SizeArray;
    private ArrayList<Integer> QuantityArray;
    private ArrayList<String> ColorArray;

    private String strengthSelected;
    private int quantitySelected;
    private String colorSelected;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().hide();
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ADD TO CART");
        toolbar.setTitleTextColor(Color.WHITE);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mItem = getIntent().getParcelableExtra("mItem");
        }

        imgThumbnail = (ImageView) findViewById(R.id.img_thumbnail);
        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        price = (TextView) findViewById(R.id.price);

        name.setText(mItem.getItemName());
        description.setText(mItem.getItemDesc());
        price.setText("$".concat(String.valueOf(mItem.getPrice())));

        String pureBase64 = mItem.getItemImage().split(",")[1];
        final byte[] decodedBytes = Base64.getDecoder().decode(pureBase64);
        Glide.with(this).load(decodedBytes).into(imgThumbnail);

        /* for fill your Spinner */
        QuantityArray = new ArrayList<Integer>();
        QuantityArray.add(1);
        QuantityArray.add(2);
        QuantityArray.add(3);
        QuantityArray.add(4);
        QuantityArray.add(5);
        QuantityArray.add(6);
        QuantityArray.add(7);
        QuantityArray.add(8);
        QuantityArray.add(9);
        QuantityArray.add(10);

        ArrayAdapter<Integer> qtyAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, QuantityArray);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner qtyspinner = (Spinner) findViewById(R.id.qtyspinner);
        qtyspinner.setAdapter(qtyAdapter);
        qtyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                quantitySelected = QuantityArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                quantitySelected = QuantityArray.get(1);
            }
        });

        addtocart = (Button) findViewById(R.id.addtocart);
        addtocart.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.addtocart) {

            //Set size, Quantity and Color
//            myNewItem.setmStrength(strengthSelected);
//            myNewItem.setQuantity(quantitySelected);


            //Using carteasy - begin

            Carteasy cs = new Carteasy();
            cs.add(mItem.getItemId(), "product id", mItem.getItemId());
            cs.add(mItem.getItemId(), "product name", mItem.getItemName());
            cs.add(mItem.getItemId(), "product desc", mItem.getItemDesc());
            cs.add(mItem.getItemId(), "product price", mItem.getPrice());
            cs.add(mItem.getItemId(), "product thumbnail", mItem.getItemImage());
//            cs.add(mItem.getItemId(), "product strength", myNewItem.getmStrength());
            cs.add(mItem.getItemId(), "product qty", quantitySelected);
            cs.commit(getApplicationContext());
            cs.persistData(getApplicationContext(), false);

            //Using carteasy - end

            Toast.makeText(DetailActivity.this, "Added to cart...", Toast.LENGTH_SHORT).show();
            Context context = v.getContext();
            Intent intent = new Intent(context, ViewCartActivity.class);
            context.startActivity(intent);
            finish();
        }
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
}
