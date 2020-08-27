package com.vidya.lunchbox.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vidya.lunchbox.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.vidya.lunchbox.model.Order;

public class OrderDetails extends AppCompatActivity {

    private TextView orderIdVal, orderStatus, dateTime;
    private RecyclerView rvOrderedProducts;
    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        orderIdVal = findViewById(R.id.orderIdVal);
        orderStatus = findViewById(R.id.orderStatus);
        dateTime = findViewById(R.id.dateTime);
        rvOrderedProducts = findViewById(R.id.rvOrderedProducts);

        getBundleData();
    }

    private void getBundleData() {
        if (getIntent().getExtras() != null) {
            order = getIntent().getExtras().getParcelable("order");
        }
    }
}