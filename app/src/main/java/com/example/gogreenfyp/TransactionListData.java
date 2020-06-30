package com.example.gogreenfyp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionListData {

    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private CollectionReference collection = fireStore.collection("Users");

    public static HashMap<String, TransactionDetails> getTransactionDetails(){

        HashMap<String, TransactionDetails> transactionData = new HashMap<String, TransactionDetails>();

        transactionData.put("Chicken Rice TX*1505", new TransactionDetails("9025541321505", 150));

        transactionData.put("Wanton Mee TX*1393", new TransactionDetails("6180577123485",  200));

        transactionData.put("Chicken Rice TX*9312", new TransactionDetails("7540042539312", 150));

        transactionData.put("Wanton Mee TX*3290", new TransactionDetails("5541321393290",  200));

        return transactionData;
    }

    public static ArrayList<TransactionHeader> getTransactionHeaders(){

        ArrayList<TransactionHeader> transactionHeaders = new ArrayList<TransactionHeader>();


        return transactionHeaders;
    }


}
