package com.example.gogreenfyp;

public class TransactionHistory {

    private String transactionNum;
    private int points;

    public TransactionHistory(String transactionNum, int points) {
        this.transactionNum = transactionNum;
        this.points = points;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
