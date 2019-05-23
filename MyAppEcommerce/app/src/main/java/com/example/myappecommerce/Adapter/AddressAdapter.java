package com.example.myappecommerce.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.Address;
import com.example.myappecommerce.R;

import java.util.List;

import static com.example.myappecommerce.activities.AddressActivity.refreshItemAddress;
import static com.example.myappecommerce.activities.DeliveryActivity.SELECT_ADDRESS;
import static com.example.myappecommerce.fragment.AccountFragment.MANAGER_ADDRESS;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<Address> addressList;
    private int MODE_ADDRESS;
    private int preSelectedPosition;

    public AddressAdapter(List<Address> addressList, int MODE_ADDRESS) {
        this.addressList = addressList;
        this.MODE_ADDRESS = MODE_ADDRESS;
        preSelectedPosition = DBQuery.selectedAddress;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_item_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder viewHolder, int position) {
        String name  = addressList.get(position).getFullName();
        String address = addressList.get(position).getAddress();
        String pincode = addressList.get(position).getZipCode();
        Boolean selected = addressList.get(position).isSelected();

        viewHolder.setAddressData(name,address,pincode,selected,position);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fullName;
        private TextView address;
        private TextView pinCode;
        private ImageView icon;
        private LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pinCode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private void setAddressData(String username, String useraddress, String userpinCode, boolean selected, final int position){
            fullName.setText(username);
            address.setText(useraddress);
            pinCode.setText(userpinCode);

            if(MODE_ADDRESS == SELECT_ADDRESS){
                icon.setImageResource(R.drawable.ic_check_white_24dp);
                if(selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                }else{
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(preSelectedPosition != position){
                            addressList.get(position).setSelected(true);
                            addressList.get(preSelectedPosition).setSelected(false);
                            refreshItemAddress(preSelectedPosition,position);
                            preSelectedPosition = position;
                            DBQuery.selectedAddress = position;
                        }

                    }
                });

            }else if(MODE_ADDRESS == MANAGER_ADDRESS){
                optionContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.ic_menu_vertical_dot);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        refreshItemAddress(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItemAddress(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
