package com.inhatc.tripshare.api.dto;

import java.util.List;

public class TripUpdateRequest {
    public String title;
    public String destination;
    public String startDate;
    public String endDate;
    public String description;
    public String coverUrl;
    public List<ScheduleItemRequest> scheduleItems;

    public TripUpdateRequest(String title, String destination, String startDate, String endDate,
                             String description, String coverUrl,
                             List<ScheduleItemRequest> scheduleItems) {
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.coverUrl = coverUrl;
        this.scheduleItems = scheduleItems;
    }
}
