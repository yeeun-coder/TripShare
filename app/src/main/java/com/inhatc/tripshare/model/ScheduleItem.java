package com.inhatc.tripshare.model;

public class ScheduleItem {
    private final int day;
    private final String time;
    private final String title;
    private final String description;
    private final double latitude;
    private final double longitude;

    public ScheduleItem(int day, String time, String title, String description,
                        double latitude, double longitude) {
        this.day = day;
        this.time = time;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
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
