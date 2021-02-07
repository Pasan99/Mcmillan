package com.triangle.mcmillan.utilities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.Order;
import com.triangle.mcmillan.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CartHelper {
    public static ArrayList<CartItem> cartItems = new ArrayList<>();

    public static void changeQuantity(CartItem item, boolean isNew){
        if (cartItems != null && cartItems.size() > 0){
            if (!isNew){
                int index = cartItems.indexOf(item);
                cartItems.remove(item);
                if (item.getQuantity() > 0) {
                    cartItems.add(index, item);
                }
            }
            else{
                cartItems.add(item);
            }
        }
        else{
            cartItems = new ArrayList<>();
            cartItems.add(item);
        }
        // update cart
        FirebaseDatabase.getInstance().getReference("users").
                child(UserHelper.CurrentUser.getId())
                .child("cart")
                .setValue(cartItems);
    }

    public static long getCartTotal(){
        long total = 0;
        if (cartItems != null && cartItems.size() > 0){
            for (CartItem item : cartItems) {
                total += (item.getPrice() * item.getQuantity());
            }
        }
        return total;
    }

    public static void placeOrder(){
        Order order = new Order();
        order.setCartItems(cartItems);
        Random r = new Random();
        int id =  r.nextInt(1000000000);
        order.setId(String.valueOf(id));
        order.setUserId(UserHelper.CurrentUser.getId());
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        order.setPlacedDate(formatter.format(date));
        order.setTotal(CartHelper.getCartTotal());
        order.setStatus(Order.OrderStatus.SUBMITTED);
        // Create a new post reference with an auto-generated id
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        DatabaseReference newOrder = ref.push();
        order.setKey(newOrder.getKey());
        newOrder.setValue(order);

        cartItems = new ArrayList<>();
        // update cart
        FirebaseDatabase.getInstance().getReference("users").
                child(UserHelper.CurrentUser.getId())
                .child("cart")
                .setValue(cartItems);

    }

}
