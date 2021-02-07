package com.triangle.mcmillan.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private String id;
    private ArrayList<CartItem> cartItems;
    private Long total;
    private String status;
    private String placedDate;
    private String userId;
    private String key;

    public Order() {
    }

    public Order(String id, ArrayList<CartItem> cartItems, Long total, String status, String placedDate, String userId, String key) {
        this.cartItems = cartItems;
        this.total = total;
        this.status = status;
        this.placedDate = placedDate;
        this.id = id;
        this.userId = userId;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(String placedDate) {
        this.placedDate = placedDate;
    }

    public class OrderStatus{
        public static final String SUBMITTED = "submitted";
        public static final String ACCEPTED = "accepted";
        public static final String READY = "ready";
    }
}
