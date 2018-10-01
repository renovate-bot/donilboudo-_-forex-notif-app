package com.nsidetech;

import java.time.LocalDateTime;

public class Alerte {
    private String asset;
    private LocalDateTime time;
    private int timefreame;
    private String message;

    public Alerte(String asset, LocalDateTime time, int timeframe, String message) {
        this.asset = asset;
        this.time = time;
        this.timefreame = timeframe;
        this.message = message;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getTimefreame() {
        return timefreame;
    }

    public void setTimefreame(int timefreame) {
        this.timefreame = timefreame;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}