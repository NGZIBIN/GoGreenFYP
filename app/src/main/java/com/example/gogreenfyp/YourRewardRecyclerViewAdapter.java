package com.example.gogreenfyp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Date fStoreDate = Data.get(position).getUseByDate();
        Date currDate = Calendar.getInstance().getTime();
        SimpleDateFormat spf = new SimpleDateFormat("d/MMM/yyyy");
        String rewardDate = spf.format(fStoreDate);
        Log.d("Reward firestore Date", fStoreDate.toString());
        holder.tvRewardTitle.setText(Data.get(position).getName());
        holder.tvExpiryDate.setText(rewardDate);
        Log.d("Reward Date", rewardDate);



        dateDiff diff = new dateDiff();
        long days = diff.daysBetween(currDate, fStoreDate);
        Log.d("Days diff", String.valueOf(days));
        int negDay = (int) - days;
        Log.d("Negative day", String.valueOf(negDay));

        if(currDate.after(fStoreDate)){
            holder.tvExpireSoon.setText("Reward Expired");
        }

        if(days > 1 && days <= 7){
            holder.tvExpireSoon.setText("Expiring in " + (days + 1) + " days!");
        }
        else if(days == 0){
            holder.tvExpireSoon.setText("Expiring Today!");
        }




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
                    if(fStoreDate.after(currDate) || days == 0) {
                        context.startActivity(i);
                    }else{
                        Toast.makeText(context, "Reward Expired", Toast.LENGTH_SHORT).show();
                    }
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
        TextView tvRewardTitle, tvExpiryDate, tvExpireSoon;
        ImageView RewardImg;
        CardView YourRewardCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            tvExpiryDate = (TextView) itemView.findViewById(R.id.expiryDate);
            tvExpireSoon = (TextView) itemView.findViewById(R.id.expireSoon);
            RewardImg = (ImageView) itemView.findViewById(R.id.rewardImg);
            YourRewardCardView = (CardView) itemView.findViewById(R.id.AllRewardCardView);
        }
    }

    public class dateDiff{
        public long daysBetween(Date one, Date two){
            long difference = (one.getTime() - two.getTime())/ 86400000;
            return Math.abs(difference);
        }
    }
}
