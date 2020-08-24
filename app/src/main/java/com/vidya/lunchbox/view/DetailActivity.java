package com.vidya.lunchbox.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.appcompat.app.AppCompatActivity;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.cart.Carteasy;
import com.vidya.lunchbox.model.Items;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addtocart;
    private Toolbar toolbar;
    private int id;

    private ArrayList<Items> mItems;
    private Items myNewItem;

    public ImageView imgThumbnail;
    public TextView name;
    public TextView description;
    public TextView price;

    private ArrayList<String> SizeArray;
    private ArrayList<Integer> QuantityArray;
    private ArrayList<String> ColorArray;

    private String strengthSelected;
    private int quantitySelected;
    private String colorSelected;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            mItems = (ArrayList<Items>) getIntent().getSerializableExtra("mItems");
        }

        imgThumbnail = (ImageView) findViewById(R.id.img_thumbnail);
        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        price = (TextView) findViewById(R.id.price);

        myNewItem = mItems.get(id);
        name.setText(myNewItem.getName());
        description.setText(myNewItem.getDescription());
        price.setText("$" + Integer.toString(myNewItem.getPrice()));
        imgThumbnail.setImageResource(myNewItem.getThumbnail());

        /* for fill your Spinner */
        QuantityArray = new ArrayList<Integer>();
        QuantityArray.add(1);
        QuantityArray.add(2);
        QuantityArray.add(3);
        QuantityArray.add(4);
        QuantityArray.add(5);

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
            myNewItem.setmStrength(strengthSelected);
            myNewItem.setQuantity(quantitySelected);


            //Using carteasy - begin

            Carteasy cs = new Carteasy();
            cs.add(myNewItem.getProductid(), "product id", myNewItem.getProductid());
            cs.add(myNewItem.getProductid(), "product name", myNewItem.getName());
            cs.add(myNewItem.getProductid(), "product desc", myNewItem.getDescription());
            cs.add(myNewItem.getProductid(), "product price", myNewItem.getPrice());
            cs.add(myNewItem.getProductid(), "product thumbnail", myNewItem.getThumbnail());
            cs.add(myNewItem.getProductid(), "product strength", myNewItem.getmStrength());
            cs.add(myNewItem.getProductid(), "product qty", myNewItem.getQuantity());
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
