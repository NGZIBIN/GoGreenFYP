package com.example.gogreenfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etEmail, etPassword;
    ProgressBar pb;
    FirebaseAuth fAuth;
//    TextView testing;
//    String UserID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    DocumentReference userRef = db.collection("Users").document("walletAddress");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        pb = findViewById(R.id.pb);
        final String userID;
        fAuth = FirebaseAuth.getInstance();
//        userID = fAuth.getCurrentUser().getUid();
//        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//
//                    String userIDAuth = "";
//                    int walletAdd = 0;
//                    for(DocumentSnapshot documentSnapshots: task.getResult()){
//                        User user = documentSnapshots.toObject(User.class);
//                        userIDAuth = user.getUserID();
//                        walletAdd = user.getWalletAddress();
//                        if(userIDAuth.equals(userID) && walletAdd == 0){
//                            Log.d("TAG", "SUCCESS" + userIDAuth + "Wallet address " + walletAdd);
//                        }
//
//                    }
//
//                }else {
//                    Log.d("TAG", "Get failed with " , task.getException());
//                }
//            }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is Required");
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


                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            setUpAlertDialog();

                        }else {
                            Toast.makeText(LoginActivity.this, "You are not registered yet!", Toast.LENGTH_LONG).show();
                            pb.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void setUpAlertDialog() {
        final String userID;
        userID = fAuth.getCurrentUser().getUid();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    String userIDAuth = "";
                    String walletAdd = "";

                    for(DocumentSnapshot documentSnapshots: task.getResult()){
                        User user = documentSnapshots.toObject(User.class);
                        userIDAuth = user.getUserID();
                        walletAdd = user.getWalletAddress();
                        if(userIDAuth.equals(userID)){
                            if(walletAdd.equals("0")){
                                WalletErrorDialog walletErrorDialog = new WalletErrorDialog(LoginActivity.this);
                                walletErrorDialog.show();
                            }else {
                                Toast.makeText(LoginActivity.this, "Successfully Login!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("walletAddress", walletAdd));
                            }
                            Log.d("TAG", "SUCCESS " + userIDAuth + " Wallet address " + walletAdd);
                        }
                    }

                }else {
                    Log.d("TAG", "Get failed with " , task.getException());
                }
            }
        });

    }

    public void goRegister(View view){
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }

    public void goForgetPass(View view){
        Intent i = new Intent(this, forgetPassword.class);
        startActivity(i);
    }
}
