package com.example.gogreenfyp;

import java.util.Date;

public class Rewards{

   private String instructions;
   private String name;
   private String termsAndConditions;
   private String imageURL;
   private int pointsToRedeem;
   private int quantity;
   private int quantityLeft;
   private Date useByDate;

   public Rewards() {

    }

    public Rewards(String instructions, String name, String termsAndConditions, int pointsToRedeem, int quantity, int quantityLeft, String imageURL, Date useByDate) {
        this.instructions = instructions;
        this.name = name;
        this.termsAndConditions = termsAndConditions;
        this.pointsToRedeem = pointsToRedeem;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
        this.imageURL = imageURL;
        this.useByDate = useByDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
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

    public Date getUseByDate() {
        return useByDate;
    }
}
