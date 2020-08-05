package com.example.gogreenfyp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Random;

import jnr.ffi.annotations.In;

public class Wallet {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Users");

    public boolean createWallet(Activity activity, FirebaseAuth firebaseAuth) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {

        String address;
        setupBouncyCastle();
        String walletPath = activity.getFilesDir().getAbsolutePath();
        String password = generateRandomPassword();
        File walletFile = new File(walletPath);
        String fileName = WalletUtils.generateNewWalletFile(password, walletFile);
        String fullPath = walletPath+"/"+fileName;

        walletFile = new File(fullPath);

        Credentials credentials = WalletUtils.loadCredentials(password, walletFile);
        address = credentials.getAddress();
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        savePrivateKey(activity, privateKey, firebaseAuth);

        updateWalletAddress(firebaseAuth, address);

        return address != null;
    }

    public void savePrivateKey(Activity activity, String privateKey, FirebaseAuth firebaseAuth){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(firebaseAuth.getCurrentUser() != null && privateKey != null) {
            String userID = firebaseAuth.getCurrentUser().getUid();
            Credentials credentials = Credentials.create(privateKey);
            String address = credentials.getAddress();
            editor.putString(userID, privateKey);
            editor.apply();
            updateWalletAddress(firebaseAuth, address);
            Toast.makeText(activity, "Imported successfully", Toast.LENGTH_SHORT).show();
            Log.d("Wallet saved", "Success");
            activity.finish();
        }

    }

    public String getWalletAddress(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        return sharedPreferences.getString("address", "");
    }

    private void updateWalletAddress(FirebaseAuth firebaseAuth, final String walletAddress) {

        if (firebaseAuth.getCurrentUser() != null) {

            String userID = firebaseAuth.getCurrentUser().getUid();

            collectionReference.whereEqualTo("userID", userID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        String docPath = documentSnapshot.getId();
                        DocumentReference documentReference = collectionReference.document(docPath);

                        HashMap<String, Object> newWalletAddress = new HashMap<String, Object>();
                        newWalletAddress.put("walletAddress", walletAddress);

                        documentReference.set(newWalletAddress, SetOptions.merge());
                        Log.d("Update", "Success");
                    }
                }
            });
        }
    }

    private static String generateRandomPassword(){

        String password = "";
        String[] allCharacters = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9","~","!","@","#","$","%","^","&","*","(",")","-","=","+","[","{","]","}","|",";",":","<",".",">","/","?"};

        Random random = new Random();
        int length = allCharacters.length;

        for(int i = 0; i < 10; i++){
            int randNumber = random.nextInt(length);
            password += allCharacters[randNumber];
        }
        return password;
    }

    public Credentials getWalletCredentials(Activity activity) throws IOException, CipherException {
        if(activity == null || FirebaseAuth.getInstance().getCurrentUser() == null) {
            return  null;
        }

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String privateKey = sharedPreferences.getString(userID, "");

        return Credentials.create(privateKey);
    }

    private static void setupBouncyCastle() {
        Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

}
