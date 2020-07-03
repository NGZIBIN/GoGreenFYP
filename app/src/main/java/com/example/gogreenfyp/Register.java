package com.example.gogreenfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText etUsername, etPassword, etEmail;
    Button btnRegister;
    String userID;
    ProgressBar pb;
    FirebaseAuth fAuth;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    DocumentReference userDoc = db.document("Users/" + userID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);
        pb = findViewById(R.id.pb);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//
//        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                final int walletBalance = 0;
                final int pointsBalance = 0;
                final int badgeProgress = 0;
                final String walletAddress = "0";
                String badges = "Rookie, Elite, Prestige ";
                String[] badgesArray = badges.split("\\s*,\\s*");
                final List<String> badgesTag = Arrays.asList(badgesArray);

                String userRewards = "GongCha, Koi";
                String[] userRewardsArray = userRewards.split("\\s*,\\s*");
                final List<String> userRewardsTag = Arrays.asList(userRewardsArray);

                String userRedeemedRewards = "GongCha, Koi";
                String[] userRedeemedRewardsArray = userRedeemedRewards.split("\\s*,\\s*");
                final List<String> userRedeemedRewardsTag = Arrays.asList(userRedeemedRewardsArray);



                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(username)){
                    etUsername.setError("Username is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is required");
                    return;
                }
                if(password.length() < 8){
                    etPassword.setError("Password must have at least 8 characters");
                    return;
                }

                    pb.setVisibility(View.VISIBLE);



                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_LONG).show();
                            userID = fAuth.getCurrentUser().getUid();
                            User user = new User(userID, username, email, walletBalance, pointsBalance, badgeProgress, walletAddress,badgesTag, userRewardsTag, userRedeemedRewardsTag);
                            userRef.add(user);
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else {
                            Toast.makeText(Register.this, "Error, please try again! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            pb.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
    }
}