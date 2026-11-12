package com.inhatc.tripshare.dto;

public class ReviewResponse {
    private Long id;
    private String author;
    private String content;
    private int rating;
    private String createdAt;

    public ReviewResponse(Long id, String author, String content, int rating, String createdAt) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
