package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.adapters.CartAdapter;
import com.triangle.mcmillan.adapters.FoodItemAdapter;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.models.User;
import com.triangle.mcmillan.utilities.CartHelper;
import com.triangle.mcmillan.utilities.UserHelper;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ArrayList<CartItem> mCartItems;
    CartAdapter mCartItemAdapter;
    RecyclerView mRecycleView;
    TextView mCartTotal;
    Button mPlaceOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        getItems();
    }

    @SuppressLint("SetTextI18n")
    private void getItems() {
        mCartItems = CartHelper.cartItems;
        if (mCartItems != null) {
            startRecycleView();
        }

        mCartTotal.setText("LKR " + CartHelper.getCartTotal());
    }

    private void startRecycleView() {
        mCartItemAdapter = new CartAdapter(mCartItems, CartActivity.this, mCartTotal, true);
        mRecycleView.setAdapter(mCartItemAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
    }

    private void initViews() {
        mRecycleView = findViewById(R.id.recycleviewCart);
        mCartTotal = findViewById(R.id.textCartTotal);
        mPlaceOrderBtn = findViewById(R.id.btnOrderNow);

        mPlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                if (mCartItems != null && mCartItems.size() > 0) {
                    CartHelper.placeOrder();
                    Toast.makeText(CartActivity.this, "Order Placed Successfully", Toast.LENGTH_LONG);
                    startActivity(new Intent(CartActivity.this, MyOrdersActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(CartActivity.this, "No items in the cart", Toast.LENGTH_LONG);
                }
            }
        });
    }
}