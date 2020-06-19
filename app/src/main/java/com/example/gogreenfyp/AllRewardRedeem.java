package com.example.gogreenfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AllRewardRedeem extends AppCompatActivity {

    private TextView tvrewardTitle, tvrewardPointsNeeded, tvcurrentPoints, tvQuantity;
    private ImageView rewardImg;
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
