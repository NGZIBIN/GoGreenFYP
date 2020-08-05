package com.example.gogreenfyp.pojo;

public class TransactionDetails {

    private String transactionNo;
    private int points;

    public TransactionDetails(String transactionNo, int points) {
        this.transactionNo = transactionNo;
        this.points = points;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
