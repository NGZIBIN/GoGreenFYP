package com.example.gogreenfyp.pojo;

public class Badge {
    private String name;
    private String instructions;
    private String termsAndCond;
    private int pointsToRedeem;
    private int quantity;
    private int quantityLeft;

    public Badge(){
        // empty constructor needed
    }

    public Badge(String name, String instructions, String termsAndCond, int pointsToRedeem, int quantity, int quantityLeft) {
        this.name = name;
        this.instructions = instructions;
        this.termsAndCond = termsAndCond;
        this.pointsToRedeem = pointsToRedeem;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getTermsAndCond() {
        return termsAndCond;
    }

    public int getPointsToRedeem() {
        return pointsToRedeem;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityLeft() {
        return quantityLeft;
    }
}
