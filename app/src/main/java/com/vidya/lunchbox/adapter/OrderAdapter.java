package com.vidya.lunchbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.cart.Carteasy;
import com.vidya.lunchbox.model.Cart;
import com.vidya.lunchbox.model.Order;
import com.vidya.lunchbox.utils.DeleteClickListener;
import com.vidya.lunchbox.utils.ItemClickListener;
import com.vidya.lunchbox.utils.Utils;

import java.util.ArrayList;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<Order> orders;
    private ItemClickListener itemClickListener;

    public OrderAdapter(ArrayList<Order> orders, ItemClickListener itemClickListener) {
        this.orders = orders;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Order order = orders.get(i);
        viewHolder.orderIdVal.setText(order.getOrderId());
        viewHolder.orderStatus.setText(order.getStatus());
        viewHolder.dateTime.setText(Utils.getDateTime(order.getDateTime()));

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(i);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public TextView orderIdVal, orderStatus, dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            orderIdVal = (TextView) itemView.findViewById(R.id.orderIdVal);
            orderStatus = (TextView) itemView.findViewById(R.id.orderStatus);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);
        }
    }
}
