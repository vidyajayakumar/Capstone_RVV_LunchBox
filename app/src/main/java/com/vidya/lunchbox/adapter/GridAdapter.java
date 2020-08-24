package com.vidya.lunchbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.model.Items;
import com.vidya.lunchbox.view.DetailActivity;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    ArrayList<Items> mItems;

    public GridAdapter(Context context, ArrayList<Items> adapterItems) {
        super();
        mItems = adapterItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Items nature = mItems.get(i);
        viewHolder.name.setText(nature.getName());
        viewHolder.description.setText(nature.getDescription());
        viewHolder.price.setText("$" + Integer.toString(nature.getPrice()));
        //viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
        Glide.with(viewHolder.imgThumbnail.getContext())
                .load(nature.getThumbnail())
                .into(viewHolder.imgThumbnail);

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", i);
                intent.putExtra("mItems", mItems);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView name;
        public TextView description;
        public TextView price;
        public final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}