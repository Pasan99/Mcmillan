package com.triangle.mcmillan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.activities.OrderDetailsActivity;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.models.Order;
import com.triangle.mcmillan.utilities.CartHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private View mMainView;
    private ArrayList<Order> orders;
    private Context context;
    private boolean isEditable;

    public void setFoodItems(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public OrderItemAdapter(ArrayList<Order> orders, Context context, boolean isEditable) {
        this.orders = orders;
        this.context = context;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mMainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new OrderItemAdapter.ViewHolder(mMainView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        final Order order = orders.get(position);
        holder.id.setText(order.getId());
        holder.date.setText(order.getPlacedDate());
        holder.status.setText(order.getStatus().toUpperCase());
        holder.amount.setText("LKR " + order.getTotal().toString());

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cartItems",(Serializable) order.getCartItems());
                bundle.putSerializable("order", order);
                bundle.putBoolean("editable", isEditable);
                intent.putExtra("BUNDLE", bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView id;
        private TextView date;
        private TextView amount;
        private TextView status;
        private Button viewBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.orderItemId);
            date = itemView.findViewById(R.id.orderItemDate);
            amount = itemView.findViewById(R.id.orderItemPrice);
            status = itemView.findViewById(R.id.orderItemStatus);
            viewBtn = itemView.findViewById(R.id.orderItemViewBtn);
        }
    }
}
