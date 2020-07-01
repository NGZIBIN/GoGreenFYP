package com.example.gogreenfyp;

public class Transaction {

    private String receiverAddress;
    private String senderAddress;
    private TransactionHeader transactionHeader;
    private TransactionDetails transactionDetails;

    public Transaction(String receiverAddress, String senderAddress, TransactionHeader transactionHeader, TransactionDetails transactionDetails) {
        this.receiverAddress = receiverAddress;
        this.senderAddress = senderAddress;
        this.transactionHeader = transactionHeader;
        this.transactionDetails = transactionDetails;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public TransactionHeader getTransactionHeader() {
        return transactionHeader;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }
}
