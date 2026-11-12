package com.inhatc.tripshare.dto;

public class UserSummaryResponse {
    private Long id;
    private String nickname;
    private String profileImage;

    public UserSummaryResponse(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
