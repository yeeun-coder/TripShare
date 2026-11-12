package com.inhatc.tripshare.model;

import java.util.List;

public class Trip {
    private final int id;
    private final long authorUserId;
    private final String title;
    private final String author;
    private final String authorProfileImage;
    private final String location;
    private final String startDate;
    private final String endDate;
    private final String description;
    private final String coverUrl;
    private final int imageRes;
    private final int likes;
    private final int comments;
    private final List<ScheduleItem> schedule;
    private final boolean mine;
    private final boolean saved;
    private final boolean liked;
    private final boolean followingAuthor;

    public Trip(int id, long authorUserId, String title, String author, String authorProfileImage,
                String location, String startDate, String endDate, String description,
                String coverUrl, int imageRes, int likes, int comments,
                List<ScheduleItem> schedule, boolean mine, boolean saved,
                boolean liked, boolean followingAuthor) {
        this.id = id;
        this.authorUserId = authorUserId;
        this.title = title;
        this.author = author;
        this.authorProfileImage = authorProfileImage;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.coverUrl = coverUrl;
        this.imageRes = imageRes;
        this.likes = likes;
        this.comments = comments;
        this.schedule = schedule;
        this.mine = mine;
        this.saved = saved;
        this.liked = liked;
        this.followingAuthor = followingAuthor;
    }

    public int getId() {
        return id;
    }

    public long getAuthorUserId() {
        return authorUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorProfileImage() {
        return authorProfileImage;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public List<ScheduleItem> getSchedule() {
        return schedule;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isSaved() {
        return saved;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isFollowingAuthor() {
        return followingAuthor;
    }

    public String getDateRange() {
        return startDate + " - " + endDate;
    }
}
