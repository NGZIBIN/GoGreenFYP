package com.example.gogreenfyp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RedeemedRewardRecycleViewAdapter extends RecyclerView.Adapter<RedeemedRewardRecycleViewAdapter.MyViewHolder> {


    private Context context;
    private List<Rewards> Data;

    public RedeemedRewardRecycleViewAdapter(Context context, List<Rewards> data) {
        this.context = context;
        Data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.reward_your_redeemed_cardview, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvRewardTitle.setText(Data.get(position).getName());
        Log.d("REWARD DATE", String.valueOf(Data.get(position).getUseByDate()));




        // Image
        Glide.with(context)
                .load(Data.get(position).getImageURL())
                .into(holder.RewardImg);

        //Log.d("IMAGE", Data.get(position).getImageURL());

        holder.YourRewardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Use_reward.class);
                i.putExtra("RewardTitle", Data.get(position).getName());
                i.putExtra("RewardInstructions", Data.get(position).getInstructions());
                i.putExtra("RewardPoints", Data.get(position).getPointsToRedeem());
                i.putExtra("RewardQuantity", Data.get(position).getQuantity());
                i.putExtra("RewardQuantityLeft", Data.get(position).getQuantityLeft());
                i.putExtra("RewardTerms", Data.get(position).getTermsAndConditions());
                i.putExtra("RewardImg", Data.get(position).getImageURL());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRewardTitle;
        ImageView RewardImg;
        CardView YourRewardCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            RewardImg = (ImageView) itemView.findViewById(R.id.rewardImg);
            YourRewardCardView = (CardView) itemView.findViewById(R.id.AllRewardCardView);
        }
    }
}

