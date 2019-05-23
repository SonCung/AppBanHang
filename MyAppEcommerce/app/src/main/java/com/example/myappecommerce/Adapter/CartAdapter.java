package com.example.myappecommerce.Adapter;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.CartItem;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.ProductDetailsActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItem> cartItemList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean btnShowDelete;

    public CartAdapter(List<CartItem> cartItemList, TextView cartTotalAmount, boolean btnShowDelete) {
        this.cartItemList = cartItemList;
        this.cartTotalAmount = cartTotalAmount;
        this.btnShowDelete = btnShowDelete;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemList.get(position).getType()) {
            case 0:
                return CartItem.CART_ITEM;
            case 1:
                return CartItem.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItem.CART_ITEM:
                View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new cartItemViewHolder(viewItem);
            case CartItem.TOTAL_AMOUNT:
                View viewTotal = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new cartTotalAmountViewHolder(viewTotal);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemList.get(position).getType()) {
            case CartItem.CART_ITEM:
                String productId = cartItemList.get(position).getProductID();
                String resource = cartItemList.get(position).getProductImage();
                String title = cartItemList.get(position).getProductTitle();
                Long freeCoupons = cartItemList.get(position).getFreeCoupons();
                String productPrice = cartItemList.get(position).getProductPrice();
                String cuttedPrice = cartItemList.get(position).getCuttedPrice();
                Long offerApplied = cartItemList.get(position).getOffersApplied();
                Long couponsApplied = cartItemList.get(position).getCouponsApplied();
                boolean inStock = cartItemList.get(position).isInStock();

                ((cartItemViewHolder) viewHolder).setItemDetails(productId,resource,title,freeCoupons,productPrice,cuttedPrice,offerApplied,couponsApplied, position, inStock);
                break;
            case CartItem.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for (int x = 0; x < cartItemList.size(); x++){
                    if(cartItemList.get(x).getType() == CartItem.CART_ITEM && cartItemList.get(x).isInStock()){
                        totalItems++;
                        totalItemPrice = totalItemPrice + Integer.parseInt(cartItemList.get(x).getProductPrice());
                    }
                }
                if(totalItemPrice > 200){
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                }else{
                    deliveryPrice = "5";
                    totalAmount = totalItemPrice + 5;
                }
                ((cartTotalAmountViewHolder) viewHolder).setTotalAmount(totalItems,totalItemPrice , deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;
        }
        if(lastPosition < position ) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    class cartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private ImageView freeCouponsIcon;
        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView couponsApplied;
        private TextView productQuantity;
        private LinearLayout couponRedemptionLayout;

        private LinearLayout btnDeleteCart;


        public cartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            freeCouponsIcon = itemView.findViewById(R.id.free_coupen_icon);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupen_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            couponRedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);

            btnDeleteCart = itemView.findViewById(R.id.btn_remove_item_cart);
        }

        public void setItemDetails(String productId, String resource, String title, Long freeCouponsNo, String productPriceText, String cuttedPriceText,
                                   Long offersAppliedNo, Long couponsAppliedNo, final int position, boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(productImage);
            productTitle.setText(title);

            if(inStock) {
                if (freeCouponsNo > 0) {
                    freeCouponsIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {
                        freeCoupons.setText("free" + freeCouponsNo + " Coupon");
                    } else {
                        freeCoupons.setText("free" + freeCouponsNo + " Coupons");
                    }
                } else {
                    freeCouponsIcon.setVisibility(View.INVISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);
                }

                productPrice.setText(productPriceText+" $");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText(cuttedPriceText+" $");
                couponRedemptionLayout.setVisibility(View.VISIBLE);
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button btnCancel = quantityDialog.findViewById(R.id.btn_cancel);
                        Button btnOk = quantityDialog.findViewById(R.id.btn_Ok);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productQuantity.setText("Qty: "+quantityNo.getText());
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });
                if (couponsAppliedNo > 0) {
                    couponsApplied.setVisibility(View.VISIBLE);
                    couponsApplied.setText(couponsAppliedNo + "Coupons applied");
                } else {
                    couponsApplied.setVisibility(View.INVISIBLE);
                }
                if (offersAppliedNo > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText(offersAppliedNo + "Offers applied");
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            }else{
                productPrice.setText("hết hàng");
                productPrice.setTextColor(itemView.getResources().getColor(R.color.red));
                cuttedPrice.setText("");
                couponRedemptionLayout.setVisibility(View.GONE);
                freeCoupons.setVisibility(View.INVISIBLE);
                productQuantity.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freeCouponsIcon.setVisibility(View.INVISIBLE);
            }

            if(btnShowDelete){
                btnDeleteCart.setVisibility(View.VISIBLE);
            }else{
                btnDeleteCart.setVisibility(View.GONE);
            }
            btnDeleteCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query = true;
                        DBQuery.removeFromCart(position, itemView.getContext(),cartTotalAmount);
                    }
                }
            });
        }
    }


    class cartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalItems;
        private TextView totalItemsPrice;
        private TextView deliveryItems;
        private TextView totalAmount;
        private TextView saveAmount;

        public cartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemsPrice = itemView.findViewById(R.id.total_item_price);
            deliveryItems = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price_details);
            saveAmount = itemView.findViewById(R.id.saved_amount);

        }

            public void setTotalAmount(int totalItemText, int totalItemsPriceText, String deliveryItemsText, int totalAmountText ,int saveAmountText) {
            totalItems.setText("Price("+totalItemText+" items)");
            totalItemsPrice.setText(totalItemsPriceText+" $");
            if(deliveryItems.equals("FREE")){
                deliveryItems.setText(deliveryItemsText+" $");
            }else{
                deliveryItems.setText(deliveryItemsText+" $");
            }
            totalAmount.setText(totalAmountText+" $");
            cartTotalAmount.setText(totalAmountText+"");
            saveAmount.setText("You save "+saveAmountText+" $ on this order.");

            LinearLayout parentLayout = (LinearLayout) cartTotalAmount.getParent().getParent();
            if(totalItemsPriceText == 0){
                DBQuery.cartItemsModeLists.remove(DBQuery.cartItemsModeLists.size()-1);
                parentLayout.setVisibility(View.GONE);
            }else{
                parentLayout.setVisibility(View.VISIBLE);
            }

        }
    }
}
