package com.example.gogreenfyp;

public class Rewards{

   private String instructions;
   private String name;
   private String termsAndCondition;
   private int pointsToRedeem;
   private int quantity;
   private int quantityLeft;

   public Rewards() {

    }

    public Rewards(String instructions, String name, String termsAndCondition, int pointsToRedeem, int quantity, int quantityLeft) {
        this.instructions = instructions;
        this.name = name;
        this.termsAndCondition = termsAndCondition;
        this.pointsToRedeem = pointsToRedeem;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
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
