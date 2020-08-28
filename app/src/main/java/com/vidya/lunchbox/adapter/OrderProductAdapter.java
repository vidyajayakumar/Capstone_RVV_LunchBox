package com.vidya.lunchbox.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.cart.Carteasy;
import com.vidya.lunchbox.model.Cart;
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.utils.DeleteClickListener;

import java.util.ArrayList;
import java.util.Base64;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ItemMenu ct = mItems.get(i);

        viewHolder.productName.setText(ct.getItemName());
        viewHolder.productDesc.setText(ct.getItemDesc());
        viewHolder.productQty.setText(String.valueOf(ct.getQuantity()));
        viewHolder.productPrice.setText("$".concat(String.valueOf(ct.getPrice())));

        String pureBase64 = ct.getItemImage().split(",")[1];
        final byte[] decodedBytes = Base64.getDecoder().decode(pureBase64);
        Glide.with(mContext).load(decodedBytes).into(viewHolder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgProduct;
        public TextView productName, productDesc, productQty, productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDesc = (TextView) itemView.findViewById(R.id.productDesc);
            productQty = (TextView) itemView.findViewById(R.id.productQty);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
        }
    }
}
