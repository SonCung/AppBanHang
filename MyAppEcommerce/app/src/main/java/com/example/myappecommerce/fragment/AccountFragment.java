package com.example.myappecommerce.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.AddressActivity;
import com.example.myappecommerce.activities.DeliveryActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    public static final int MANAGER_ADDRESS = 1;

    private Button btnViewAllAddress;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        btnViewAllAddress = view.findViewById(R.id.btn_address_view_all);

        btnViewAllAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(getContext(), AddressActivity.class);
                addressIntent.putExtra("MODE",MANAGER_ADDRESS);
                startActivity(addressIntent);
            }
        });

        return view;
    }

}
