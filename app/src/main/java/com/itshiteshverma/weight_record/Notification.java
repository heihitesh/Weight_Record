package com.itshiteshverma.weight_record;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//import com.example.dailycheck.R;

public class Notification extends Activity {

    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";

    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifiaction);

        // Save time of run:
        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = settings.edit();

        // First time running app?
        if (!settings.contains("lastTimeActionDone"))
            enableNotification(null);

        Log.v(TAG, "Starting CheckRecentRun service...");
        startService(new Intent(this, CheckActionDone.class));
    }

    public void doAction(View v) {
        Log.v(TAG, "Recording time action done");
        editor.putLong("lastTimeActionDone", System.currentTimeMillis());
        editor.commit();
    }

    public void enableNotification(View v) {
        editor.putBoolean("enabled", true);
        editor.commit();
        Log.v(TAG, "Notifications enabled");
    }

    public void disableNotification(View v) {
        editor.putBoolean("enabled", false);
        editor.commit();
        Log.v(TAG, "Notifications disabled");
    }
}