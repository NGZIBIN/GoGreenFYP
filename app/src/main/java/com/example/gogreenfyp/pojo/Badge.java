package com.example.gogreenfyp.pojo;

public class Badge {
    private String name;
    private String badgeImage;
    private int usagePoints;
    private int bonusPoints;



    public Badge(){
        // empty constructor needed
    }

    public Badge(String name, String badgeImage, int usagePoints, int bonusPoints) {
        this.name = name;
        this.badgeImage = badgeImage;
        this.usagePoints = usagePoints;
        this.bonusPoints = bonusPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBadgeImage() {
        return badgeImage;
    }

    public void setBadgeImage(String badgeImage) {
        this.badgeImage = badgeImage;
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
