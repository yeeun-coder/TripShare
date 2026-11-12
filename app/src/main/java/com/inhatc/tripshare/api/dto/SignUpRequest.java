package com.inhatc.tripshare.api.dto;

public class SignUpRequest {
    public String email;
    public String password;
    public String nickname;

    public SignUpRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
