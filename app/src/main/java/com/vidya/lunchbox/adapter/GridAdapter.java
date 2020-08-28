package com.vidya.lunchbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.model.Items;
import com.vidya.lunchbox.view.DetailActivity;

import java.util.ArrayList;
import java.util.Base64;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    ArrayList<ItemMenu> mItems;
    private Context context;

    public GridAdapter(Context context, ArrayList<ItemMenu> adapterItems) {
        super();
        this.context = context;
        mItems = adapterItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ItemMenu nature = mItems.get(i);

        if (nature.isInStock())
            viewHolder.rlOutOfStock.setVisibility(View.GONE);
        else
            viewHolder.rlOutOfStock.setVisibility(View.VISIBLE);

        viewHolder.name.setText(nature.getItemName());
        viewHolder.description.setText(nature.getItemDesc());
        viewHolder.price.setText("$".concat(String.valueOf(nature.getPrice())));
        //viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());

        String pureBase64 = nature.getItemImage().split(",")[1];
        final byte[] decodedBytes = Base64.getDecoder().decode(pureBase64);

        Glide.with(context).load(decodedBytes)
                .into(viewHolder.imgThumbnail);

        viewHolder.rlOutOfStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Out of Stock", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("mItem", mItems.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public ImageView imgThumbnail;
        public TextView name;
        public TextView description;
        public TextView price;
        public RelativeLayout rlOutOfStock;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
            rlOutOfStock = (RelativeLayout) itemView.findViewById(R.id.rlOutOfStock);
        }
    }
}