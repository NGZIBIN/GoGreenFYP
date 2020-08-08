package com.example.gogreenfyp.rewards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gogreenfyp.R;
import com.example.gogreenfyp.pojo.Rewards;
import com.example.gogreenfyp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RewardRedemptionInfoActivity extends AppCompatActivity {

    Button btnUseReward;
    TextView tvRewardTitle, tvExpiryDate, tvRewardTerms;
    ImageView rewardImg;
    String USER_ID;
    ArrayList<String> USER_REWARDS = new ArrayList<String>();
    List<Rewards> listReward;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_reward);

        btnUseReward = findViewById(R.id.btnUseReward);
        tvRewardTitle = findViewById(R.id.rewardTitle);
        tvExpiryDate = findViewById(R.id.tvExpiryDate);
        tvRewardTerms = findViewById(R.id.tvTnc);
        rewardImg = findViewById(R.id.rewardImg);
        USER_REWARDS = new ArrayList<String>();


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final String userID;
        USER_ID = fAuth.getCurrentUser().getUid();
        CollectionReference rewardsCollectionRef = db.collection("Rewards");

        // Get intent
        Intent i = getIntent();
        final String title = i.getStringExtra("RewardTitle");
        String instructions = i.getStringExtra("RewardInstructions");
        final String imageURL = i.getStringExtra("RewardImg");
        String terms = i.getStringExtra("RewardTerms");
        final int points = i.getIntExtra("RewardPoints", 0);
        final int quantity = i.getIntExtra("RewardQuantity", 0);
        int quantityLeft = i.getIntExtra("RewardQuantityLeft", 0);
        final String expiryDate = i.getStringExtra("RewardUseByDate");

        // Set data to text views
        tvRewardTitle.setText(title);
        tvExpiryDate.setText(expiryDate);
        tvRewardTerms.setText(terms);
        // Image
        Glide.with(getApplicationContext())
                .load(imageURL)
                .into(rewardImg);
        


        btnUseReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RewardRedemptionInfoActivity.this);
                dialog.setContentView(R.layout.dialog_use_reward);
                dialog.setCancelable(false);
                dialog.show();

                Button btnYes, btnNo;
                TextView tvExpiryDate, rewardTitle;
                final ImageView rewardImg;

                btnYes = dialog.findViewById(R.id.btnYes);
                btnNo = dialog.findViewById(R.id.btnNo);
                tvExpiryDate = dialog.findViewById(R.id.tvExpiryDate);
                rewardTitle = dialog.findViewById(R.id.rewardTitle);
                rewardImg = dialog.findViewById(R.id.rewardImg);

                //Set Reward Image
                Glide.with(getApplicationContext())
                        .load(imageURL)
                        .into(rewardImg);

                rewardTitle.setText(title);
                tvExpiryDate.setText(expiryDate);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        dialog.setContentView(R.layout.activity_rewards_redeeming);
                        dialog.dismiss();
                        Intent i = new Intent(RewardRedemptionInfoActivity.this, RewardRedemptionActivity.class);
                        i.putExtra("rewardImg", imageURL);
                        i.putExtra("rewardTitle", title);


                        startActivity(i);

                        //Delete from YourReward after redemption
                        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    String userIDAuth = "";
                                    for(final DocumentSnapshot documentSnapshot:task.getResult()){
                                        User user = documentSnapshot.toObject(User.class);
                                        userIDAuth = user.getUserID();
                                        if(userIDAuth.equals(USER_ID)){
                                            final String currentUser = documentSnapshot.getId();

                                            db.collection("Rewards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                                                            Rewards rewards = documentSnapshot.toObject(Rewards.class);
                                                            String titleCurrent = rewards.getName();
                                                            if(title.equals(titleCurrent)){
                                                                String currentReward = documentSnapshot.getId();
                                                                DocumentReference rewardArray = db.collection("Users").document(currentUser);
                                                                rewardArray.update("userRewards", FieldValue.arrayRemove(currentReward));
                                                                rewardArray.update("userRedeemedRewards", FieldValue.arrayUnion(currentReward));

                                                            }

                                                        }
                                                    }
                                                }
                                            });

                                        }
                                    }
                                }
                            }
                        });

                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }
}
