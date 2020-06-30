/*
package com.example.gogreenfyp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gogreenfyp.R;
import com.example.gogreenfyp.Rewards;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RewardAdapter extends FirestoreRecyclerAdapter<Rewards, RewardAdapter.RewardHolder> {
    public RewardAdapter(@NonNull FirestoreRecyclerOptions<Rewards> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RewardHolder holder, int position, @NonNull Rewards model) {
        holder.rewardTitle.setText(model.getRewardName());
        holder.rewardPoints.setText(model.getPoints());
    }

    @NonNull
    @Override
    public RewardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.AllRewardCardView, parent, false);
        return new RewardHolder(v);
    }

    class RewardHolder extends RecyclerView.ViewHolder {
        TextView rewardTitle, rewardPoints;

        public RewardHolder(@NonNull View itemView) {
            super(itemView);
            rewardTitle = itemView.findViewById(R.id.rewardTitle);
            rewardPoints = itemView.findViewById(R.id.rewardPointsToClaim);
        }
    }
}
*/
