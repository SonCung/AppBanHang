package com.example.myappecommerce.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myappecommerce.Adapter.WishListAdapter;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class WishListFragment extends Fragment {
    private RecyclerView wishListRecyclerView;
    private Dialog loadingDialog;
    public static WishListAdapter wishListAdapter;


    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wish_list, container, false);
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        wishListRecyclerView = view.findViewById(R.id.wishlist_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishListRecyclerView.setLayoutManager(linearLayoutManager);

        if(DBQuery.wishListModeLists.size() == 0){
            DBQuery.wishList.clear();
            DBQuery.loadWishList(getContext(),loadingDialog, true);
        }else{
            loadingDialog.dismiss();
        }

        wishListAdapter = new WishListAdapter(DBQuery.wishListModeLists,true);
        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();
        return view;
    }

}
