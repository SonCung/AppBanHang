package com.example.myappecommerce.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myappecommerce.Model.ProductSpecification;
import com.example.myappecommerce.fragment.ProductDescriptionFragment;
import com.example.myappecommerce.fragment.ProductSpecificationFragment;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecification> productSpecificationList;

    public ProductDetailsAdapter(FragmentManager fm, int totalTab, String productDescription, String productOtherDetails, List<ProductSpecification> productSpecificationList) {
        super(fm);
        this.totalTabs = totalTab;
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationList = productSpecificationList;
    }


    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductDescriptionFragment productDescriptionFragment = new ProductDescriptionFragment();
                productDescriptionFragment.body = productDescription;
                return productDescriptionFragment;
            case 1:
                ProductSpecificationFragment productSpecificationFragment = new ProductSpecificationFragment();
                productSpecificationFragment.productSpecificationList = productSpecificationList;
                return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                productDescriptionFragment1.body = productOtherDetails;
                return productDescriptionFragment1;

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
