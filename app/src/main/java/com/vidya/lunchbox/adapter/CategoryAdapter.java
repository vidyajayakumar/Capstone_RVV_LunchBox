package com.vidya.lunchbox.adapter;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.model.Category;
import com.vidya.lunchbox.model.CategoryNew;
import com.vidya.lunchbox.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.Base64;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context mcontext;
    private ArrayList<CategoryNew> listdata;
    private ItemClickListener clickListener;

    // RecyclerView recyclerView;
    public CategoryAdapter(Context activity, ArrayList<CategoryNew> listdata) {
        this.listdata = listdata;
        mcontext = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CategoryNew myListData = listdata.get(position);
        holder.textView.setText(myListData.getCatName());
        String pureBase64 = myListData.getCatImage().split(",")[1];
        final byte[] decodedBytes = Base64.getDecoder().decode(pureBase64);
        Glide.with(mcontext).load(decodedBytes).into(holder.imageView);
        holder.description.setVisibility(View.GONE);
        holder.price.setVisibility(View.GONE);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null)
                    clickListener.onClick(position);
            }
        });
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView, description, price;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_item);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            this.textView = (TextView) itemView.findViewById(R.id.name);
            this.description = (TextView) itemView.findViewById(R.id.description);
            this.price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}