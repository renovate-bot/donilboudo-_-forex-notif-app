package com.nsidetech;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;

class NotificationHelper {
    private static NotificationHelper INSTANCE;

    private NotificationHelper() {
        init();
    }

    static NotificationHelper getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new NotificationHelper();
        }
        return INSTANCE;
    }

    private void init() {
        try
        {
            URL url = NotificationHelper.class.getResource("serviceAccountKey.json");
            FileInputStream serviceAccount = new FileInputStream(url.getPath());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://forexnotificationsapp-c34e7.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void sendMessage(String asset, String message, LocalDateTime time) {
        // The topic name can be optionally prefixed with "/topics/".
        String topic = "news";

        // See documentation on defining a message payload.
        Message messageObject = Message.builder()
                .putData("message", message)
                .setTopic(topic)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = null;
        try
        {
            response = FirebaseMessaging.getInstance().send(messageObject);
        }
        catch (FirebaseMessagingException e)
        {
            e.printStackTrace();
        }
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }
}