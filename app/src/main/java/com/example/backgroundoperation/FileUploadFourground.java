package com.example.backgroundoperation;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FileUploadFourground extends Worker {

    private static final String CHANNEL_ID = "Foreground Test Notification";
    private NotificationManager notificationManager;

    private Context context;

    public FileUploadFourground(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        setForegroundAsync(createForegroundInfo(0));

        for (int i = 1; i <= 100; i++) {
            try {
                Thread.sleep(1000);
                setForegroundAsync(createForegroundInfo(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Result.success();
    }


    @NonNull
    private ForegroundInfo createForegroundInfo(@NonNull int progress) {
        // Build a notification using bytesRead and contentLength

        Context context = getApplicationContext();
        String id = "My Notification Channel Id_1";
        String title = "Notification Progress Test";
        String cancel = "Cancel";
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title + " Upload - " + progress + " %")
                .setTicker(title)
                .setSmallIcon(R.drawable.ic_google)
                .setOngoing(true)
                .setProgress(100, progress, false)
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                .addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();

        return new ForegroundInfo(100, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "this is for Test Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
