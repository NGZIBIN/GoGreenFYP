package com.example.gogreenfyp.pojo;

public class Transaction {

    private double amount;
    private String place;
    private String item;
    private String transactionNo;
    private String from;
    private String to;
    private int points;

    public Transaction(){

    }

    public Transaction(double amount, String place, String item, String transactionNo, String from, String to, int points) {
        this.amount = amount;
        this.place = place;
        this.item = item;
        this.transactionNo = transactionNo;
        this.from = from;
        this.to = to;
        this.points = points;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getAmount() {
        return amount;
    }

    public String getPlace() {
        return place;
    }

    public String getItem() {
        return item;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public int getPoints() {
        return points;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

}
