package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.adapters.FoodItemAdapter;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.models.User;
import com.triangle.mcmillan.utilities.UserHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<FoodItem> mFoodItems;
    FoodItemAdapter mFoodItemAdapter;
    RecyclerView mRecycleView;
    ImageButton mCartBtn;
    Button mLogoutBtn;
    Button mMyOrdersBtn;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (UserHelper.CurrentUser == null){
            UserHelper.loadCurrentUser();
        }
        initViews();
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                getItems();
            }
        });
    }

    private void getItems() {
        mFoodItems = new ArrayList<>();
        startRecycleView();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items");
        // get items form database
        // Read from the database
        myRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    assert foodItem != null;
                    if (UserHelper.CurrentUser != null){
                        User currentUser = UserHelper.CurrentUser;
                        if (currentUser.getCart() != null) {
                            for(CartItem item : currentUser.getCart()) {
                                if (item.getId().equals(foodItem.getId())) {
                                    foodItem.setInCart(true);
                                    break;
                                }
                            }
                        }
                    }

                    mFoodItems.add(foodItem);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mFoodItemAdapter.setFoodItems(mFoodItems);
                mFoodItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startRecycleView() {
        mFoodItemAdapter = new FoodItemAdapter(mFoodItems, MainActivity.this, false);
        mRecycleView.setAdapter(mFoodItemAdapter);
        mRecycleView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
    }

    private void initViews() {
        mRecycleView = findViewById(R.id.recycleViewHome);
        mCartBtn = findViewById(R.id.imgBtnCart);
        mLogoutBtn = findViewById(R.id.btnLogout);
        mMyOrdersBtn = findViewById(R.id.btnMyOrders);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        mMyOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyOrdersActivity.class));
            }
        });
    }

    @Override
    public void onRefresh() {
        getItems();
    }
}