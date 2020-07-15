package com.example.gogreenfyp.pojo;

public class Badge {
    private String name;
    private String imageURL;
    private int usagePoints;
    private int bonusPoints;



    public Badge(){
        // empty constructor needed
    }

    public Badge(String name, String imageURL, int usagePoints, int bonusPoints) {
        this.name = name;
        this.imageURL = imageURL;
        this.usagePoints = usagePoints;
        this.bonusPoints = bonusPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String badgeImage) {
        this.imageURL = badgeImage;
    }

    public int getUsagePoints() {
        return usagePoints;
    }

    public void setUsagePoints(int usagePoints) {
        this.usagePoints = usagePoints;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
