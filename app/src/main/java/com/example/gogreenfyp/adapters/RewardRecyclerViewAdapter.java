package com.example.gogreenfyp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gogreenfyp.pojo.User;
import com.example.gogreenfyp.rewards.RewardInformationActivity;
import com.example.gogreenfyp.R;
import com.example.gogreenfyp.pojo.Rewards;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    ArrayList<String> USER_REWARDS;
    ArrayList<String> USER_HISTORY_REWARDS_ID;
    ArrayList<String> REWARDS_DOC_ID;

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

        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete (@NonNull Task < QuerySnapshot > task) {
                if (task.isSuccessful()) {

                    USER_REWARDS = new ArrayList<>();
                    USER_HISTORY_REWARDS_ID = new ArrayList<>();

                    // Loop through all user documents
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        // Convert user document into User objects
                        User user = documentSnapshot.toObject(User.class);

                        // Get all user id
                        String USER_ID = user.getUserID();

                        // Check if current user id == USER_ID
                        if (fAuth.getCurrentUser().getUid().equals(USER_ID)) {
                            // Get current user's rewards and store in list
                            USER_REWARDS = (ArrayList<String>) documentSnapshot.get("userRewards");
                            USER_HISTORY_REWARDS_ID = (ArrayList<String>) documentSnapshot.get("userRedeemedRewards");

                            Log.d("USER REWARDS", USER_REWARDS.toString());
                            break;
                        }
                    }
                }
                // Retrieve catalog of rewards
                db.collection("Rewards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            REWARDS_DOC_ID = new ArrayList<>();
                            // Loop through all rewards documents
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                REWARDS_DOC_ID.add(document.getId());
                            }
                            // Loop through userRewards array in user document
                            if(Data.get(position).getQuantityLeft() < 1 && !USER_REWARDS.contains(REWARDS_DOC_ID) && !USER_HISTORY_REWARDS_ID.contains(REWARDS_DOC_ID)){
                                Log.d("Data: ", Data.get(position).getName());
                                holder.tvRewardPointsNeeded.setText("FULLY");
                                holder.tvTitlePoints.setText("REDEEMED");
                            }

                        }
                    }
                });
            }
        });



        Date fStoreDate = Data.get(position).getUseByDate();
        Date currDate = Calendar.getInstance().getTime();

        holder.tvRewardTitle.setText(Data.get(position).getName());
        holder.tvRewardPointsNeeded.setText(String.valueOf(Data.get(position).getPointsToRedeem()));

        dateDiff diff = new dateDiff();
        long days = diff.daysBetween(currDate, fStoreDate);
        Log.d("Days diff", String.valueOf(days));
        int negDay = (int) - days;
        Log.d("Negative day", String.valueOf(negDay));


        // Image
        Glide.with(context)
                .load(Data.get(position).getImageURL())
                .into(holder.RewardImg);

        //Log.d("IMAGE", Data.get(position).getImageURL());

        holder.AllRewardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RewardInformationActivity.class);
                i.putExtra("RewardTitle", Data.get(position).getName());
                i.putExtra("RewardInstructions", Data.get(position).getInstructions());
                i.putExtra("RewardPoints", Data.get(position).getPointsToRedeem());
                i.putExtra("RewardQuantity", Data.get(position).getQuantity());
                i.putExtra("RewardQuantityLeft", Data.get(position).getQuantityLeft());
                i.putExtra("RewardTerms", Data.get(position).getTermsAndConditions());
                i.putExtra("RewardImg", Data.get(position).getImageURL());
                i.putExtra("RewardUseByDate", Data.get(position).getUseByDate());

                // context.startActivity(i);
                // Loop through userRewards array in user document
                if(Data.get(position).getQuantityLeft() < 1 && !USER_REWARDS.contains(REWARDS_DOC_ID) && !USER_HISTORY_REWARDS_ID.contains(REWARDS_DOC_ID)){
                    Log.d("Data: ", Data.get(position).getName());
                    holder.tvRewardPointsNeeded.setText("FULLY");
                    holder.tvTitlePoints.setText("REDEEMED");
                } else {
                    context.startActivity(i);
                }

                /*if(fStoreDate.after(currDate) || days == 0){
                    context.startActivity(i);
                }else{
                    Toast.makeText(context, "Sorry Reward No Longer Available", Toast.LENGTH_SHORT).show();

                }*/

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
        TextView tvTitlePoints;
        ImageView RewardImg;
        CardView AllRewardCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            tvRewardPointsNeeded = (TextView) itemView.findViewById(R.id.rewardPointsToClaim);
            tvTitlePoints = (TextView) itemView.findViewById(R.id.tvTextPoints);
            RewardImg = (ImageView) itemView.findViewById(R.id.rewardImg);
            AllRewardCardView = (CardView) itemView.findViewById(R.id.AllRewardCardView);
        }
    }

    public class dateDiff{
        public long daysBetween(Date one, Date two){
            long difference = (one.getTime() - two.getTime())/ 86400000;
            return Math.abs(difference);
        }
    }
}
