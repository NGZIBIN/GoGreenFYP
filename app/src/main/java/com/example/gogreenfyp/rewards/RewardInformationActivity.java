package com.example.gogreenfyp.rewards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gogreenfyp.MainActivity;
import com.example.gogreenfyp.R;
import com.example.gogreenfyp.pojo.Rewards;
import com.example.gogreenfyp.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RewardInformationActivity extends AppCompatActivity {

    private TextView tvrewardTitle, tvrewardPointsNeeded, tvcurrentPointsMain, tvQuantity, tvTerms;
    private ImageView rewardImg;
    private Button btnRedeem;
    int counter;
    int currentPoints = 0;
    private static final String KEY_POINTS = "pointsBalance";
    private static final String KEY_QUANTITY = "quantityLeft";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reward_redeem);
        tvcurrentPointsMain = findViewById(R.id.currentPoints);
        tvrewardTitle = (TextView) findViewById(R.id.rewardTitle);
        tvrewardPointsNeeded = (TextView) findViewById(R.id.pointsNeeded);
        tvTerms = findViewById(R.id.tvTerms);
        rewardImg = (ImageView) findViewById(R.id.RewardImg);
        tvQuantity = (TextView) findViewById(R.id.counter);
        btnRedeem = findViewById(R.id.btnRedeem);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final String userID;
        userID = fAuth.getCurrentUser().getUid();




        Intent i = getIntent();
        final String title = i.getStringExtra("RewardTitle");
        String instructions = i.getStringExtra("RewardInstructions");
        final String imageURL = i.getStringExtra("RewardImg");
        String terms = i.getStringExtra("RewardTerms");
        final int points = i.getIntExtra("RewardPoints", 0);
        final int quantity = i.getIntExtra("RewardQuantity", 0);
        int quantityLeft = i.getIntExtra("RewardQuantityLeft", 0);

        //int image = i.getExtras().getInt("RewardImg");

        // Set reward information
        tvrewardTitle.setText(title);
        tvrewardPointsNeeded.setText(points + " points");
        tvTerms.setText(terms);
//        rewardImg.setImageResource(imageURL);

        // Image
        Glide.with(getApplicationContext())
                .load(imageURL)
                .into(rewardImg);

        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String userIDAuth = "";
                    int pointsBalance = 0;
                    for(DocumentSnapshot documentSnapshots:task.getResult()){
                        User user = documentSnapshots.toObject(User.class);
                        userIDAuth = user.getUserID();
                        pointsBalance = user.getPointsBalance();
                        if(userIDAuth.equals(userID)){
                            String points = String.valueOf(pointsBalance);
                            tvcurrentPointsMain.setText(points);
                        }
                    }
                }
            }
        });

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final String quantitySelected = tvQuantity.getText().toString();
                final Dialog dialog = new Dialog(RewardInformationActivity.this);
                dialog.setContentView(R.layout.dialog_confirm_redemption);
                dialog.setCancelable(false);
                dialog.show();


                Button btnYes, btnNo;
                final TextView rewardTitle, tvQuantity, tvPointsRequired, tvCurrentpoints;
                ImageView rewardImg;


                btnYes = dialog.findViewById(R.id.btnYes);
                btnNo = dialog.findViewById(R.id.btnNo);
                rewardImg = dialog.findViewById(R.id.rewardImg);
                rewardTitle = dialog.findViewById(R.id.rewardTitle);
                tvQuantity = dialog.findViewById(R.id.tvQuantity);
                tvPointsRequired = dialog.findViewById(R.id.tvPointsRequired);
                tvCurrentpoints = dialog.findViewById(R.id.tvCurrentBalance);

                //Set Reward Image
                Glide.with(getApplicationContext())
                        .load(imageURL)
                        .into(rewardImg);


                //Setting Quantity
                tvQuantity.setText(quantitySelected);

                final int qty = Integer.parseInt(quantitySelected);
                int totalpointsNeededStr = qty * points;
                String pointsNeeded = String.valueOf(totalpointsNeededStr);
                String titleStr = title;
                rewardTitle.setText(titleStr);

                tvCurrentpoints.setText(tvcurrentPointsMain.getText().toString());
                tvPointsRequired.setText(pointsNeeded);


                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        final int totalPointsSpend = qty * points;
                        String currentPointsString = tvcurrentPointsMain.getText().toString();
                        currentPoints = Integer.parseInt(currentPointsString);


                        if(currentPoints < totalPointsSpend){
                            Toast.makeText(RewardInformationActivity.this, "You do not have enough points!", Toast.LENGTH_LONG).show();

                        }else{
                            //Updating of User Points
                            db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        currentPoints = currentPoints - totalPointsSpend;
                                        String userIDAuth = "";
                                        for(final DocumentSnapshot documentSnapshots:task.getResult()){
                                            User user = documentSnapshots.toObject(User.class);
                                            userIDAuth = user.getUserID();
//                                            Log.d("TAG THIS ID DIFFERENT", userID);
//                                            Log.d("TAG ALL USER NOW", userIDAuth);
                                            if(userIDAuth.equals(userID)){
                                                final String currentUser = documentSnapshots.getId();
                                                Log.d("TAG Current User", currentUser);
                                                DocumentReference usersPointsRef = db.collection("Users").document(currentUser);
                                                Map<String, Object> points = new HashMap<>();
                                                points.put(KEY_POINTS, currentPoints);
                                                usersPointsRef.set(points, SetOptions.merge());


                                                //Updating quantity left
                                                db.collection("Rewards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            for(DocumentSnapshot documentSnapshot:task.getResult()){

                                                                Rewards rewards = documentSnapshot.toObject(Rewards.class);
                                                                String selectedQty = tvQuantity.getText().toString();
                                                                int qty = Integer.parseInt(selectedQty);
                                                                int quantity1 = rewards.getQuantityLeft();
                                                                String titleCurrent = rewards.getName();
                                                                Log.d("Tag CURRENT QTY ", String.valueOf(quantity1));
                                                                int qtyLeft = quantity1 - qty;
                                                                if(title.equals(titleCurrent)){
                                                                    String currentReward = documentSnapshot.getId();
                                                                    DocumentReference qtyRef = db.collection("Rewards").document(currentReward);
                                                                    Map<String, Object> quantityLeft = new HashMap<>();
                                                                    quantityLeft.put(KEY_QUANTITY, qtyLeft);
                                                                    qtyRef.set(quantityLeft, SetOptions.merge());
                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                                //Success Dialog
                                                final Dialog dialog = new Dialog(RewardInformationActivity.this);
                                                dialog.setContentView(R.layout.dialog_success_redemption);
                                                dialog.setCancelable(false);
                                                dialog.show();

                                                Button btnYes, btnNo;
                                                ImageView rewardImg;
                                                final TextView tvQuantity, tvTransNum, tvCurrentBalance, tvRewardTitle;

                                                btnYes = dialog.findViewById(R.id.btnYes);
//                                                btnNo = dialog.findViewById(R.id.btnNo);
                                                rewardImg = dialog.findViewById(R.id.rewardImg);
                                                tvRewardTitle = dialog.findViewById(R.id.rewardTitle);
                                                tvQuantity = dialog.findViewById(R.id.tvQuantity);
                                                tvTransNum = dialog.findViewById(R.id.tvTransNum);
                                                tvCurrentBalance = dialog.findViewById(R.id.tvCurrentBalance);

                                                //Set Reward Image
                                                Glide.with(getApplicationContext())
                                                        .load(imageURL)
                                                        .into(rewardImg);

                                                tvRewardTitle.setText(title);


                                                //Setting Quantity
                                                tvQuantity.setText(quantitySelected);

                                                //Current points
                                                db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()){

                                                            for(DocumentSnapshot documentSnapshotsBalance:task.getResult()){
                                                                User user = documentSnapshotsBalance.toObject(User.class);
                                                                String userIDAuth = user.getUserID();
                                                                int pointsBalance = user.getPointsBalance();
                                                                String strBalance = String.valueOf(pointsBalance);
                                                                if(userIDAuth.equals(userID)){
                                                                    tvCurrentBalance.setText(strBalance);
                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                                //Title
                                                String titleStr = title;
                                                rewardTitle.setText(titleStr);

                                                //Adding into users Reward
                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
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
//
                                                                            rewardArray.update("userRewards", FieldValue.arrayUnion(currentReward));
                                                                            rewardArray.update("allRewards", FieldValue.arrayRemove(currentReward));

                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        });
//                                                        Intent i = new Intent(AllRewardRedeem.class, AllRewardsFragment.class)
                                                        dialog.dismiss();
                                                        Intent i = new Intent(RewardInformationActivity.this, MainActivity.class);
                                                        startActivity(i);
                                                        Toast.makeText(RewardInformationActivity.this, "Successfully Redeem Reward!", Toast.LENGTH_SHORT).show();
                                                        finish();





                                                    }
                                                });

//                                                btnNo.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//                                                        Intent i = new Intent(AllRewardRedeem.this, MainActivity.class);
//                                                        startActivity(i);
//                                                        dialog.dismiss();
//                                                    }
//                                                });



                                            }
                                        }
                                    }
                                }
                            });
                        }

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




//        initCounter();
//        addBtn(new View(this));
//        minusBtn(new View(this));
    }

//    public void initCounter(){
//        counter = 1;
//        tvQuantity.setText(String.valueOf(counter));
//    }
//
//    public void minusBtn(View view){
//        counter --;
//        tvQuantity.setText(String.valueOf(counter));
//    }
//
//    public void addBtn(View view) {
//        counter ++;
//        tvQuantity.setText(String.valueOf(counter));
//    }
}
