package com.example.gogreenfyp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllRewardRedeem extends AppCompatActivity {

    private TextView tvrewardTitle, tvrewardPointsNeeded, tvcurrentPoints, tvQuantity, tvTerms;
    private ImageView rewardImg;
    private Button btnRedeem;
    int counter;

//    private View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.bt
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reward_redeem);

        tvrewardTitle = (TextView) findViewById(R.id.rewardTitle);
        tvrewardPointsNeeded = (TextView) findViewById(R.id.pointsNeeded);
        tvTerms = findViewById(R.id.tvTerms);
        rewardImg = (ImageView) findViewById(R.id.RewardImg);
        tvQuantity = (TextView) findViewById(R.id.counter);
        btnRedeem = findViewById(R.id.btnRedeem);

//        btnRedeem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new SweetAlertDialog(AllRewardRedeem.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("You will be spending your points!")
//                        .setConfirmText("Redeem")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                final TextView tvQuantityTitle = new TextView(AllRewardRedeem.this);
//                                final TextView tvQuantityCount = new TextView(AllRewardRedeem.this);
//                                tvQuantityTitle.setText("Quantity");
//                                tvQuantityCount.setText(tvQuantity.getText().toString());
//                                new SweetAlertDialog(AllRewardRedeem.this, SweetAlertDialog.NORMAL_TYPE)
//                                        .setTitleText("Custom view")
//                                        .setConfirmText("Ok")
//                                        .setCustomView(tvQuantityTitle)
//                                        .setCustomView(tvQuantityCount)
//                                        .show();
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
//            }
//        });

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.dialog_confirm_redemption, null);

                final AlertDialog myBuilder = new AlertDialog.Builder(AllRewardRedeem.this)
                        .setView(viewDialog).show();

            }
        });

        Intent i = getIntent();
        String title = i.getStringExtra("RewardTitle");
        String instructions = i.getStringExtra("RewardInstructions");
        String terms = i.getStringExtra("RewardTerms");
        int points = i.getIntExtra("RewardPoints", 0);
        int quantity = i.getIntExtra("RewardQuantity", 0);
        int quantityLeft = i.getIntExtra("RewardQuantityLeft", 0);

        //int image = i.getExtras().getInt("RewardImg");

        // Set reward information
        tvrewardTitle.setText(title);
        tvrewardPointsNeeded.setText(points + " points");
        tvTerms.setText(terms);
        //rewardImg.setImageResource(image);

        initCounter();
        addBtn(new View(this));
        minusBtn(new View(this));
    }

    public void initCounter(){
        counter = 1;
        tvQuantity.setText(String.valueOf(counter));
    }

    public void minusBtn(View view){
        counter --;
        tvQuantity.setText(String.valueOf(counter));
    }

    public void addBtn(View view) {
        counter ++;
        tvQuantity.setText(String.valueOf(counter));
    }
}
