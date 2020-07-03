package com.example.gogreenfyp;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class User {
    private String userID;
    private String username;
    private String email;
    private int walletBalance;
    private int pointsBalance;
    private int badgeProgress;
    private String walletAddress;
    List<String> userBadges;
    List<String> userRewards;
    List<String> userRedeemedRewards;

    public User(){

    }

    public User(String userID, String username, String email, int walletBalance, int pointsBalance, int badgeProgress, String walletAddress, List<String> userBadges,  List<String> userRewards, List<String> userRedeemedRewards  ) {
        this.username = username;
        this.userID = userID;
        this.email = email;
        this.walletBalance = walletBalance;
        this.pointsBalance = pointsBalance;
        this.badgeProgress = badgeProgress;
        this.walletAddress = walletAddress;
        this.userBadges = userBadges;
        this.userRewards = userRewards;
        this.userRedeemedRewards = userRedeemedRewards;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        this.walletBalance = walletBalance;
    }

    public int getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    public int getBadgeProgress() {
        return badgeProgress;
    }

    public void setBadgeProgress(int badgeProgress) {
        this.badgeProgress = badgeProgress;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public List<String> getUserBadges() {
        return userBadges;
    }

    public void setUserBadges(List<String> tags) {
        this.userBadges = tags;
    }

    public List<String> getUserRewards() {
        return userRewards;
    }

    public void setUserRewards(List<String> userRewards) {
        this.userRewards = userRewards;
    }

    public List<String> getUserRedeemedRewards() {
        return userRedeemedRewards;
    }

    public void setUserRedeemedRewards(List<String> userRedeemedRewards) {
        this.userRedeemedRewards = userRedeemedRewards;
    }
}
