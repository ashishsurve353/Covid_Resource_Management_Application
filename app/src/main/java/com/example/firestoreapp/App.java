package com.example.firestoreapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class App extends Application {
    public static final String CHANNEL_1_ID = "Notification for Appointment";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels()
    {

        //  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        // {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "Channel1",
                NotificationManager.IMPORTANCE_HIGH
        );

        channel1.setDescription("This is channel");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);

        //  }


    }
}

