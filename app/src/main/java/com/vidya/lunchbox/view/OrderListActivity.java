package com.vidya.lunchbox.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.adapter.CategoryAdapter;
import com.vidya.lunchbox.adapter.OrderAdapter;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.model.CategoryNew;
import com.vidya.lunchbox.model.Order;
import com.vidya.lunchbox.utils.ItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OrderAdapter mAdapter;
    private ArrayList<Order> orders;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        sessionManager = new SessionManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        orders = new ArrayList<>();
        mAdapter = new OrderAdapter(orders, this);
        mRecyclerView.setAdapter(mAdapter);

        getOrderList();
    }

    private void getOrderList() {
        orders.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("orders");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    if (sessionManager.getUserData().getEmail().equals(order.getUserId()))
                        orders.add(order);
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
    public void onClick(int position) {

    }
}