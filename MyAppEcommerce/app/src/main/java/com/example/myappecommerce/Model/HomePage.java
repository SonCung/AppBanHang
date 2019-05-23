package com.example.myappecommerce.Model;

import java.util.List;

public class HomePage {

    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD = 1;
    public static final int HORIZONTAL_PRODUCT = 2;
    public static final int GRID_PRODUCT = 3;

    private int type;
    private String backgroundColor;

    private List<Slider> sliderList;

    public HomePage(int type, List<Slider> sliderList) {
        this.type = type;
        this.sliderList = sliderList;
    }

    // Banner
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Slider> getSliderList() {
        return sliderList;
    }

    public void setSliderList(List<Slider> sliderList) {
        this.sliderList = sliderList;
    }

    // Strip
    public String resource;

    public HomePage(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    // Horizontal Product AND GRID VIEW Product
    private String title;
    private List<HorizontalProduct> horizontalProductList;
    private List<WishList> wishListList;

    public HomePage(int type, String title,String backgroundColor ,List<HorizontalProduct> horizontalProductList, List<WishList> wishListList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductList = horizontalProductList;
        this.wishListList =wishListList;
    }

    public HomePage(int type, String title,String backgroundColor ,List<HorizontalProduct> horizontalProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductList = horizontalProductList;
    }

    public List<WishList> getWishListList() {
        return wishListList;
    }

    public void setWishListList(List<WishList> wishListList) {
        this.wishListList = wishListList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProduct> getHorizontalProductList() {
        return horizontalProductList;
    }

    public void setHorizontalProductList(List<HorizontalProduct> horizontalProductList) {
        this.horizontalProductList = horizontalProductList;
    }
}
