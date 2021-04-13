package ca.unb.mobiledev.hermes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/*
This Class serves as an Alarm receiver for the Reminder that can be set by Add note and Edit note
 */
public class AlarmReceiver extends BroadcastReceiver {

    private int notificationId = 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Creating Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ca.unb.mobiledev.hermes", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        //Launching Notification
        //Intent mainIntent = new Intent(context, (Class)intent.getExtras().get("classname"));
        Intent mainIntent = new Intent(context, AddNote.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , mainIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ca.unb.mobiledev.hermes")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Test Title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build() );

    }
}
