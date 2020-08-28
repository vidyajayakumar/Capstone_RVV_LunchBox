package com.vidya.lunchbox.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.adapter.CategoryAdapter;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.model.CategoryNew;
import com.vidya.lunchbox.model.ItemMenu;
import com.vidya.lunchbox.utils.CategoryNames;
import com.vidya.lunchbox.adapter.GridAdapter;
import com.vidya.lunchbox.utils.ItemClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vidya.lunchbox.utils.WebsocketStandalone;

import java.util.ArrayList;
import java.util.UUID;

public class CategoryListActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView mRecyclerView,todayDealsRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;
    ArrayList<CategoryNew> mItems;
    ArrayList<ItemMenu> dealItems;
    private StorageReference storageReference;
    ObjectMapper mapper = new ObjectMapper();

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;
    private RecyclerView.Adapter mTodaysDealAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);


        mItems = new ArrayList<>();
        dealItems = new ArrayList<ItemMenu>();
        storageReference = FirebaseStorage.getInstance().getReference();
        /***  Adding Menu Items and Categories to Database ***/
//        addCategories();

        /*
         * getting todays deal
         * */
        // Calling the RecyclerView
        getTodaysDealsItemMenus();

        todayDealsRecyclerview = (RecyclerView) findViewById(R.id.horizontal_recyclerview);
        todayDealsRecyclerview.setHasFixedSize(true);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        todayDealsRecyclerview.setLayoutManager(RecyclerViewLayoutManager);
        HorizontalLayout = new LinearLayoutManager(CategoryListActivity.this, LinearLayoutManager.HORIZONTAL, false);
        todayDealsRecyclerview.setLayoutManager(HorizontalLayout);
        mTodaysDealAdapter = new GridAdapter(this, dealItems);
        todayDealsRecyclerview.setAdapter(mTodaysDealAdapter);

        /*** Getting Menu Categories from Data ***/
        getAllCategories();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.getUserData().getEmail();
        WebsocketStandalone websocketStandalone = new WebsocketStandalone();
        websocketStandalone.connectWebSocket();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("action","androidDeviceConnect");
        jsonNode.put("userId",sessionManager.getUserData().getEmail());
        try {
            System.out.println(mapper.writeValueAsString(jsonNode));
            System.out.println("sending data");
            WebsocketStandalone.mWebSocketClient.send(mapper.writeValueAsString(jsonNode));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void getAllCategories() {

        mItems.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("categories");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    CategoryNew category = postSnapshot.getValue(CategoryNew.class);
                    mItems.add(category);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
            case R.id.action_orders:
                Intent intent = new Intent(CategoryListActivity.this, OrderListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(int position) {

       Intent intent = new Intent(CategoryListActivity.this, ItemsActivity.class);
      intent.putExtra("CategoryId", mItems.get(position).getCatId());
       startActivity(intent);
    }




    /***** Adding Menu Items and Categories to Database *****/

    private void addCategories() {
        CategoryNew category1 = new CategoryNew();
        category1.setCatName(CategoryNames.MAINCOURSE_CATEGORY);
        uploadImage(getImageUri(R.drawable.hyderabadi_chicken_biryani), false, null, category1, "");

        CategoryNew category2 = new CategoryNew();
        category2.setCatName(CategoryNames.DESERTS_CATEGORY);
        uploadImage(getImageUri(R.drawable.tomato_gazpacho_soup), false, null, category2, "");

        CategoryNew category3 = new CategoryNew();
        category3.setCatName(CategoryNames.BEVARAGES_CATEGORY);
        uploadImage(getImageUri(R.drawable.spiced_coffee), false, null, category3, "");

        CategoryNew category4 = new CategoryNew();
        category4.setCatName(CategoryNames.APETIZERS_CATEGORY);
        uploadImage(getImageUri(R.drawable.tourtiere_spring_rolls), false, null, category4, "");
    }

    private Uri getImageUri(int drawableImg) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + drawableImg);
    }

    public void createMenu(ItemMenu itemMenu, String catId) {
        DatabaseReference presentersReference = FirebaseDatabase.getInstance().getReference("itemMenus");
        final String presenterId = UUID.randomUUID().toString();
        itemMenu.setItemId(presenterId);
        itemMenu.setCatId(catId);
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

    private void updateMenuItems(String catName, String catId) {
        switch (catName){
            case CategoryNames.MAINCOURSE_CATEGORY:
                for (int i = 0; i < getResources().getStringArray(R.array.maincourse_names).length; i++) {
                    int[] myImageList = new int[]{R.drawable.ravioli, R.drawable.pepperoni_pizza, R.drawable.hyderabadi_chicken_biryani,
                            R.drawable.hamburger, R.drawable.vegan_cream_cheese_veggie};
                    ItemMenu menuItem = new ItemMenu();
                    menuItem.setItemName(getResources().getStringArray(R.array.maincourse_names)[i]);
                    menuItem.setItemDesc(getResources().getStringArray(R.array.maincourse_desc)[i]);
                    menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.maincourse_cost)[i])));
                    menuItem.setDeal(false);
                    uploadImage(getImageUri((myImageList[i])), true, menuItem, null, catId);
                }
                break;
            case CategoryNames.APETIZERS_CATEGORY:
                for (int i = 0; i < getResources().getStringArray(R.array.appetizers_names).length; i++) {
                    int[] myImageList = new int[]{R.drawable.tourtiere_spring_rolls};
                    ItemMenu menuItem = new ItemMenu();
                    menuItem.setItemName(getResources().getStringArray(R.array.appetizers_names)[i]);
                    menuItem.setItemDesc(getResources().getStringArray(R.array.appetizers_desc)[i]);
                    menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.appetizers_cost)[i])));
                    menuItem.setDeal(false);
                    uploadImage(getImageUri((myImageList[i])), true, menuItem, null, catId);
                }
                break;
            case CategoryNames.BEVARAGES_CATEGORY:
                for (int i = 0; i < getResources().getStringArray(R.array.beverages_names).length; i++) {
                    int[] myImageList = new int[]{R.drawable.spiced_coffee, R.drawable.lemon_tea, R.drawable.vanilla_sweet_cream};
                    ItemMenu menuItem = new ItemMenu();
                    menuItem.setItemName(getResources().getStringArray(R.array.beverages_names)[i]);
                    menuItem.setItemDesc(getResources().getStringArray(R.array.beverages_desc)[i]);
                    menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.beverages_cost)[i])));
                    menuItem.setDeal(false);
                    uploadImage(getImageUri((myImageList[i])), true, menuItem, null, catId);
                }
                break;
            case CategoryNames.DESERTS_CATEGORY:
                for (int i = 0; i < getResources().getStringArray(R.array.desert_names).length; i++) {
                    int[] myImageList = new int[]{R.drawable.tomato_gazpacho_soup, R.drawable.chicken_bacon_soup, R.drawable.tiger_tail_ice_cream,
                            R.drawable.saskatoon_berry_pie, R.drawable.nanaimo_bar};
                    ItemMenu menuItem = new ItemMenu();
                    menuItem.setItemName(getResources().getStringArray(R.array.desert_names)[i]);
                    menuItem.setItemDesc(getResources().getStringArray(R.array.desert_desc)[i]);
                    menuItem.setPrice(Double.parseDouble(String.valueOf(getResources().getIntArray(R.array.desert_cost)[i])));
                    menuItem.setDeal(false);
                    uploadImage(getImageUri((myImageList[i])), true, menuItem, null, catId);
                }
                break;
        }
    }

    public void createCategory(final CategoryNew category) {
        DatabaseReference presentersReference = FirebaseDatabase.getInstance().getReference("categories");
        final String presenterId = UUID.randomUUID().toString();
        category.setCatId(presenterId);
        presentersReference.child(presenterId).setValue(category, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    updateMenuItems(category.getCatName(), presenterId);
                    Log.e("TAG", "Category added : " + presenterId);
                } else {
                    Log.e("TAG", "Failed to add", databaseError.toException());
                }
            }
        });
    }

    public void uploadImage(final Uri filePath, final boolean menu, final ItemMenu menuItem,
                            final CategoryNew category, final String catId) {
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
                                createMenu(menuItem, catId);
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

    private void getTodaysDealsItemMenus() {
        dealItems.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("itemMenus");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ItemMenu menu = postSnapshot.getValue(ItemMenu.class);
                    if (menu.isDeal())
                        dealItems.add(menu);
                }
                mTodaysDealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
}