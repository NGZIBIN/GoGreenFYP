package com.example.gogreenfyp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YourRewardRecyclerViewAdapter extends RecyclerView.Adapter<YourRewardRecyclerViewAdapter.MyViewHolder> implements Filterable {


    private Context context;
    private List<Rewards> Data;
    List<Rewards> allData;
    public YourRewardRecyclerViewAdapter(Context context, List<Rewards> data) {
        this.context = context;
        Data = data;
        allData = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.reward_your_reward_cardview, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Date fStoreDate = Data.get(position).getUseByDate();
        SimpleDateFormat spf = new SimpleDateFormat("F/MMM/yyyy");
        final String rewardDate = spf.format(fStoreDate);

        holder.tvRewardTitle.setText(Data.get(position).getName());
        holder.tvExpiryDate.setText(rewardDate);
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
                i.putExtra("RewardUseByDate", rewardDate);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Rewards> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(allData);
            }else{
                String filter = charSequence.toString().toLowerCase().trim();
                for (Rewards item : allData){
                    if(item.getName().toLowerCase().contains(filter)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Data.clear();
            Data.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRewardTitle, tvExpiryDate;
        ImageView RewardImg;
        CardView YourRewardCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            tvExpiryDate = (TextView) itemView.findViewById(R.id.expiryDate);
            RewardImg = (ImageView) itemView.findViewById(R.id.rewardImg);
            YourRewardCardView = (CardView) itemView.findViewById(R.id.AllRewardCardView);
        }
    }
}
