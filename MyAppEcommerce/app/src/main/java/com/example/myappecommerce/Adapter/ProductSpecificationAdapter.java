package com.example.myappecommerce.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myappecommerce.Model.ProductSpecification;
import com.example.myappecommerce.R;

import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecification> productSpecificationsList;

    public ProductSpecificationAdapter(List<ProductSpecification> productSpecificationsList) {
        this.productSpecificationsList = productSpecificationsList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationsList.get(position).getType()) {
            case 0:
                return ProductSpecification.SPECIFICATION_TITLE;
            case 1:
                return ProductSpecification.SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ProductSpecification.SPECIFICATION_TITLE:
                TextView title = new TextView(viewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, viewGroup.getContext()),
                        setDp(16, viewGroup.getContext()),
                        setDp(16, viewGroup.getContext()),
                        setDp(8, viewGroup.getContext()));
                title.setLayoutParams(layoutParams);

                return new ViewHolder(title);
            case ProductSpecification.SPECIFICATION_BODY:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_specification_item_layout, viewGroup, false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder viewHolder, int i) {

        switch (productSpecificationsList.get(i).getType()) {
            case ProductSpecification.SPECIFICATION_TITLE:
                viewHolder.setTitle(productSpecificationsList.get(i).getTitle());
                break;
            case ProductSpecification.SPECIFICATION_BODY:
                String featureTitle = productSpecificationsList.get(i).getFeatureName();
                String featureDetail = productSpecificationsList.get(i).getFeatureValue();
                viewHolder.setFeatures(featureTitle, featureDetail);
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return productSpecificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureValue;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        private void setTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void setFeatures(String featureTitle, String featureDetail) {
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName.setText(featureTitle);
            featureValue.setText(featureDetail);

        }
    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
