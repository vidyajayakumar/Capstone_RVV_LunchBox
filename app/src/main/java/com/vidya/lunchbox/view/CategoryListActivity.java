package com.vidya.lunchbox.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.adapter.CategoryAdapter;
import com.vidya.lunchbox.model.Category;
import com.vidya.lunchbox.utils.CategoryNames;
import com.vidya.lunchbox.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryListActivity extends AppCompatActivity implements ItemClickListener {

    ArrayList<Category> mItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);
        mItems = new ArrayList<>();

        Category category = new Category();
        category.setDescription(CategoryNames.MAINCOURSE_CATEGORY);
        category.setImgId(R.drawable.hyderabadi_chicken_biryani);
        mItems.add(category);

        category = new Category();
        category.setDescription(CategoryNames.DESERTS_CATEGORY);
        category.setImgId(R.drawable.tomato_gazpacho_soup);
        mItems.add(category);

        category = new Category();
        category.setDescription(CategoryNames.BEVARAGES_CATEGORY);
        category.setImgId(R.drawable.spiced_coffee);
        mItems.add(category);

        category = new Category();
        category.setDescription(CategoryNames.APETIZERS_CATEGORY);
        category.setImgId(R.drawable.tourtiere_spring_rolls);
        mItems.add(category);

        Collections.shuffle(mItems);

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CategoryAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this); // Bind the listener
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent i = new Intent(CategoryListActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view, int position, String category) {

        Intent intent = new Intent(CategoryListActivity.this, MainActivity.class);
        intent.putExtra("CategoryName", category);
        startActivity(intent);
    }
}
