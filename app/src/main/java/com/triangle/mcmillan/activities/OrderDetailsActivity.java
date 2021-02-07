package com.triangle.mcmillan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.adapters.CartAdapter;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.Order;
import com.triangle.mcmillan.utilities.CartHelper;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    ArrayList<CartItem> mCartItems;
    CartAdapter mCartItemAdapter;
    RecyclerView mRecycleView;
    TextView mCartTotal;
    Order mOrder;
    boolean isEditable;

    TextView mOrderId;
    TextView mOrderDate;

    ConstraintLayout statusLayout;
    Spinner statusSpinner;
    Button statusUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initViews();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mOrder = (Order) args.getSerializable("order");
        isEditable = args.getBoolean("editable");
        mOrder.setCartItems((ArrayList<CartItem>) args.getSerializable("cartItems"));
        Log.e("es", String.valueOf(mOrder.getCartItems().get(0).getId()));
        getItems();

        if (isEditable){
            statusLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateStatus(){
        String status = statusSpinner.getSelectedItem().toString().toLowerCase();
        mOrder.setStatus(status);
        FirebaseDatabase.getInstance().getReference("orders")
                .child(mOrder.getKey())
                .child("status")
                .setValue(status);
        Snackbar.make(mRecycleView, "Status Updated Successfully", Snackbar.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    private void getItems() {
        if (mOrder !=null){
            mCartItems = mOrder.getCartItems();
            mOrderDate.setText(mOrder.getPlacedDate());
            mOrderId.setText(mOrder.getId());

            mCartTotal.setText("LKR " + mOrder.getTotal());
        }
        if (mCartItems != null) {
            startRecycleView();
        }
    }

    private void startRecycleView() {
        mCartItemAdapter = new CartAdapter(mCartItems, OrderDetailsActivity.this, mCartTotal, false);
        mRecycleView.setAdapter(mCartItemAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
    }

    private void initViews() {
        mRecycleView = findViewById(R.id.recycleviewCart);
        mCartTotal = findViewById(R.id.textCartTotal);
        mOrderId = findViewById(R.id.textOrderId);
        mOrderDate = findViewById(R.id.txtOrderDate);
        statusLayout = findViewById(R.id.containerOrderStatus);
        statusSpinner = findViewById(R.id.spinnerOrderStatus);
        statusUpdateBtn = findViewById(R.id.btnStatusUpdate);

        statusUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
    }
}