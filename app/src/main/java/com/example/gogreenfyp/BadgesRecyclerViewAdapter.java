package com.example.gogreenfyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gogreenfyp.pojo.Badge;

import java.util.List;

public class BadgesRecyclerViewAdapter extends RecyclerView.Adapter<BadgesRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Badge> Data;

    public BadgesRecyclerViewAdapter(Context context, List<Badge> data) {
        this.context = context;
        Data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.badgescardview, parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgesRecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.tvBadgeTitle.setText(Data.get(position).getName());
        holder.amtUsage.setText(String.valueOf(Data.get(position).getUsagePoints()));
        holder.bonusPoints.setText(String.valueOf(Data.get(position).getBonusPoints()));
//        holder.badgeImg.setImageResource(Data.get(position).getBadgeImage());
        // Image
        Glide.with(context)
                .load(Data.get(position).getImageURL())
                .into(holder.badgeImg);

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView tvBadgeTitle;
        TextView amtUsage;
        TextView bonusPoints;
        ImageView badgeImg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBadgeTitle = (TextView) itemView.findViewById(R.id.badgeTitle);
            amtUsage = (TextView) itemView.findViewById(R.id.amtUsage);
            badgeImg = (ImageView) itemView.findViewById(R.id.badgeImage);
            bonusPoints = (TextView) itemView.findViewById(R.id.bonusPoints);
        }
    }

}
