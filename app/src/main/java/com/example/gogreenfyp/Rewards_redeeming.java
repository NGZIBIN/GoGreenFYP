package com.example.gogreenfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Rewards_redeeming extends AppCompatActivity {
    CountDownTimer countDownTimer;
    final long[] timeInMiliSeconds = {120000};
    boolean timerRunning;
    TextView timer;
    Button btnDone;
    TextView rewardTitle;
    ImageView rewardImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_redeeming);
        btnDone = findViewById(R.id.btnDone);
        rewardTitle = findViewById(R.id.rewardTitle);
        rewardImg = findViewById(R.id.rewardImg);

        Intent i = getIntent();
        String imgURL = i.getStringExtra("rewardImg");
        String rewardTitleName = i.getStringExtra("rewardTitle");

        //Set Reward Image
        Glide.with(getApplicationContext())
                .load(imgURL)
                .into(rewardImg);

        //Set Reward Title
        rewardTitle.setText(rewardTitleName);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(Rewards_redeeming.this);
                dialog.setContentView(R.layout.dialog_use_success);
                dialog.setCancelable(false);
                dialog.show();


                Button btnFinsh;

                btnFinsh = dialog.findViewById(R.id.btnFinish);

                btnFinsh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent i = new Intent(Rewards_redeeming.this, MainActivity.class);
                        startActivity(i);
                    }
                });


            }
        });

        timer = findViewById(R.id.timer);

        countDownTimer = new CountDownTimer(timeInMiliSeconds[0], 1000) {
            @Override
            public void onTick(long l) {
                timeInMiliSeconds[0] = l;
                int seconds = (int) timeInMiliSeconds[0] % 600000 / 1000;

                String timeLeft;

                timeLeft = "" + seconds;
                timer.setText(timeLeft);

            }

            @Override
            public void onFinish() {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(Rewards_redeeming.this);
                myBuilder.setTitle("Redemption Complete");
                myBuilder.setMessage("Thank you for your support! Continue to contribute and save the environment!");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Rewards_redeeming.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();


            }
        }.start();

    }
}
