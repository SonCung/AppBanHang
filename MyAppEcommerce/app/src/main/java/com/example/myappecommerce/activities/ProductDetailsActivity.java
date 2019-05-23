package com.example.myappecommerce.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappecommerce.Adapter.ProductDetailsAdapter;
import com.example.myappecommerce.Adapter.ProductImagesAdapter;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.CartItem;
import com.example.myappecommerce.Model.ProductSpecification;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;
import com.example.myappecommerce.fragment.CartFragment;
import com.example.myappecommerce.fragment.ProductDescriptionFragment;
import com.example.myappecommerce.fragment.ProductSpecificationFragment;
import com.example.myappecommerce.fragment.SignInFragment;
import com.example.myappecommerce.fragment.SignUpFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myappecommerce.activities.MainActivity.showCart;
import static com.example.myappecommerce.activities.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {
    public static boolean running_wishList_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private TabLayout viewPagerIndicator;

    // description
    private List<ProductSpecification> productSpecificationList = new ArrayList<>();
    private ConstraintLayout productConstraintOnlyContainer;
    private ConstraintLayout productConstraintTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private TextView productOnlyDescriptionBody;
    private String productDescription;
    private String productOtherDetails;
    // Rating
    public static int initRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingProgressBarContainer;
    private TextView averageRatings;

    //Buy Now and add to Cart
    private Button btnBuyNow;
    private LinearLayout btnAddToCart;


    // wishList
    public static boolean ALREADY_ADD_TO_WISH_LIST = false;
    public static FloatingActionButton btnAddToWishList;
    public static String productID;

    // cartList
    public static boolean ALREADY_ADD_TO_CART_LIST = false;
    public static MenuItem cartItem;
    private TextView badgeCount;

    // FireBase
    FirebaseFirestore firebaseFirestore;
    FirebaseUser currentUser;
    private DocumentSnapshot documentSnapshot;


    // Dialog
    private Dialog signInDialog;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewPages);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating);
        totalRatingMiniView = findViewById(R.id.total_rating);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        viewPagerIndicator = findViewById(R.id.view_pager_indicator);
        btnAddToWishList = findViewById(R.id.add_to_wishList);

        // Description
        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tablayout);
        productConstraintOnlyContainer = findViewById(R.id.product_details_container);
        productConstraintTabsContainer = findViewById(R.id.product_details_tabs_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);

        // Ratings
        initRating = -1;
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_total_number_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRatings = findViewById(R.id.average_ratings);


        // BuyNow
        btnBuyNow = findViewById(R.id.btn_buy_now);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);

        // loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        // Fire Base
        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                        productImages.add(documentSnapshot.get("product_image_" + x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText((long) documentSnapshot.get("total_rating") + " ratings");
                    productPrice.setText(documentSnapshot.get("product_price").toString() + " VND");
                    cuttedPrice.setText(documentSnapshot.get("cutted_price").toString());

                    if ((boolean) documentSnapshot.get("use_tab_layout")) {
                        productConstraintTabsContainer.setVisibility(View.VISIBLE);
                        productConstraintOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString();
                        productOtherDetails = documentSnapshot.get("product_other_details").toString();

                        for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                            productSpecificationList.add(new ProductSpecification(0, documentSnapshot.get("spec_title_" + x).toString()));
                            for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                productSpecificationList.add(new ProductSpecification(1, documentSnapshot.get("spec_title_" + x + "_field_name_" + y).toString(), documentSnapshot.get("spec_title_" + x + "_field_value_" + y).toString()));
                            }
                        }
                    } else {
                        productConstraintTabsContainer.setVisibility(View.GONE);
                        productConstraintOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }

                    totalRatings.setText((long) documentSnapshot.get("total_rating") + " ratings");
                    averageRatings.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationList));
                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long) documentSnapshot.get(5 - x + "_star")));

                        ProgressBar progressBar = (ProgressBar) ratingProgressBarContainer.getChildAt(x);
                        int maxProgressBar = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_rating")));
                        progressBar.setMax(maxProgressBar);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get(5 - x + "_star"))));
                    }
                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_rating")));

                    // WishList
                    if (currentUser != null) {
                        if (DBQuery.myRating.size() == 0) {
                            DBQuery.loadRatingList(ProductDetailsActivity.this);
                        }
                        if (DBQuery.cartList.size() == 0) {
                            DBQuery.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailsActivity.this));
                        }
                        if (DBQuery.wishList.size() == 0) {
                            DBQuery.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }
                    if (DBQuery.myRatedIds.contains(productID)) {
                        int index = DBQuery.myRatedIds.indexOf(productID);
                        initRating = Integer.parseInt(String.valueOf(DBQuery.myRating.get(index))) - 1;
                        setRatings(initRating);
                    }
                    if (DBQuery.cartList.contains(productID)) {
                        ALREADY_ADD_TO_CART_LIST = true;
                    } else {
                        ALREADY_ADD_TO_CART_LIST = false;
                    }
                    if (DBQuery.wishList.contains(productID)) {
                        ALREADY_ADD_TO_WISH_LIST = true;
                        btnAddToWishList.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                    } else {
                        btnAddToWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        ALREADY_ADD_TO_WISH_LIST = false;
                    }

                    if ((boolean) documentSnapshot.get("in_stock")) {
                        btnAddToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currentUser == null) {
                                    signInDialog.show();
                                } else {
                                    // to do add to cart
                                    if (!running_cart_query) {
                                        running_cart_query = true;
                                        if (ALREADY_ADD_TO_CART_LIST) {
                                            running_cart_query = false;
                                            Toast.makeText(ProductDetailsActivity.this, "already add to cart", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, Object> addproductID = new HashMap<>();
                                            addproductID.put("product_id_" + String.valueOf(DBQuery.cartList.size()), productID);
                                            addproductID.put("list_size", (long) (DBQuery.cartList.size() + 1));

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                                    .document("MY_CART")
                                                    .update(addproductID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBQuery.cartItemsModeLists.size() != 0) {
                                                            DBQuery.cartItemsModeLists.add(0,new CartItem(
                                                                    CartItem.CART_ITEM
                                                                    , productID
                                                                    , documentSnapshot.get("product_image_1").toString()
                                                                    , documentSnapshot.get("product_title").toString()
                                                                    , (long) documentSnapshot.get("free_coupons")
                                                                    , documentSnapshot.get("product_price").toString()
                                                                    , documentSnapshot.get("cutted_price").toString()
                                                                    , (long) 1
                                                                    , (long) 0
                                                                    , (long) 0
                                                                    , (boolean) documentSnapshot.get("in_stock")
                                                            ));
                                                        }
                                                        ALREADY_ADD_TO_CART_LIST = true;
                                                        DBQuery.cartList.add(productID);
                                                        Toast.makeText(ProductDetailsActivity.this, "Add cart success!", Toast.LENGTH_SHORT).show();
                                                        invalidateOptionsMenu();
                                                        running_cart_query = false;
                                                    } else {
                                                        running_cart_query = false;
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                }
                            }
                        });
                    } else {
                        btnBuyNow.setVisibility(View.GONE);
                        TextView outOfStock = (TextView) btnAddToCart.getChildAt(0);
                        outOfStock.setText("Hết hàng");
                        outOfStock.setTextColor(getResources().getColor(R.color.red));
                        outOfStock.setCompoundDrawables(null, null, null, null);
                    }

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);

        btnAddToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {

                    if (!running_wishList_query) {
                        running_wishList_query = true;
                        if (ALREADY_ADD_TO_WISH_LIST) {
                            int index = DBQuery.wishList.indexOf(productID);
                            DBQuery.removeFromWishList(index, ProductDetailsActivity.this);
                            btnAddToWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            btnAddToWishList.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            Map<String, Object> addproductID = new HashMap<>();
                            addproductID.put("product_id_" + String.valueOf(DBQuery.wishList.size()), productID);
                            addproductID.put("list_size", (long) (DBQuery.wishList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                    .document("MY_WISHLIST")
                                    .update(addproductID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBQuery.wishListModeLists.size() != 0) {
                                            DBQuery.wishListModeLists.add(
                                                    new WishList(productID, documentSnapshot.get("product_image_1").toString()
                                                            , documentSnapshot.get("product_title").toString()
                                                            , (long) documentSnapshot.get("free_coupons")
                                                            , documentSnapshot.get("average_rating").toString()
                                                            , (long) documentSnapshot.get("total_rating")
                                                            , documentSnapshot.get("product_price").toString()
                                                            , documentSnapshot.get("cutted_price").toString()
                                                            , (boolean) documentSnapshot.get("COD")));
                                        }
                                        ALREADY_ADD_TO_WISH_LIST = true;
                                        btnAddToWishList.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                        DBQuery.wishList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Add wishlist success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        btnAddToWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishList_query = false;
                                }
                            });
                        }
                    }
                }
            }
        });

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Rating
        rateNowContainer = findViewById(R.id.ratings_now_container);
        for (int i = 0; i < rateNowContainer.getChildCount(); i++) {
            final int starPosition = i;
            rateNowContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starPosition != initRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;

                                setRatings(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBQuery.myRatedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculatorAverageRating((long) starPosition - initRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculatorAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_rating", (long) documentSnapshot.get("total_rating") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBQuery.myRatedIds.contains(productID)) {
                                                myRating.put("rating_" + DBQuery.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                            } else {

                                                myRating.put("list_size", (long) DBQuery.myRatedIds.size() + 1);
                                                myRating.put("product_id_" + DBQuery.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBQuery.myRatedIds.size(), (long) starPosition + 1);
                                            }
                                            firebaseFirestore.collection("USERS")
                                                    .document(currentUser.getUid())
                                                    .collection("USER_DATA")
                                                    .document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBQuery.myRatedIds.contains(productID)) {

                                                            DBQuery.myRating.set(DBQuery.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initRating - 1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                        } else {
                                                            DBQuery.myRatedIds.add(productID);
                                                            DBQuery.myRating.add((long) starPosition + 1);
                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));


                                                            totalRatingMiniView.setText(((long) documentSnapshot.get("total_rating") + 1) + " ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_rating") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_rating") + 1));

                                                            Toast.makeText(ProductDetailsActivity.this, "rating success", Toast.LENGTH_SHORT).show();
                                                        }
                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);
                                                            ProgressBar progressBar = (ProgressBar) ratingProgressBarContainer.getChildAt(x);
                                                            int maxProgressBar = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgressBar);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                        }
                                                        initRating = starPosition;
                                                        averageRatings.setText(calculatorAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculatorAverageRating(0, true));

                                                        if (DBQuery.wishList.contains(productID) && DBQuery.wishListModeLists.size() != 0) {
                                                            int index = DBQuery.wishList.indexOf(productID);
                                                            DBQuery.wishListModeLists.get(index).setRating(averageRatings.getText().toString());
                                                            DBQuery.wishListModeLists.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));

                                                        }
                                                    } else {
                                                        setRatings(initRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });
                                        } else {
                                            running_rating_query = false;
                                            setRatings(initRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }

        // Buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.cartItemsModeLists = new ArrayList<>();
                    DeliveryActivity.cartItemsModeLists.add(new CartItem(
                            CartItem.CART_ITEM
                            , productID
                            , documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupons")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) 0
                            , (long) 0
                            , (boolean) documentSnapshot.get("in_stock")
                    ));
                    DeliveryActivity.cartItemsModeLists.add(new CartItem(CartItem.TOTAL_AMOUNT));

                    if(DBQuery.addressList.size() == 0){
                        DBQuery.loadAddress(ProductDetailsActivity.this, loadingDialog);
                    }else{
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });


        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btnSignIn = signInDialog.findViewById(R.id.btn_SignIn);
        Button btnSignUp = signInDialog.findViewById(R.id.btn_SignUp);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (DBQuery.myRating.size() == 0) {
                DBQuery.loadRatingList(ProductDetailsActivity.this);
            }

            if (DBQuery.wishList.size() == 0) {
                DBQuery.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                DBQuery.loadRatingList(ProductDetailsActivity.this);
            } else {
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }

        if (DBQuery.myRatedIds.contains(productID)) {
            int index = DBQuery.myRatedIds.indexOf(productID);
            initRating = Integer.parseInt(String.valueOf(DBQuery.myRating.get(index))) - 1;
            setRatings(initRating);
        }
        if (DBQuery.cartList.contains(productID)) {
            ALREADY_ADD_TO_CART_LIST = true;
        } else {
            ALREADY_ADD_TO_CART_LIST = false;
        }
        if (DBQuery.wishList.contains(productID)) {
            ALREADY_ADD_TO_WISH_LIST = true;
            btnAddToWishList.setSupportImageTintList(getResources().getColorStateList(R.color.red));
        } else {
            btnAddToWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADD_TO_WISH_LIST = false;
        }
        invalidateOptionsMenu();
    }

    public static void setRatings(int starPosition) {
        for (int i = 0; i < rateNowContainer.getChildCount(); i++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(i);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BFBFBF")));
            if (i <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF4500")));
            }
        }
    }

    private String calculatorAverageRating(long currentUserRating, boolean update) {
        Double totalStar = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStar = totalStar + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStar = totalStar + currentUserRating;
        if (update) {
            return String.valueOf(totalStar / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStar / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart, menu);

        cartItem = menu.findItem(R.id.main_cart_icon);
        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.ic_cart);
        badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

        if (currentUser != null) {
            if (DBQuery.cartList.size() == 0) {
                DBQuery.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount,new TextView(ProductDetailsActivity.this));
            } else {
                badgeCount.setVisibility(View.VISIBLE);
                if (DBQuery.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBQuery.cartList.size()));
                } else {
                    badgeCount.setText("99");
                }
            }
        }
        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartIntent);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            // search
            return true;
        } else if (id == R.id.main_cart_icon) {
            // cart
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
