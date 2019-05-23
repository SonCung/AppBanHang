package com.example.myappecommerce.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myappecommerce.Adapter.OrderAdapter;
import com.example.myappecommerce.Model.OrderItem;
import com.example.myappecommerce.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    private RecyclerView ordersRecyclerView;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);

        ordersRecyclerView = view.findViewById(R.id.order_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(new OrderItem(R.drawable.iphone,"iphone 13(black)","Delivery on Mon, 15th April 2019",2));
        orderItemList.add(new OrderItem(R.drawable.iphone,"iphone 15(red)","Cancelled",4));

        OrderAdapter orderAdapter = new OrderAdapter(orderItemList);
        ordersRecyclerView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();

        return view;
    }

}
