package com.example.myappecommerce.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappecommerce.Model.HorizontalProduct;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.ProductDetailsActivity;

import java.util.List;

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.ViewHolder> {

    private List<HorizontalProduct> horizontalProductList;

    public HorizontalProductAdapter(List<HorizontalProduct> horizontalProductList) {
        this.horizontalProductList = horizontalProductList;
    }

    @NonNull
    @Override
    public HorizontalProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductAdapter.ViewHolder viewHolder, int i) {
        String resource = horizontalProductList.get(i).getProductImage();
        String title = horizontalProductList.get(i).getProductTitle();
        String description = horizontalProductList.get(i).getProductDescription();
        String price = horizontalProductList.get(i).getProductPrice();
        String productID = horizontalProductList.get(i).getProductID();

        viewHolder.setHorizontalProduct(productID,resource,title,description,price);
    }

    @Override
    public int getItemCount() {
        if(horizontalProductList.size() >8){
            return 8;
        }else{
            return horizontalProductList.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView horizontalScrollImage;
        private TextView horizontalScrollTitle;
        private TextView horizontalScrollDescription;
        private TextView horizontalScrollPrice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            horizontalScrollImage = itemView.findViewById(R.id.hs_item_image);
            horizontalScrollTitle = itemView.findViewById(R.id.hs_item_title);
            horizontalScrollDescription = itemView.findViewById(R.id.hs_item_description);
            horizontalScrollPrice = itemView.findViewById(R.id.hs_item_price);
        }


        private void setHorizontalProduct(final String productID, String resource, String title, String description, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(horizontalScrollImage);
            horizontalScrollTitle.setText(title);
            horizontalScrollDescription.setText(description);
            horizontalScrollPrice.setText(price+" VND");

            if(!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        productDetailsIntent.putExtra("PRODUCT_ID",productID);
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }
        }
    }
}
