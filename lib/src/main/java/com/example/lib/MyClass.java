package com.example.lib;

import java.util.Random;

public class MyClass {

    public static void main(String[] args){

//        String pass1 = generateRandomPassword();
//        String pass2 = generateRandomPassword();
//
//        System.out.println(pass1);
//        System.out.println(pass2);
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