package com.example.gogreenfyp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

public class RewardRecyclerViewAdapter extends RecyclerView.Adapter<RewardRecyclerViewAdapter.MyViewHolder> {


    private Context context;
    private List<Rewards> Data;

    public RewardRecyclerViewAdapter(Context context, List<Rewards> data) {
        this.context = context;
        Data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.reward_cardview, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tvRewardTitle.setText(Data.get(position).getName());
        holder.tvRewardPointsNeeded.setText(String.valueOf(Data.get(position).getPointsToRedeem()));
        //holder.RewardImg.setImageResource(Data.get(position).get());

        // Image
        Glide.with(context)
                .load(Data.get(position).getImageURL())
                .into(holder.RewardImg);

        holder.AllRewardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AllRewardRedeem.class);
                i.putExtra("RewardTitle", Data.get(position).getName());
                i.putExtra("RewardInstructions", Data.get(position).getInstructions());
                i.putExtra("RewardPoints", Data.get(position).getPointsToRedeem());
                i.putExtra("RewardQuantity", Data.get(position).getQuantity());
                i.putExtra("RewardQuantityLeft", Data.get(position).getQuantityLeft());
                i.putExtra("RewardTerms", Data.get(position).getTermsAndCondition());

                Log.d("TERMS", Data.get(position).getName());

//                i.putExtra("RewardImg", Data.get(position).getRewardImage());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView tvRewardTitle;
        TextView tvRewardPointsNeeded;
        ImageView RewardImg;
        CardView AllRewardCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            tvRewardPointsNeeded = (TextView) itemView.findViewById(R.id.rewardPointsToClaim);
            RewardImg = (ImageView) itemView.findViewById(R.id.rewardImg);
            AllRewardCardView = (CardView) itemView.findViewById(R.id.AllRewardCardView);
        }
    }
}
