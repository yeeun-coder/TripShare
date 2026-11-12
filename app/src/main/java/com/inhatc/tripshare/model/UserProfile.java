package com.inhatc.tripshare.model;

public class UserProfile {
    private final String nickname;
    private final String email;
    private final String bio;
    private final int createdCount;
    private final int savedCount;
    private final int likesReceived;
    private final long followerCount;
    private final long followingCount;

    public UserProfile(String nickname, String email, String bio,
                       int createdCount, int savedCount, int likesReceived,
                       long followerCount, long followingCount) {
        this.nickname = nickname;
        this.email = email;
        this.bio = bio;
        this.createdCount = createdCount;
        this.savedCount = savedCount;
        this.likesReceived = likesReceived;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public int getCreatedCount() {
        return createdCount;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public int getLikesReceived() {
        return likesReceived;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }
}
