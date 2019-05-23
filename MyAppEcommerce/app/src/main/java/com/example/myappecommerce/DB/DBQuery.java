package com.example.myappecommerce.DB;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappecommerce.Adapter.CategoryAdapter;
import com.example.myappecommerce.Adapter.HomePageAdapter;
import com.example.myappecommerce.Model.Address;
import com.example.myappecommerce.Model.CartItem;
import com.example.myappecommerce.Model.Category;
import com.example.myappecommerce.Model.HomePage;
import com.example.myappecommerce.Model.HorizontalProduct;
import com.example.myappecommerce.Model.Slider;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.AddAddressActivity;
import com.example.myappecommerce.activities.DeliveryActivity;
import com.example.myappecommerce.activities.ProductDetailsActivity;
import com.example.myappecommerce.fragment.CartFragment;
import com.example.myappecommerce.fragment.HomeFragment;
import com.example.myappecommerce.fragment.WishListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBQuery {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<Category> categoryList = new ArrayList<>();

    public static List<List<HomePage>> listCategoryProductLists = new ArrayList<>();
    public static List<String> loadCategoryName = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishList> wishListModeLists = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating  = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItem> cartItemsModeLists = new ArrayList<>();

    // Address
    public static int selectedAddress = -1;
    public static List<Address> addressList = new ArrayList<>();


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    categoryList.add(new Category(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                                }
                                CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
                                categoryRecyclerView.setAdapter(categoryAdapter);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void LoadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if ((long) documentSnapshot.get("view_type") == 0) {
                                List<Slider> sliderList = new ArrayList<>();
                                long no_of_banners = (long) documentSnapshot.get("no_off_banners");
                                for (long x = 1; x < no_of_banners + 1; x++) {
                                    sliderList.add(new Slider(documentSnapshot.get("banner_" + x).toString(), documentSnapshot.get("banner_" + x + "_background").toString()));
                                }
//                                homePageList.add(new HomePage(0, sliderList));
                                listCategoryProductLists.get(index).add(new HomePage(0, sliderList));
                            } else if ((long) documentSnapshot.get("view_type") == 1) {
                                listCategoryProductLists.get(index).add(new HomePage(1, documentSnapshot.get("strip_ad_banner").toString()
                                        , documentSnapshot.get("background").toString()));
                            } else if ((long) documentSnapshot.get("view_type") == 2) {

                                List<WishList> viewAllProductList = new ArrayList<>();
                                List<HorizontalProduct> horizontalProductsList = new ArrayList<>();
                                long no_of_products = (long) documentSnapshot.get("no_of_product");
                                for (long x = 1; x < no_of_products + 1; x++) {
                                    horizontalProductsList.add(new HorizontalProduct(documentSnapshot.get("product_id_" + x).toString()
                                            , documentSnapshot.get("product_image_" + x).toString().toString()
                                            , documentSnapshot.get("product_title_" + x).toString()
                                            , documentSnapshot.get("product_subtitle_" + x).toString()
                                            , documentSnapshot.get("product_price_" + x).toString()));


                                    viewAllProductList.add(new WishList(
                                            documentSnapshot.get("product_id_"+x).toString()
                                            ,documentSnapshot.get("product_image_"+x).toString()
                                            ,documentSnapshot.get("product_full_title_"+x).toString()
                                            ,(long)documentSnapshot.get("free_coupons_"+x)
                                            ,documentSnapshot.get("average_rating_"+x).toString()
                                            ,(long)documentSnapshot.get("total_rating_"+x)
                                            ,documentSnapshot.get("product_price_"+x).toString()
                                            ,documentSnapshot.get("cutted_price_"+x).toString()
                                            ,(boolean)documentSnapshot.get("COD_"+x)));
                                }
                                listCategoryProductLists.get(index).add(new HomePage(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(), horizontalProductsList,viewAllProductList));


                            } else if ((long) documentSnapshot.get("view_type") == 3) {
                                List<HorizontalProduct> GridProductsList = new ArrayList<>();
                                long no_of_products = (long) documentSnapshot.get("no_of_product");
                                for (long x = 1; x < no_of_products + 1; x++) {
                                    GridProductsList.add(new HorizontalProduct(documentSnapshot.get("product_id_" + x).toString()
                                            , documentSnapshot.get("product_image_" + x).toString().toString()
                                            , documentSnapshot.get("product_title_" + x).toString()
                                            , documentSnapshot.get("product_subtitle_" + x).toString()
                                            , documentSnapshot.get("product_price_" + x).toString()));
                                }
                                listCategoryProductLists.get(index).add(new HomePage(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(), GridProductsList));
                            }
                        }
                        HomePageAdapter homePageAdapter = new HomePageAdapter(listCategoryProductLists.get(index));
                        homePageRecyclerView.setAdapter(homePageAdapter);
                        homePageAdapter.notifyDataSetChanged();
                        HomeFragment.swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadWishList(final Context context,final Dialog dialog, final boolean loadProductData){
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for (long x = 0; x < (long)task.getResult().get("list_size"); x++){
                        wishList.add(task.getResult().get("product_id_"+x).toString());
                        if (DBQuery.wishList.contains(ProductDetailsActivity.productID)) {
                            ProductDetailsActivity.ALREADY_ADD_TO_WISH_LIST = true;
                            if(ProductDetailsActivity.btnAddToWishList != null) {
                                ProductDetailsActivity.btnAddToWishList.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                            }
                        } else {
                            if(ProductDetailsActivity.btnAddToWishList != null) {
                                ProductDetailsActivity.btnAddToWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                            ProductDetailsActivity.ALREADY_ADD_TO_WISH_LIST = false;
                        }

                        if(loadProductData) {
                            wishListModeLists.clear();
                            final String productId = task.getResult().get("product_id_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        wishListModeLists.add(new WishList(
                                                productId
                                                ,task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , (long) task.getResult().get("free_coupons")
                                                , task.getResult().get("average_rating").toString()
                                                , (long) task.getResult().get("total_rating")
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("cutted_price").toString()
                                                , (boolean) task.getResult().get("COD")));

                                        WishListFragment.wishListAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

        });
    }

    public static void removeFromWishList(final int index, final Context context){
        final String removeProductId = wishList.get(index);
        wishList.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();

        for (int x= 0; x<wishList.size(); x++){
            updateWishList.put("product_id_"+x, wishList.get(x));
        }
        updateWishList.put("list_size",(long)wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_WISHLIST").set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(wishListModeLists.size() !=0 ){
                        wishListModeLists.remove(index);
                        WishListFragment.wishListAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.ALREADY_ADD_TO_WISH_LIST = false;
                    Toast.makeText(context, "remove success", Toast.LENGTH_SHORT).show();

                }else{
                    if(ProductDetailsActivity.btnAddToWishList != null) {
                        ProductDetailsActivity.btnAddToWishList.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                    }
                    wishList.add(index,removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

                ProductDetailsActivity.running_wishList_query = false;

            }
        });
    }

    public static void loadRatingList(final Context context){
        if(!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();

            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_id_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_id_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                ProductDetailsActivity.initRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if(ProductDetailsActivity.rateNowContainer!=null) {
                                    ProductDetailsActivity.setRatings(ProductDetailsActivity.initRating);
                                }
                            }
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount){
        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for (long x = 0; x < (long)task.getResult().get("list_size"); x++){
                        cartList.add(task.getResult().get("product_id_"+x).toString());

                        if (DBQuery.cartList.contains(ProductDetailsActivity.productID)) {
                            ProductDetailsActivity.ALREADY_ADD_TO_CART_LIST = true;
                        } else {
                            ProductDetailsActivity.ALREADY_ADD_TO_CART_LIST = false;
                        }

                        if(loadProductData) {
                            cartItemsModeLists.clear();
                            final String productId = task.getResult().get("product_id_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int index  = 0;
                                        if(cartList.size() >= 2){
                                            index = cartList.size() - 2;
                                        }

                                        cartItemsModeLists.add(index ,new CartItem(
                                                CartItem.CART_ITEM
                                                ,productId
                                                ,task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , (long) task.getResult().get("free_coupons")
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("cutted_price").toString()
                                                , (long) 1
                                                , (long) 0
                                                , (long) 0
                                                , (boolean) task.getResult().get("in_stock")
                                        ));
                                        if(cartList.size() == 1){
                                            cartItemsModeLists.add(new CartItem(CartItem.TOTAL_AMOUNT));
                                            LinearLayout parentLayout = (LinearLayout) cartTotalAmount.getParent().getParent();
                                            parentLayout.setVisibility(View.VISIBLE);
                                        }
                                        if(cartList.size() == 0){
                                            cartItemsModeLists.clear();
                                        }

                                        CartFragment.cartAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if(cartList.size() != 0){
                        badgeCount.setVisibility(View.VISIBLE);
                    }else{
                        badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if(DBQuery.cartList.size()<99) {
                        badgeCount.setText(String.valueOf(DBQuery.cartList.size()));
                    }else{
                        badgeCount.setText("99");
                    }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

        });
    }

    public static void removeFromCart(final int index, final Context context, final TextView cartTotalAmount){
        final String removeProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++){
            updateCartList.put("product_id_"+x, cartList.get(x));
        }
        updateCartList.put("list_size",(long)cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_CART").set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(cartItemsModeLists.size() !=0 ){
                        cartItemsModeLists.remove(index);
                        CartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if(cartList.size() == 0){
                        LinearLayout parentLayout = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parentLayout.setVisibility(View.GONE);
                        cartItemsModeLists.clear();
                    }
                    Toast.makeText(context, "remove cart success", Toast.LENGTH_SHORT).show();
                }else{
                    cartList.add(index,removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

                ProductDetailsActivity.running_cart_query = false;

            }
        });
    }

    public static void loadAddress(final Context context, final Dialog loadingDialog){

        addressList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_ADDRESS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Intent deliveryIntent;
                    if((long)task.getResult().get("list_size") == 0){
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT","deliveryIntent");
                    }else{
                        for (long x = 1; x < (long)task.getResult().get("list_size") + 1; x++){
                            addressList.add(new Address(task.getResult().get("fullName_"+x).toString()
                            ,task.getResult().get("address_"+x).toString()
                            ,task.getResult().get("zipCode_"+x).toString()
                            ,(boolean)task.getResult().get("selected_"+x)));

                            if((boolean)task.getResult().get("selected_"+x)){
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        deliveryIntent = new Intent(context, DeliveryActivity.class);
                    }
                    context.startActivity(deliveryIntent);
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    public static void clearData(){
        categoryList.clear();
        listCategoryProductLists.clear();
        loadCategoryName.clear();
        wishList.clear();
        wishListModeLists.clear();
        cartList.clear();
        cartItemsModeLists.clear();
    }

}
