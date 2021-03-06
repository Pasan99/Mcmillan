package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.adapters.OrderItemAdapter;
import com.triangle.mcmillan.models.Order;
import com.triangle.mcmillan.models.User;
import com.triangle.mcmillan.utilities.UserHelper;

import java.util.ArrayList;

public class AllOrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Order> mOrders;
    OrderItemAdapter mOrderItemAdapter;
    RecyclerView mRecycleView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
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
        mOrders = new ArrayList<>();
        startRecycleView();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orders");
        // get items form database
        // Read from the database
        myRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Order order = snapshot.getValue(Order.class);
                    assert order != null;
                    mOrders.add(order);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mOrderItemAdapter.setFoodItems(mOrders);
                mOrderItemAdapter.notifyDataSetChanged();
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
        mOrderItemAdapter = new OrderItemAdapter(mOrders, AllOrdersActivity.this, true);
        mRecycleView.setAdapter(mOrderItemAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(AllOrdersActivity.this));
    }

    private void initViews() {
        mRecycleView = findViewById(R.id.recycleViewOrders);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getItems();
    }
}