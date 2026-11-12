package com.inhatc.tripshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class TripCreateRequest {

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

    @NotNull
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ScheduleItemRequest> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(List<ScheduleItemRequest> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
}
