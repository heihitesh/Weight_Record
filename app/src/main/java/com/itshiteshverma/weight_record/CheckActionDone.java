package com.itshiteshverma.weight_record;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.dailycheck.R;

public class CheckActionDone extends Service {

    private final static String TAG = "CheckRecentPlay";
    private static Long MILLISECS_PER_DAY = 86L;

    private static long delay = 129600000; //172800000;                 // for 1.5 day (for testing)
      //  private static long delay = MILLISECS_PER_DAY;   // 1 day
    NotificationManager nm;
    EditText frq;







    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "Service started");
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);



        // Are notifications enabled?
        if (settings.getBoolean("enabled", true)) {
            // And was action not recently done?
            Long lastTimeDone = settings.getLong("lastTimeActionDone", 0);
            if ((System.currentTimeMillis() - lastTimeDone) >= delay) {
                sendNotification();
            } else {
                Log.i(TAG, "Action recently done");
            }
        } else {
            Log.i(TAG, "Notifications are disabled");
        }

        // Set an alarm for the next time this service should run:
        setAlarm();

        Log.v(TAG, "Service stopped");
        stopSelf();
    }




    public void setAlarm() {

        Intent serviceIntent = new Intent(this, CheckActionDone.class);
        PendingIntent pi = PendingIntent.getService(this, 1, serviceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        Log.v(TAG, "Alarm set");
    }

    public void sendNotification() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        @SuppressWarnings("deprecation")
        Notification noti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 1, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Action Not Do")
                .setContentText("You didn't do the daily action.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.save)
                .setTicker("You haven;t done the daily action; please do it now.")
                .setWhen(System.currentTimeMillis())
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, noti);

        Log.v(TAG, "Notification sent");

    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}