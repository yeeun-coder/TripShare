package com.inhatc.tripshare.dto;

import java.util.List;

public class ScheduleItemResponse {
    private int dayNum;
    private String scheduleTime;
    private String placeName;
    private String description;
    private double latitude;
    private double longitude;

    public ScheduleItemResponse(int dayNum, String scheduleTime, String placeName,
                                String description, double latitude, double longitude) {
        this.dayNum = dayNum;
        this.scheduleTime = scheduleTime;
        this.placeName = placeName;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getDayNum() {
        return dayNum;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
