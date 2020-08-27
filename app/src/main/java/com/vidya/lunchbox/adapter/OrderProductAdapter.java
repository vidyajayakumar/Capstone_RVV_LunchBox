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
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.utils.DeleteClickListener;

import java.util.ArrayList;


public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {

    ArrayList<ItemMenu> mItems;
    Context mContext;

    public OrderProductAdapter(Context context, ArrayList<ItemMenu> item) {
        mContext = context;
        mItems = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ItemMenu ct = mItems.get(i);

        viewHolder.productName.setText(ct.getItemName());
        viewHolder.productDesc.setText(ct.getItemDesc());
        viewHolder.productPrice.setText("$".concat(String.valueOf(ct.getPrice())));

        Glide.with(mContext).load(ct.getItemImage()).into(viewHolder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgProduct;
        public TextView productName, productDesc, productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDesc = (TextView) itemView.findViewById(R.id.productDesc);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            imgProduct =  (ImageView) itemView.findViewById(R.id.imgProduct);
        }
    }
}
