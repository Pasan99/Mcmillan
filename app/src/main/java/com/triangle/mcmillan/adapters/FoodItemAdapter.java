package com.triangle.mcmillan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.triangle.mcmillan.activities.EditFoodItemActivity;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.utilities.CartHelper;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    private View mMainView;
    private ArrayList<FoodItem> foodItems;
    private Context context;
    private boolean isAdmin;

    public void setFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public FoodItemAdapter(ArrayList<FoodItem> foodItems, Context context, boolean isAdmin) {
        this.foodItems = foodItems;
        this.context = context;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mMainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);

        return new FoodItemAdapter.ViewHolder(mMainView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        final FoodItem foodItem = foodItems.get(position);
        holder.name.setText(foodItem.getName());
        holder.description.setText(foodItem.getDescription());
        holder.price.setText("LKR " + foodItem.getPrice().toString());
        Glide
                .with(context)
                .load(foodItem.getImage())
                .centerCrop()
                .placeholder(R.drawable.gif_loading_image)
                .into(holder.image);

        if (!isAdmin) {
            if (foodItem.isInCart()) {
                holder.addToCartBtn.setVisibility(View.GONE);
                holder.itemInCart.setVisibility(View.VISIBLE);
            } else {
                holder.itemInCart.setVisibility(View.GONE);
                holder.addToCartBtn.setVisibility(View.VISIBLE);
            }

            if (holder.addToCartBtn != null) {
                holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        foodItem.setInCart(true);
                        CartItem item = new CartItem();
                        item.setName(foodItem.getName());
                        item.setId(foodItem.getId());
                        item.setImage(foodItem.getImage());
                        item.setQuantity(1);
                        item.setPrice(foodItem.getPrice());
                        CartHelper.changeQuantity(item, true);
                        notifyDataSetChanged();
                    }
                });
            }
        }
        else{
            holder.addToCartBtn.setText("Edit");
            holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditFoodItemActivity.class);
                    intent.putExtra("item", foodItem);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private TextView price;
        private ImageView image;
        private Button addToCartBtn;
        private TextView itemInCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodItemName);
            description = itemView.findViewById(R.id.foodItemDescription);
            price = itemView.findViewById(R.id.foodItemPrice);
            image = itemView.findViewById(R.id.foodItemImage);
            addToCartBtn = itemView.findViewById(R.id.foodItemAddToCart);
            itemInCart = itemView.findViewById(R.id.foodItemItemCount);
        }
    }
}
