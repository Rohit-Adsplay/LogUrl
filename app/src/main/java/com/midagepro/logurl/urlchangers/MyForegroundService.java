package com.midagepro.logurl.urlchangers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.midagepro.logurl.MainActivity;
import com.midagepro.logurl.R;

public class MyForegroundService extends Service {

    private static final String TAG = "MyForegroundService";
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String URL_TO_LAUNCH = "https://www.pastebin.com"; // URL to launch in Chrome

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Foreground service started");

        // Create notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create notification to be displayed as foreground service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service")
                .setContentText("Observing URL")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(getPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = builder.build();
        startForeground(1, notification); // Start the foreground service with the notification
        go("",this);

        // Return START_STICKY to indicate that the service should be restarted if it's killed by the system
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Foreground service stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Helper method to create a pending intent for notification click
    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(this, MainActivity.class); // Replace MainActivity with your desired activity class
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, notificationIntent, 0);
    }

    public void go(String capturedURLBrowser, Context context){
        Log.d("TAGGGGG", capturedURLBrowser);
        if (capturedURLBrowser.contains("adsplay")) {
            Intent chromeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TO_LAUNCH));
            chromeIntent.setPackage("com.android.chrome"); // Specify the package name for Chrome
            chromeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Set the flag to start as a new task
            context.startActivity(chromeIntent);
        }
    }
}

