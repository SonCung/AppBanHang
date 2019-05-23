package com.example.myappecommerce.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myappecommerce.Adapter.CartAdapter;
import com.example.myappecommerce.Adapter.WishListAdapter;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.CartItem;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.AddAddressActivity;
import com.example.myappecommerce.activities.DeliveryActivity;
import com.example.myappecommerce.activities.MainActivity;
import com.example.myappecommerce.activities.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private RecyclerView cartItemsRecyclerView;
    private Button btnContinues;

    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;

    private TextView totalCartAmount;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);
        // loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        cartItemsRecyclerView = view.findViewById(R.id.cart_item_recycleView);
        btnContinues = view.findViewById(R.id.btn_cart_continue);
        // cart amount
        totalCartAmount = view.findViewById(R.id.total_cart_amount);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        if(DBQuery.cartItemsModeLists.size() == 0){
            DBQuery.cartList.clear();
            DBQuery.loadCartList(getContext(),loadingDialog, true, new TextView(getContext()),totalCartAmount);
        }else{
            if(DBQuery.cartItemsModeLists.get(DBQuery.cartItemsModeLists.size()-1).getType() == CartItem.TOTAL_AMOUNT){
                LinearLayout parentLayout = (LinearLayout) totalCartAmount.getParent().getParent();
                parentLayout.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }

        cartAdapter = new CartAdapter(DBQuery.cartItemsModeLists, totalCartAmount, true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        btnContinues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartItemsModeLists = new ArrayList<>();
                for(int x = 0; x <DBQuery.cartItemsModeLists.size(); x++){
                    CartItem cartItem = DBQuery.cartItemsModeLists.get(x);
                    if(cartItem.isInStock()){
                        DeliveryActivity.cartItemsModeLists.add(cartItem);
                    }
                }

                DeliveryActivity.cartItemsModeLists.add(new CartItem(CartItem.TOTAL_AMOUNT));
                loadingDialog.show();
                if(DBQuery.addressList.size() == 0){
                    DBQuery.loadAddress(getContext(), loadingDialog);
                }else{
                    loadingDialog.dismiss();
                    Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }

            }
        });

        return view;
    }

}
