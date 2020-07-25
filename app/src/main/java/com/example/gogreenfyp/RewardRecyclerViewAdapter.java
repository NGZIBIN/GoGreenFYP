package com.example.gogreenfyp;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RewardRecyclerViewAdapter extends RecyclerView.Adapter<RewardRecyclerViewAdapter.MyViewHolder> implements Filterable {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
//    List<AllReward> rewardList =  new ArrayList<>();
    ArrayList<String> allRewards;
    private Context context;
    private List<Rewards> Data;
    List<Rewards> allData;

    public RewardRecyclerViewAdapter(Context context, List<Rewards> data) {
        this.context = context;
        Data = data;
        allData = new ArrayList<>(data);

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


//        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    String userIDAuth = "";
//                    for(DocumentSnapshot documentSnapshots:task.getResult()){
//                        User user = documentSnapshots.toObject(User.class);
//                        userIDAuth = user.getUserID();
//                        if(userIDAuth.equals(userID)){
//                           allRewards = (ArrayList<String>) documentSnapshots.get("userRewards");
//                        }
//                    }
//                }
//            }
//        });
//        Log.d("ALl Rewards", allRewards + "");
//        for(int i = 0; i<allRewards.size(); i++){
//            holder.AllRewardCardView.setVisibility(View.GONE);
//        }


        // Image
        Glide.with(context)
                .load(Data.get(position).getImageURL())
                .into(holder.RewardImg);

        //Log.d("IMAGE", Data.get(position).getImageURL());

        holder.AllRewardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AllRewardRedeem.class);
                i.putExtra("RewardTitle", Data.get(position).getName());
                i.putExtra("RewardInstructions", Data.get(position).getInstructions());
                i.putExtra("RewardPoints", Data.get(position).getPointsToRedeem());
                i.putExtra("RewardQuantity", Data.get(position).getQuantity());
                i.putExtra("RewardQuantityLeft", Data.get(position).getQuantityLeft());
                i.putExtra("RewardTerms", Data.get(position).getTermsAndConditions());
                i.putExtra("RewardImg", Data.get(position).getImageURL());
                i.putExtra("RewardUseByDate", Data.get(position).getUseByDate());
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
