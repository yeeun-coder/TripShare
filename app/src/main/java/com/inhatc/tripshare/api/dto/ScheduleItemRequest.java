package com.inhatc.tripshare.api.dto;

public class ScheduleItemRequest {
    public int dayNum;
    public String scheduleTime;
    public String placeName;
    public String description;
    public double latitude;
    public double longitude;

    public ScheduleItemRequest(int dayNum, String scheduleTime, String placeName,
                               String description, double latitude, double longitude) {
        this.dayNum = dayNum;
        this.scheduleTime = scheduleTime;
        this.placeName = placeName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
