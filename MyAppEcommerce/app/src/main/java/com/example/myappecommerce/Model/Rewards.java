package com.example.myappecommerce.Model;

public class Rewards {
    private String title;
    private String couponDate;
    private String couponBody;

    public Rewards(String title, String couponDate, String couponBody) {
        this.title = title;
        this.couponDate = couponDate;
        this.couponBody = couponBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCouponDate() {
        return couponDate;
    }

    public void setCouponDate(String couponDate) {
        this.couponDate = couponDate;
    }

    public String getCouponBody() {
        return couponBody;
    }

    public void setCouponBody(String couponBody) {
        this.couponBody = couponBody;
    }
}
