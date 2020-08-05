package com.example.gogreenfyp.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gogreenfyp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ImportWalletActivity extends AppCompatActivity {

    EditText etPrivateKey;
    Button btnImport;
    Wallet wallet = new Wallet();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);

        etPrivateKey = findViewById(R.id.etPrivateKey);
        btnImport = findViewById(R.id.btnImport);



        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String privateKey = etPrivateKey.getText().toString();

                if(isValidKey(privateKey)){
                    wallet.savePrivateKey(ImportWalletActivity.this, privateKey, firebaseAuth);
                }
                else{
                    etPrivateKey.setError("Invalid private key.");
                    Toast.makeText(ImportWalletActivity.this, "Invalid private key! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean isValidKey(String privateKey){

        if(privateKey == null){
            return false;
        }
        boolean isLength64 = privateKey.length() == 64;
        boolean isAlphaNumeric = privateKey.matches("[A-Za-z0-9]+");

        return isLength64 && isAlphaNumeric;
    }

}
