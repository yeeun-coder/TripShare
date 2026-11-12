package com.inhatc.tripshare.model;

public class Review {
    private final int id;
    private final int tripId;
    private final String author;
    private final String content;
    private final String date;
    private final int rating;

    public Review(int id, int tripId, String author, String content, String date, int rating) {
        this.id = id;
        this.tripId = tripId;
        this.author = author;
        this.content = content;
        this.date = date;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getTripId() {
        return tripId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }
}
