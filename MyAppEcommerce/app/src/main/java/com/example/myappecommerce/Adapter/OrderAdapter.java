package com.example.myappecommerce.Adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myappecommerce.Model.OrderItem;
import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.OrderDetailsActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderItem> orderItemList;

    public OrderAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item_layout, viewGroup,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int resource = orderItemList.get(position).getProductImage();
        String title = orderItemList.get(position).getProductTitle();
        String deliveryDate = orderItemList.get(position).getDeliveryStatus();
        int rating = orderItemList.get(position).getRating();

        viewHolder.setOrder(resource,title,deliveryDate,rating);

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;

         public ViewHolder(@NonNull final View itemView) {
             super(itemView);

             productImage = itemView.findViewById(R.id.product_image_order);
             productTitle = itemView.findViewById(R.id.product_title_order);
             orderIndicator = itemView.findViewById(R.id.order_indicator);
             deliveryStatus = itemView.findViewById(R.id.order_delived_date);
             rateNowContainer = itemView.findViewById(R.id.ratings_now_container);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent orderDetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                     itemView.getContext().startActivity(orderDetailsIntent);
                 }
             });
         }

         public void setOrder(int resource, String title, String deliveredDate, int rating){
             productImage.setImageResource(resource);
             productTitle.setText(title);
             if(deliveredDate.equals("Cancelled")){
                 orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));
             }else{
                 orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.green)));
             }
             deliveryStatus.setText(deliveredDate);

             // Rating
             setRatings(rating);
             for(int i =0; i < rateNowContainer.getChildCount(); i++){
                 final int starPosition = i;
                 rateNowContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         setRatings(starPosition);
                     }
                 });
             }
         }
         public void setRatings(int starPosition){
             for(int i = 0; i < rateNowContainer.getChildCount(); i++){
                 ImageView starBtn = (ImageView) rateNowContainer.getChildAt(i);
                 starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BFBFBF")));
                 if(i <= starPosition){
                     starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF4500")));
                 }
             }
         }
     }
}
