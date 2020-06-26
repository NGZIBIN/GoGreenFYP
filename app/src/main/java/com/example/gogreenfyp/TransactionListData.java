package com.example.gogreenfyp;

import java.util.HashMap;

public class TransactionListData {

    public static HashMap<String, TransactionDetails> getTransactionData(){

        HashMap<String, TransactionDetails> transactionData = new HashMap<String, TransactionDetails>();

        transactionData.put("Chicken Rice TX*1505", new TransactionDetails("9025541321505", 150));

        transactionData.put("Wanton Mee TX*1393", new TransactionDetails("6180577123485",  200));

        transactionData.put("Chicken Rice TX*9312", new TransactionDetails("7540042539312", 150));

        transactionData.put("Wanton Mee TX*3290", new TransactionDetails("5541321393290",  200));

        return transactionData;
    }


}
