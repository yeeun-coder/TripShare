package com.inhatc.tripshare.api.dto;

public class UserUpdateRequest {
    public String nickname;
    public String bio;
    public String profileImage;

    public UserUpdateRequest(String nickname, String bio, String profileImage) {
        this.nickname = nickname;
        this.bio = bio;
        this.profileImage = profileImage;
    }
}
