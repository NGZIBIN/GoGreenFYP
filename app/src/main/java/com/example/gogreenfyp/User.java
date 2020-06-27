package com.example.gogreenfyp;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class User {
    private String username;
    private String email;
    private int walletBalance;
    private int pointsBalance;
    private int badgeProgress;
    private int walletAddress;
    List<String> userBadges;

    public User(){

    }

    public User(String username, String email, int walletBalance, int pointsBalance, int badgeProgress, int walletAddress, List<String> userBadges) {
        this.username = username;
        this.email = email;
        this.walletBalance = walletBalance;
        this.pointsBalance = pointsBalance;
        this.badgeProgress = badgeProgress;
        this.walletAddress = walletAddress;
        this.userBadges = userBadges;
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

    public int getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(int walletAddress) {
        this.walletAddress = walletAddress;
    }

    public List<String> getUserBadges() {
        return userBadges;
    }

    public void setUserBadges(List<String> tags) {
        this.userBadges = tags;
    }
}
