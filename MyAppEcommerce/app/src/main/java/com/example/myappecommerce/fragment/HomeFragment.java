package com.example.myappecommerce.fragment;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myappecommerce.Adapter.CategoryAdapter;
import com.example.myappecommerce.Adapter.GridProductLayoutAdapter;
import com.example.myappecommerce.Adapter.HomePageAdapter;
import com.example.myappecommerce.Adapter.HorizontalProductAdapter;
import com.example.myappecommerce.Adapter.SliderAdapter;
import com.example.myappecommerce.Model.Category;
import com.example.myappecommerce.Model.HomePage;
import com.example.myappecommerce.Model.HorizontalProduct;
import com.example.myappecommerce.Model.Slider;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.myappecommerce.DB.*;

import static com.example.myappecommerce.DB.DBQuery.LoadFragmentData;
import static com.example.myappecommerce.DB.DBQuery.categoryList;
import static com.example.myappecommerce.DB.DBQuery.firebaseFirestore;
import static com.example.myappecommerce.DB.DBQuery.*;
import static com.example.myappecommerce.DB.DBQuery.listCategoryProductLists;
import static com.example.myappecommerce.DB.DBQuery.loadCategories;
import static com.example.myappecommerce.DB.DBQuery.loadCategoryName;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    // Category
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryListsFake = new ArrayList<>();
    // Fragment
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter homePageAdapter;
    private List<HomePage> homePageListsFake = new ArrayList<>();
    // Check Internet
    private ImageView noInternerConnections;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    // Refresh Layout
    public static SwipeRefreshLayout swipeRefreshLayout;
    // Retry
    private Button btnRetry;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //RefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        // Retry
        btnRetry = view.findViewById(R.id.btn_retry);

        // LoadCategory from FireBase
        categoryRecyclerView = view.findViewById(R.id.category_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        //  LoadFragment from FireBase
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager1 = new LinearLayoutManager(getContext());
        testingLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager1);

        // category fake list
        categoryListsFake.add(new Category("null", ""));
        categoryListsFake.add(new Category("", ""));
        categoryListsFake.add(new Category("", ""));
        categoryListsFake.add(new Category("", ""));
        categoryListsFake.add(new Category("", ""));
        categoryListsFake.add(new Category("", ""));

        // homepage fake list
        List<Slider> sliderListFake = new ArrayList<>();
        sliderListFake.add(new Slider("null", "#cfcfcf"));
        sliderListFake.add(new Slider("null", "#cfcfcf"));
        sliderListFake.add(new Slider("null", "#cfcfcf"));

        List<HorizontalProduct> horizontalProductListFake = new ArrayList<>();
        horizontalProductListFake.add(new HorizontalProduct("", "", "", "", ""));
        horizontalProductListFake.add(new HorizontalProduct("", "", "", "", ""));
        horizontalProductListFake.add(new HorizontalProduct("", "", "", "", ""));
        horizontalProductListFake.add(new HorizontalProduct("", "", "", "", ""));

        homePageListsFake.add(new HomePage(0, sliderListFake));
        homePageListsFake.add(new HomePage(1, "", "#cfcfcf"));
        homePageListsFake.add(new HomePage(2, "", "#cfcfcf", horizontalProductListFake, new ArrayList<WishList>()));
        homePageListsFake.add(new HomePage(3, "", "#cfcfcf", horizontalProductListFake));

        categoryAdapter = new CategoryAdapter(categoryListsFake);
        homePageAdapter = new HomePageAdapter(homePageListsFake);


        // Check internet
        noInternerConnections = view.findViewById(R.id.no_internet_connection);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInternerConnections.setVisibility(View.GONE);
            btnRetry.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if (categoryList.size() == 0) {
                loadCategories(categoryRecyclerView, getContext());
            } else {
                categoryAdapter = new CategoryAdapter(categoryList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);
            if (listCategoryProductLists.size() == 0) {
                loadCategoryName.add("HOME");
                listCategoryProductLists.add(new ArrayList<HomePage>());

                LoadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
            } else {
                homePageAdapter = new HomePageAdapter(listCategoryProductLists.get(0));
                homePageAdapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(homePageAdapter);
        } else {
            MainActivity.drawer.setDrawerLockMode(1);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            btnRetry.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.no_interner_connection).into(noInternerConnections);
            noInternerConnections.setVisibility(View.VISIBLE);
        }
        //RefreshLayout

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));
                swipeRefreshLayout.setRefreshing(true);
                reloadHomePage();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadHomePage();
            }
        });
        return view;
    }

    public void reloadHomePage() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        categoryList.clear();
        listCategoryProductLists.clear();
        loadCategoryName.clear();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInternerConnections.setVisibility(View.GONE);
            btnRetry.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryListsFake);
            homePageAdapter = new HomePageAdapter(homePageListsFake);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(homePageAdapter);

            loadCategories(categoryRecyclerView, getContext());

            loadCategoryName.add("HOME");
            listCategoryProductLists.add(new ArrayList<HomePage>());
            LoadFragmentData(homePageRecyclerView, getContext(), 0, "Home");


        } else {
            MainActivity.drawer.setDrawerLockMode(1);
            Toast.makeText(getContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            btnRetry.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.no_interner_connection).into(noInternerConnections);
            noInternerConnections.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
