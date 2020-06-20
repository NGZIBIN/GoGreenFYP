package com.example.gogreenfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllRewardRedeem extends AppCompatActivity {

    private TextView tvrewardTitle, tvrewardPointsNeeded, tvcurrentPoints, tvQuantity;
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
        rewardImg = (ImageView) findViewById(R.id.RewardImg);
        tvQuantity = (TextView) findViewById(R.id.counter);
        btnRedeem = findViewById(R.id.btnRedeem);

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(AllRewardRedeem.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You will be spending your points!")
                        .setConfirmText("Redeem")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                final TextView tvQuantityTitle = new TextView(AllRewardRedeem.this);
                                final TextView tvQuantityCount = new TextView(AllRewardRedeem.this);
                                tvQuantityTitle.setText("Quantity");
                                tvQuantityCount.setText(tvQuantity.getText().toString());
                                new SweetAlertDialog(AllRewardRedeem.this, SweetAlertDialog.NORMAL_TYPE)
                                        .setTitleText("Custom view")
                                        .setConfirmText("Ok")
                                        .setCustomView(tvQuantityTitle)
                                        .setCustomView(tvQuantityCount)
                                        .show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        Intent i = getIntent();
        String title = i.getStringExtra("RewardTitle");
        int image = i.getExtras().getInt("RewardImg");

        tvrewardTitle.setText(title);
        rewardImg.setImageResource(image);

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
