package com.inhatc.tripshare.api.dto;

public class ReviewCreateRequest {
    public Long userId;
    public String content;
    public int rating;

    public ReviewCreateRequest(Long userId, String content, int rating) {
        this.userId = userId;
        this.content = content;
        this.rating = rating;
    }
}
