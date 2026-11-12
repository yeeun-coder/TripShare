package com.inhatc.tripshare.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class TripUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String destination;

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

    private String description;

    private String coverUrl;

    private List<ScheduleItemRequest> scheduleItems;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<ScheduleItemRequest> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(List<ScheduleItemRequest> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
}
