package com.example.lib;

import java.util.Random;

public class MyClass {

    public static void main(String[] args){

        String str1 = "BAFF065CFA87201DF8232D7D4FE32A31A2FAED6468CB0C8415CC46F1DDDAE163";
        String str2 = "0x7719a994B36e69020A4E9269d326014784b089D9";
//
        System.out.println(str1.matches("[A-Za-z0-9]+"));
        System.out.println(str2.matches("[A-Za-z0-9]+"));
    }

    public static String generateRandomPassword(){

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