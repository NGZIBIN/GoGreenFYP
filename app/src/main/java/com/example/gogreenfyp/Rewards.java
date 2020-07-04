package com.example.gogreenfyp;

public class Rewards{

   private String instructions;
   private String name;
   private String termsAndCondition;
   private String imageURL;
   private int pointsToRedeem;
   private int quantity;
   private int quantityLeft;

   public Rewards() {

    }

    public Rewards(String instructions, String name, String termsAndCondition, int pointsToRedeem, int quantity, int quantityLeft, String imageURL) {
        this.instructions = instructions;
        this.name = name;
        this.termsAndCondition = termsAndCondition;
        this.pointsToRedeem = pointsToRedeem;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }
}
