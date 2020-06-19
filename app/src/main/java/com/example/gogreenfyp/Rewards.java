package com.example.gogreenfyp;

public class Rewards  {

    private String RewardName;
    private int Points;
    private int RewardImage;

    public Rewards(String rewardName, int points, int rewardImage) {
        RewardName = rewardName;
        Points = points;
        RewardImage = rewardImage;
    }

    public String getRewardName() {
        return RewardName;
    }

    public void setRewardName(String rewardName) {
        RewardName = rewardName;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public int getRewardImage() {
        return RewardImage;
    }

    public void setRewardImage(int rewardImage) {
        RewardImage = rewardImage;
    }
}
