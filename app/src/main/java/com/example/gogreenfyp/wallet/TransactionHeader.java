package com.example.gogreenfyp.wallet;

public class TransactionHeader {

    private String place, item;
    private double amount;

    public TransactionHeader(String item, String place, double amount) {
        this.place = place;
        this.item = item;
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
