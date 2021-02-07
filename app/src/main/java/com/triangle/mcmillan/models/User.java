package com.triangle.mcmillan.models;

import java.util.ArrayList;

public class User {
    private String name;
    private String profilePic = "https://frfars.org/wp-content/uploads/2018/12/place-holder-for-profile-picture-4.png";
    private String id;
    private Address address;
    private ArrayList<CartItem> cart;
    private String role;

    public User(){

    }

    public User(String name, String profilePic, String id, Address address, ArrayList<CartItem> cart, String role) {
        this.name = name;
        this.profilePic = profilePic;
        this.id = id;
        this.address = address;
        this.cart = cart;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    public class UserRole{
        public static final String CUSTOMER = "customer";
        public static final String MANAGER = "manager";
    }
}
