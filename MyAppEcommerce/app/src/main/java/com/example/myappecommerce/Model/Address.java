package com.example.myappecommerce.Model;

public class Address {
    private String fullName;
    private String address;
    private String zipCode;
    private boolean selected;

    public Address(String fullName, String address, String pinCode, boolean selected) {
        this.fullName = fullName;
        this.address = address;
        this.zipCode = pinCode;
        this.selected = selected;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String pinCode) {
        this.zipCode = pinCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
