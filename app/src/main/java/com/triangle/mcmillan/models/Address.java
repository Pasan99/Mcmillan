package com.triangle.mcmillan.models;

public class Address {
    private String city;
    private String contactNo;
    private String street;
    private Long zipCode;

    public Address(){

    }

    public Address(String city, String contactNo, String street, Long zipCode) {
        this.city = city;
        this.contactNo = contactNo;
        this.street = street;
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }
}
