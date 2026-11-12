package com.inhatc.tripshare.dto;

public class UserResponse {
    private Long id;
    private String nickname;
    private String email;
    private String bio;
    private String profileImage;
    private long followerCount;
    private long followingCount;

    public UserResponse(Long id, String nickname, String email, String bio, String profileImage,
                        long followerCount, long followingCount) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.bio = bio;
        this.profileImage = profileImage;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public Long getId() {
        return id;
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

    public String getProfileImage() {
        return profileImage;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }
}
