package com.example.gogreenfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Use_reward extends AppCompatActivity {

    Button btnUseReward;
    TextView tvRewardTitle, tvExpiryDate, tvRewardTerms;
    ImageView rewardImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_reward);

        btnUseReward = findViewById(R.id.btnUseReward);
        tvRewardTitle = findViewById(R.id.rewardTitle);
        tvExpiryDate = findViewById(R.id.tvExpiryDate);
        tvRewardTerms = findViewById(R.id.tvTnc);
        rewardImg = findViewById(R.id.rewardImg);

        // Get intent
        Intent i = getIntent();
        final String title = i.getStringExtra("RewardTitle");
        String instructions = i.getStringExtra("RewardInstructions");
        final String imageURL = i.getStringExtra("RewardImg");
        String terms = i.getStringExtra("RewardTerms");
        final int points = i.getIntExtra("RewardPoints", 0);
        final int quantity = i.getIntExtra("RewardQuantity", 0);
        int quantityLeft = i.getIntExtra("RewardQuantityLeft", 0);
        String expiryDate = i.getStringExtra("RewardUseByDate");

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
                final Dialog dialog = new Dialog(Use_reward.this);
                dialog.setContentView(R.layout.dialog_use_reward);
                dialog.setCancelable(false);
                dialog.show();

                Button btnYes, btnNo;
                btnYes = findViewById(R.id.btnYes);
                btnNo = findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.setContentView(R.layout.dialog_success_redemption);
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
