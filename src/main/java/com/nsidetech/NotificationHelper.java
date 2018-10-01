package com.nsidetech;

import java.time.LocalDateTime;
import java.util.Date;

class NotificationHelper {
    private static NotificationHelper INSTANCE;

    static NotificationHelper getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new NotificationHelper();
        }
        return INSTANCE;
    }

    void sendMessage(String asset, String message, LocalDateTime time) {
        System.out.format("%s, %s, %s\n", asset, message, time);
    }
}