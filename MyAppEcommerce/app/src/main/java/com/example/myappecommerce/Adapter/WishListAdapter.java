package com.example.myappecommerce.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.ProductDetailsActivity;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private List<WishList> wishListList;
    private boolean wishList;
    private int lastPosition =-1;

    public WishListAdapter(List<WishList> wishListList, boolean wishList) {
        this.wishListList = wishListList;
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String productId = wishListList.get(position).getProductId();
        String resource = wishListList.get(position).getProductImage();
        String title = wishListList.get(position).getProductTitle();
        long freeCoupon = wishListList.get(position).getFreeCoupons();
        String rating = wishListList.get(position).getRating();
        long totalRatings = wishListList.get(position).getTotalRatings();
        String productPrice = wishListList.get(position).getProductPrice();
        String cuttedPrice = wishListList.get(position).getCuttedPrice();
        boolean payment = wishListList.get(position).isCOD();

        viewHolder.setWishList(productId,resource,title,freeCoupon,rating,totalRatings,productPrice,cuttedPrice,payment,position);

        if(lastPosition < position ) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage ;
        private TextView productTitle;
        private TextView freeCoupon;
        private ImageView couponIcon;
        private TextView rating;
        private TextView  totalRatings;
        private View priceCut;
        private TextView  productPrice;
        private TextView  cuttedPrice;
        private TextView paymentMethod;
        private ImageView btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupon = itemView.findViewById(R.id.free_coupon);
            couponIcon = itemView.findViewById(R.id.coupon_icon);
            rating = itemView.findViewById(R.id.tv_product_rating);
            totalRatings = itemView.findViewById(R.id.total_ratings);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }

        public void setWishList(final String productId, String resource, String title, long freeCouponNo, String ratingNo, long totalRatingsNo, String price, String cuttedPriceValue, boolean COD, final int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions()).placeholder(R.drawable.ic_placeholder).into(productImage);
            productTitle.setText(title);
            if(freeCouponNo !=0 ){
                couponIcon.setVisibility(View.INVISIBLE);
                if(freeCouponNo == 1){
                    freeCoupon.setText("free" + freeCouponNo+ " coupon");
                }else{
                    freeCoupon.setText("free " + freeCouponNo+ " coupons");
                }
            }else{
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupon.setVisibility(View.INVISIBLE);
            }
            rating.setText(ratingNo);
            totalRatings.setText(totalRatingsNo+" ratings");
            productPrice.setText(price);
            cuttedPrice.setText(cuttedPriceValue);
            if(COD){
                paymentMethod.setVisibility(View.VISIBLE);
            }else{
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if(wishList){
                btnDelete.setVisibility(View.VISIBLE);
            }else{
                btnDelete.setVisibility(View.GONE);
            }
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.running_wishList_query) {
                        ProductDetailsActivity.running_wishList_query = true;
                        DBQuery.removeFromWishList(index, itemView.getContext());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });

        }
    }
}
