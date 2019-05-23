package com.example.myappecommerce.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myappecommerce.Model.Rewards;
import com.example.myappecommerce.R;

import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {
    private List<Rewards> rewardsList;

    public RewardsAdapter(List<Rewards> rewardsList) {
        this.rewardsList = rewardsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String title = rewardsList.get(position).getTitle();
        String date = rewardsList.get(position).getCouponDate();
        String body = rewardsList.get(position).getCouponBody();

        viewHolder.setRewards(title,date,body);
    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView couponTitle;
        private TextView couponDate;
        private TextView couponBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.coupon_title);
            couponDate = itemView.findViewById(R.id.coupon_date);
            couponBody = itemView.findViewById(R.id.coupon_body);
        }

        public void setRewards(String title, String date, String body){
            couponTitle.setText(title);
            couponDate.setText(date);
            couponBody.setText(body);
        }

    }
}
