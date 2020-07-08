package com.example.gogreenfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Use_reward extends AppCompatActivity {

    Button btnUseReward;
    TextView tvRewardTitle, tvExpiryDate;
    ImageView rewardImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_reward);

        btnUseReward = findViewById(R.id.btnUseReward);
        tvRewardTitle = findViewById(R.id.rewardTitle);
        tvExpiryDate = findViewById(R.id.tvExpiryDate);
        rewardImg = findViewById(R.id.rewardImg);


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
