package com.triangle.mcmillan.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int quantity;
    private String id;
    private String name;
    private String description;
    private Long price;
    private String image;
    private boolean isInCart = false;

    public CartItem() {
    }

    public CartItem(int quantity, String id, String name, String description, Long price, String image, boolean isInCart) {
        this.quantity = quantity;
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.isInCart = isInCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
