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
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.utils.CategoryNames;
import com.vidya.lunchbox.model.CategoryNew;
import com.vidya.lunchbox.utils.ItemClickListener;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CategoryListActivity extends AppCompatActivity implements ItemClickListener {

    ArrayList<Category> mItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        mItems = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();
//        addCategories();
//        updateMenuItems();

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

//        Collections.shuffle(mItems);

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

    private void addCategories() {
        CategoryNew category1 = new CategoryNew();
        category1.setCatName(CategoryNames.MAINCOURSE_CATEGORY);
        uploadImage(getImageUri(R.drawable.hyderabadi_chicken_biryani), false, null, category1);

        CategoryNew category2 = new CategoryNew();
        category2.setCatName(CategoryNames.DESERTS_CATEGORY);
        uploadImage(getImageUri(R.drawable.tomato_gazpacho_soup), false, null, category2);

        CategoryNew category3 = new CategoryNew();
        category3.setCatName(CategoryNames.BEVARAGES_CATEGORY);
        uploadImage(getImageUri(R.drawable.spiced_coffee), false, null, category3);

        CategoryNew category4 = new CategoryNew();
        category4.setCatName(CategoryNames.APETIZERS_CATEGORY);
        uploadImage(getImageUri(R.drawable.tourtiere_spring_rolls), false, null, category4);
    }

    private Uri getImageUri(int drawableImg) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + drawableImg);
    }

    public void createMenu(ItemMenu itemMenu) {
        DatabaseReference presentersReference = FirebaseDatabase.getInstance().getReference("menuItems");
        final String presenterId = UUID.randomUUID().toString();
        itemMenu.setItemId(presenterId);
        presentersReference.child(presenterId).setValue(itemMenu, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.e("TAG", "Menu Item added : " + presenterId);
                } else {
                    Log.e("TAG", "Failed to add", databaseError.toException());
                }
            }
        });
    }

    private void updateMenuItems() {
        for (int i = 0; i < getResources().getStringArray(R.array.maincourse_names).length; i++) {
            ItemMenu menuItem = new ItemMenu();
            menuItem.setItemName(getResources().getStringArray(R.array.maincourse_names)[i]);
            menuItem.setItemDesc(getResources().getStringArray(R.array.maincourse_desc)[i]);
            menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.maincourse_cost)[i])));
            uploadImage(getImageUri((getResources().obtainTypedArray(R.array.maincourse_imgs).getResourceId(i, -1))),
                    true, menuItem, null);
        }
        for (int i = 0; i < getResources().getStringArray(R.array.appetizers_names).length; i++) {
            ItemMenu menuItem = new ItemMenu();
            menuItem.setItemName(getResources().getStringArray(R.array.appetizers_names)[i]);
            menuItem.setItemDesc(getResources().getStringArray(R.array.appetizers_desc)[i]);
            menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.appetizers_cost)[i])));
            uploadImage(getImageUri((getResources().obtainTypedArray(R.array.maincourse_imgs).getResourceId(i, -1))),
                    true, menuItem, null);
        }
        for (int i = 0; i < getResources().getStringArray(R.array.desert_names).length; i++) {
            ItemMenu menuItem = new ItemMenu();
            menuItem.setItemName(getResources().getStringArray(R.array.desert_names)[i]);
            menuItem.setItemDesc(getResources().getStringArray(R.array.desert_desc)[i]);
            menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.desert_cost)[i])));
            uploadImage(getImageUri((getResources().obtainTypedArray(R.array.maincourse_imgs).getResourceId(i, -1))),
                    true, menuItem, null);
        }
        for (int i = 0; i < getResources().getStringArray(R.array.beverages_names).length; i++) {
            ItemMenu menuItem = new ItemMenu();
            menuItem.setItemName(getResources().getStringArray(R.array.beverages_names)[i]);
            menuItem.setItemDesc(getResources().getStringArray(R.array.beverages_desc)[i]);
            menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.beverages_cost)[i])));
            uploadImage(getImageUri((getResources().obtainTypedArray(R.array.maincourse_imgs).getResourceId(i, -1))),
                    true, menuItem, null);
        }
    }

    public void createCategory(CategoryNew category) {
        DatabaseReference presentersReference = FirebaseDatabase.getInstance().getReference("categories");
        final String presenterId = UUID.randomUUID().toString();
        category.setCatId(presenterId);
        presentersReference.child(presenterId).setValue(category, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.e("TAG", "Category added : " + presenterId);
                } else {
                    Log.e("TAG", "Failed to add", databaseError.toException());
                }
            }
        });
    }

    public void uploadImage(final Uri filePath, final boolean menu, final ItemMenu menuItem,
                            final CategoryNew category) {
        if (filePath != null) {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Uploading....");
            progress.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("uri", "" + uri);
                            if (menu) {
                                menuItem.setItemImage(uri.toString());
                                createMenu(menuItem);
                            } else {
                                category.setCatImage(uri.toString());
                                createCategory(category);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(CategoryListActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progres_time = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progress.setMessage("Uploaded " + (int) progres_time + " %");
                }
            });
        }
    }
}