package com.example.myappecommerce.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myappecommerce.Adapter.CategoryAdapter;
import com.example.myappecommerce.Adapter.HomePageAdapter;
import com.example.myappecommerce.Adapter.HorizontalProductAdapter;
import com.example.myappecommerce.Model.Category;
import com.example.myappecommerce.Model.HomePage;
import com.example.myappecommerce.Model.HorizontalProduct;
import com.example.myappecommerce.Model.Slider;
import com.example.myappecommerce.Model.WishList;
import com.example.myappecommerce.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.myappecommerce.DB.DBQuery.LoadFragmentData;
import static com.example.myappecommerce.DB.DBQuery.listCategoryProductLists;
import static com.example.myappecommerce.DB.DBQuery.loadCategoryName;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    HomePageAdapter homePageAdapter;
    private List<HomePage> homePageListsFake = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // home page fake list
        List<Slider> sliderListFake = new ArrayList<>();
        sliderListFake.add(new Slider("null", "#ffffff"));
        sliderListFake.add(new Slider("null", "#ffffff"));
        sliderListFake.add(new Slider("null", "#ffffff"));

        List<HorizontalProduct> horizontalProductListFake = new ArrayList<>();
        horizontalProductListFake.add(new HorizontalProduct("","","","",""));
        horizontalProductListFake.add(new HorizontalProduct("","","","",""));
        horizontalProductListFake.add(new HorizontalProduct("","","","",""));
        horizontalProductListFake.add(new HorizontalProduct("","","","",""));

        homePageListsFake.add(new HomePage(0,sliderListFake));
        homePageListsFake.add(new HomePage(1,"","#ffffff"));
        homePageListsFake.add(new HomePage(2,"","#ffffff", horizontalProductListFake, new ArrayList<WishList>() ));
        homePageListsFake.add(new HomePage(3,"","#ffffff",horizontalProductListFake));
        // home page fake list

        categoryRecyclerView = (RecyclerView) findViewById(R.id.category_recycler);
        LinearLayoutManager multiLayoutManager = new LinearLayoutManager(this);
        multiLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(multiLayoutManager);

        homePageAdapter = new HomePageAdapter(homePageListsFake);
        categoryRecyclerView.setAdapter(homePageAdapter);

        int listPositions = 0;
        for (int i=0 ; i < loadCategoryName.size(); i++){
            if(loadCategoryName.get(i).equals(title.toUpperCase())){
                listPositions = i;

            }
        }
        if(listPositions == 0){
            loadCategoryName.add(title.toUpperCase());
            listCategoryProductLists.add(new ArrayList<HomePage>());

            LoadFragmentData(categoryRecyclerView, this,loadCategoryName.size()-1,title);
        }else{
            homePageAdapter = new HomePageAdapter(listCategoryProductLists.get(listPositions));
        }
        homePageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_search_icon) {
            // search
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
