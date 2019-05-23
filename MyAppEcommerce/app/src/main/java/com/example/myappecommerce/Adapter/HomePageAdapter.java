package com.example.myappecommerce.Adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappecommerce.Model.HomePage;
import com.example.myappecommerce.Model.HorizontalProduct;
import com.example.myappecommerce.Model.Slider;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.ProductDetailsActivity;
import com.example.myappecommerce.activities.ViewAllActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {


    private List<HomePage> homePageList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition = -1;

    public HomePageAdapter(List<HomePage> homePageList) {
        this.homePageList = homePageList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (homePageList.get(i).getType()) {
            case HomePage.BANNER_SLIDER:
                List<Slider> sliders = homePageList.get(i).getSliderList();
                ((SliderBannerViewHolder) viewHolder).setSliderBannerViewPager(sliders);
                break;
            case HomePage.STRIP_AD:
                String resource = homePageList.get(i).getResource();
                String color = homePageList.get(i).getBackgroundColor();
                ((StripADBannerViewHolder) viewHolder).setTripAD(resource, color);
                break;
            case HomePage.HORIZONTAL_PRODUCT:
                String horizontalLayoutColor = homePageList.get(i).getBackgroundColor();
                String horizontalTitle = homePageList.get(i).getTitle();
                List<WishList> viewAllProductList = homePageList.get(i).getWishListList();
                List<HorizontalProduct> horizontalProductList = homePageList.get(i).getHorizontalProductList();
                ((HorizontalBannerViewHolder) viewHolder).setHorizontalProductLayout(horizontalProductList, horizontalTitle, horizontalLayoutColor, viewAllProductList);
                break;
            case HomePage.GRID_PRODUCT:
                String gridLayOutColor = homePageList.get(i).getBackgroundColor();
                String gridTitle = homePageList.get(i).getTitle();
                List<HorizontalProduct> gridViewProductList = homePageList.get(i).getHorizontalProductList();
                ((GridViewHolder) viewHolder).setGridViewProduct(gridViewProductList, gridTitle, gridLayOutColor);
                break;
            default:
                return;
        }
        if(lastPosition < i ) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = i;
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (homePageList.get(position).getType()) {
            case 0:
                return HomePage.BANNER_SLIDER;
            case 1:
                return HomePage.STRIP_AD;
            case 2:
                return HomePage.HORIZONTAL_PRODUCT;
            case 3:
                return HomePage.GRID_PRODUCT;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return homePageList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case HomePage.BANNER_SLIDER:
                View bannerView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slider_advertising_layout, viewGroup, false);
                return new SliderBannerViewHolder(bannerView);
            case HomePage.STRIP_AD:
                View stripView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_advertising_item_layout, viewGroup, false);
                return new StripADBannerViewHolder(stripView);
            case HomePage.HORIZONTAL_PRODUCT:
                View horizontalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout, viewGroup, false);
                return new HorizontalBannerViewHolder(horizontalView);
            case HomePage.GRID_PRODUCT:
                View GridView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_layout, viewGroup, false);
                return new GridViewHolder(GridView);
            default:
                return null;
        }
    }

    public class SliderBannerViewHolder extends RecyclerView.ViewHolder {
        // Slider Banner
        private ViewPager sliderBannerViewPager;
        private int currentPager = 2;
        SliderAdapter sliderAdapter;
        private Timer timer;
        private final long DELAY = 3000;
        private final long PERIOD = 3000;

        private List<Slider> aranggedList;


        public SliderBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderBannerViewPager = itemView.findViewById(R.id.slider_banner);
        }

        public void setSliderBannerViewPager(final List<Slider> sliderList) {

            if (timer != null) {
                timer.cancel();
            }
            aranggedList = new ArrayList<>();
            for (int x = 0; x < sliderList.size(); x++) {
                aranggedList.add(x, sliderList.get(x));
            }
            aranggedList.add(0, sliderList.get(sliderList.size() - 2));
            aranggedList.add(1, sliderList.get(sliderList.size() - 1));
            aranggedList.add(sliderList.get(0));
            aranggedList.add(sliderList.get(1));

            sliderAdapter = new SliderAdapter(aranggedList);
            sliderBannerViewPager.setAdapter(sliderAdapter);
            sliderBannerViewPager.setClipToPadding(false);
            sliderBannerViewPager.setPageMargin(10);

            sliderBannerViewPager.setCurrentItem(currentPager);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    currentPager = i;
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE) {
                        pageLoop(aranggedList);
                    }
                }
            };
            sliderBannerViewPager.addOnPageChangeListener(onPageChangeListener);

            startSliderBannerShow(sliderList);

            sliderBannerViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLoop(aranggedList);
                    stopSliderBanner();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startSliderBannerShow(aranggedList);
                    }
                    return false;
                }
            });
        }

        public void pageLoop(List<Slider> sliderList) {
            if (currentPager == sliderList.size() - 2) {
                currentPager = 2;
                sliderBannerViewPager.setCurrentItem(currentPager, false);
            }
            if (currentPager == 1) {
                currentPager = sliderList.size() - 3;
                sliderBannerViewPager.setCurrentItem(currentPager, false);
            }
        }

        public void startSliderBannerShow(final List<Slider> sliderList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPager >= sliderList.size()) {
                        currentPager = 1;
                    }
                    sliderBannerViewPager.setCurrentItem(currentPager++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY, PERIOD);
        }

        private void stopSliderBanner() {
            timer.cancel();
        }
    }

    public class StripADBannerViewHolder extends RecyclerView.ViewHolder {
        ImageView stripImage;
        private ConstraintLayout stripAdContainer;

        public StripADBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            stripImage = itemView.findViewById(R.id.strip_advertising_image);
            stripAdContainer = itemView.findViewById(R.id.strip_advertising_container);
        }

        public void setTripAD(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(stripImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalBannerViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout horizontalContainer;
        private TextView horizontalTitle;
        private Button btnHorizontalViewAll;
        private RecyclerView recycleHorizontal;

        List<HorizontalProduct> horizontalProductList = new ArrayList<>();
        HorizontalProductAdapter horizontalProductAdapter;

        public HorizontalBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalContainer = itemView.findViewById(R.id.horizontal_list_product_container);
            horizontalTitle = itemView.findViewById(R.id.horizontalScrollTitle);
            btnHorizontalViewAll = itemView.findViewById(R.id.btnHorizontalScroll);
            recycleHorizontal = itemView.findViewById(R.id.recyclerHorizontalScroll);
            recycleHorizontal.setRecycledViewPool(recycledViewPool);
        }

        public void setHorizontalProductLayout(List<HorizontalProduct> horizontalProductList, final String title, String color, final List<WishList> viewAllProductList) {
            horizontalContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalTitle.setText(title);

            if (horizontalProductList.size() > 8) {
                btnHorizontalViewAll.setVisibility(View.VISIBLE);
                btnHorizontalViewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishListList = viewAllProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 0);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            } else {
                btnHorizontalViewAll.setVisibility(View.INVISIBLE);
            }

            horizontalProductAdapter = new HorizontalProductAdapter(horizontalProductList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recycleHorizontal.setLayoutManager(linearLayoutManager);

            recycleHorizontal.setAdapter(horizontalProductAdapter);
            horizontalProductAdapter.notifyDataSetChanged();
        }
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout gridProductContainer;
        private TextView gridLayoutTitle;
        private Button btnGridLayoutViewAll;
        //        GridView gridViewLayout;
        private GridLayout gridProductLayout;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            gridProductContainer = itemView.findViewById(R.id.grid_product_container);
            gridLayoutTitle = itemView.findViewById(R.id.titleGridLayout);
            btnGridLayoutViewAll = itemView.findViewById(R.id.btnGridLayoutViewAll);
//            gridViewLayout = itemView.findViewById(R.id.gridViewLayout);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }

        public void setGridViewProduct(final List<HorizontalProduct> horizontalProductList, final String title, String color) {
            gridProductContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);
//            gridViewLayout.setAdapter(new GridProductLayoutAdapter(horizontalProductList));

            for (int x = 0; x < 4; x++) {
                ImageView productImage = (ImageView) gridProductLayout.getChildAt(x).findViewById(R.id.hs_item_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.hs_item_title);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.hs_item_description);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.hs_item_price);

                Glide.with(itemView.getContext()).load(horizontalProductList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(productImage);
                productTitle.setText(horizontalProductList.get(x).getProductTitle());
                productDescription.setText(horizontalProductList.get(x).getProductDescription());
                productPrice.setText(horizontalProductList.get(x).getProductPrice() + " VND");

                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));
                if (!title.equals("")) {
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
            }
            if (!title.equals("")) {
                btnGridLayoutViewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.horizontalProductList = horizontalProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 1);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            }
        }
    }

}
