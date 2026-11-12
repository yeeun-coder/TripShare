package com.inhatc.tripshare.api.dto;

import java.util.List;

public class TripDto {
    public Long id;
    public String title;
    public String author;
    public Long authorUserId;
    public String authorProfileImage;
    public String destination;
    public String startDate;
    public String endDate;
    public String description;
    public String coverUrl;
    public int likeCount;
    public int reviewCount;
    public boolean mine;
    public boolean saved;
    public boolean liked;
    public boolean followingAuthor;
    public List<ScheduleItemDto> scheduleItems;
}
