package com.example.myappecommerce.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myappecommerce.Model.Slider;
import com.example.myappecommerce.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<Slider> sliderList;

    public SliderAdapter(List<Slider> sliderList) {
        this.sliderList = sliderList;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_item_layout,container,false);
        ConstraintLayout sliderBannerContainer = view.findViewById(R.id.slider_banner_container);
        try {
            sliderBannerContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderList.get(position).getBackgroundColor())));
        }catch (Exception e){
            e.printStackTrace();
        }
        ImageView banner = view.findViewById(R.id.slider_banner);
        Glide.with(container.getContext()).load(sliderList.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(banner);
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
