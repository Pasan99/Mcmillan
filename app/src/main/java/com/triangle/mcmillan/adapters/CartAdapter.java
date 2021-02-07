package com.triangle.mcmillan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.utilities.CartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private View mMainView;
    private ArrayList<CartItem> cartItems;
    private Context context;
    private TextView txtTotal;
    private boolean isCartView = true;

    public void setCartItems(ArrayList<CartItem> foodItems) {
        this.cartItems = foodItems;
    }

    public CartAdapter(ArrayList<CartItem> foodItems, Context context, TextView txtTotal, boolean isCartView) {
        this.cartItems = foodItems;
        this.context = context;
        this.txtTotal = txtTotal;
        this.isCartView = isCartView;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mMainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartAdapter.ViewHolder(mMainView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        final CartItem cartItem = cartItems.get(position);
        holder.name.setText(cartItem.getName());
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.price.setText("LKR " + (cartItem.getPrice() * cartItem.getQuantity()));
        holder.actualPrice.setText("(" + cartItem.getPrice() + " x " + cartItem.getQuantity() + ")");
        Glide
                .with(context)
                .load(cartItem.getImage())
                .centerCrop()
                .placeholder(R.drawable.gif_loading_image)
                .into(holder.image);

        if(isCartView) {
            // add item to cart
            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    CartHelper.changeQuantity(cartItem, false);
                    notifyDataSetChanged();
                    txtTotal.setText("LKR " + CartHelper.getCartTotal());
                }
            });
            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    if (cartItem.getQuantity() == 0) {
                        cartItems.remove(cartItem);
                    }
                    CartHelper.changeQuantity(cartItem, false);
                    notifyDataSetChanged();
                    txtTotal.setText("LKR " + CartHelper.getCartTotal());
                }
            });
        }
        else{
            holder.addBtn.setVisibility(View.GONE);
            holder.removeBtn.setVisibility(View.GONE);
            holder.quantity.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView quantity;
        private TextView price;
        private ImageView image;
        private ImageButton addBtn;
        private ImageButton removeBtn;
        private TextView actualPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cartItemName);
            quantity = itemView.findViewById(R.id.cartItemQuantity);
            price = itemView.findViewById(R.id.cartItemPrice);
            image = itemView.findViewById(R.id.cartItemImage);
            addBtn = itemView.findViewById(R.id.cartItemAddBtn);
            removeBtn = itemView.findViewById(R.id.cartItemRemoveBtn);
            actualPrice = itemView.findViewById(R.id.cartItemActualPrice);
        }
    }
}
