package com.example.gogreenfyp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;

public class Wallet {

    public static boolean createWallet(Activity activity, FirebaseAuth firebaseAuth) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {

        String address;
        String walletPath = activity.getFilesDir().getAbsolutePath();
        String password = generateRandomPassword();
        File walletFile = new File(walletPath);
        String fileName = WalletUtils.generateNewWalletFile(password, walletFile);
        String fullPath = walletPath+"/"+fileName;

        walletFile = new File(fullPath);

        Credentials credentials = WalletUtils.loadCredentials(password, walletFile);
        address = credentials.getAddress();

        if(saveWalletDetails(activity, walletFile.getName(), password, firebaseAuth)){
            Log.d("Wallet saved", "Success");
        }

        return address == null;
    }

    private static boolean saveWalletDetails(Activity activity, String walletFileName, String password, FirebaseAuth firebaseAuth){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(firebaseAuth.getCurrentUser() != null) {
            String userID = firebaseAuth.getCurrentUser().getUid();
            editor.putString(userID+"File", walletFileName);
            editor.putString(userID+"Pass", password);
            editor.apply();
            return true;
        }
        return false;
    }

    private static void updateWalletAddress(FirebaseAuth firebaseAuth, String walletAddress){

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

}
