package com.vidya.lunchbox.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vidya.lunchbox.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.adapter.OrderAdapter;
import com.vidya.lunchbox.adapter.OrderProductAdapter;
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.model.Order;
import com.vidya.lunchbox.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.vidya.lunchbox.model.Order;

public class OrderDetails extends AppCompatActivity {

    private TextView orderIdVal, orderStatus, dateTime, orderTotal;
    private RecyclerView rvOrderedProducts;
    private Order order;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<ItemMenu> products;
    private OrderProductAdapter mAdapter;
    private double totalPrice = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        orderIdVal = findViewById(R.id.orderIdVal);
        orderStatus = findViewById(R.id.orderStatus);
        dateTime = findViewById(R.id.dateTime);
        orderTotal = findViewById(R.id.orderTotal);
        rvOrderedProducts = findViewById(R.id.rvOrderedProducts);
        rvOrderedProducts.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rvOrderedProducts.setLayoutManager(mLayoutManager);

        products = new ArrayList<>();
        mAdapter = new OrderProductAdapter(this, products);
        rvOrderedProducts.setAdapter(mAdapter);

        getBundleData();
        setData();
    }

    private void setData() {
        orderIdVal.setText(order.getOrderId());
        orderStatus.setText(order.getStatus());
        dateTime.setText(Utils.getDateTime(order.getDateTime()));
        getProductsList(Utils.getArrFromString(order.getProductIds()));
    }

    private void getProductsList(final HashMap<String, String> productIds) {
        products.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("itemMenus");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ItemMenu menu = postSnapshot.getValue(ItemMenu.class);
                    if (productIds.keySet().contains(menu.getItemId())) {
                        int qty = Integer.parseInt(productIds.get(menu.getItemId()));
                        totalPrice += qty * menu.getPrice();
                        menu.setQuantity(qty);
                        products.add(menu);
                    }
                }
                mAdapter.notifyDataSetChanged();
                orderTotal.setText("$".concat(String.valueOf(totalPrice)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    private void getBundleData() {
        if (getIntent().getExtras() != null) {
            order = getIntent().getExtras().getParcelable("order");
        }
    }
}