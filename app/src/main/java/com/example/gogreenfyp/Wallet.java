package com.example.gogreenfyp;

import android.app.Activity;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Wallet {

    public static String createWallet(Activity activity) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {

        String address = "";
        String walletPath = activity.getFilesDir().getAbsolutePath();
        File walletFile = new File(walletPath);
        String fileName = WalletUtils.generateNewWalletFile("mwYxwtTeT7r1", walletFile);
        String fullPath = walletPath+"/"+fileName;

        walletFile = new File(fullPath);

        Credentials credentials = WalletUtils.loadCredentials("mwYxwtTeT7r1", walletFile);
        address = credentials.getAddress();

        return address;
    }

//    public static String generateRandomPassword(){
//
//        char[] allCharacters = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','~','!','@','#','$','%','^','&','*','(',')','-','=','+','[','{',']','}','|',';',':','<','.','>','/','?'};
//
//        for(int i = 0; i < 10; i++){
//
//        }
//
//        return password;
//    }
//
//    public static

}
