package com.itshiteshverma.weight_record;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Settings extends Activity implements CompoundButton.OnCheckedChangeListener {


    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";

    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;
    NotificationManager nm;
    Switch Noti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Noti = (Switch) findViewById(R.id.notiswitch);

//this sharedPref is to get the switch


        Noti.setOnCheckedChangeListener(this);
        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = settings.edit();

        // First time running app?
        if (!settings.contains("lastTimeActionDone"))
            enableNotification(null);

        Log.v(TAG, "Starting CheckRecentRun service...");
        startService(new Intent(this, CheckActionDone.class));

    }

    public void enableNotification(View v) {
        editor.putBoolean("enabled", true);
        editor.commit();
        Log.v(TAG, "Notifications enabled");
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        //commit prefs on change
        editor.putBoolean("your_key", isChecked);
        editor.commit();

        if (isChecked) {
            // The toggle is enabled
            editor.putBoolean("enabled", true);
            editor.commit();
            Log.v(TAG, "Notifications enabled");
            Toast.makeText(this, "Notification Enabled", Toast.LENGTH_SHORT).show();
        } else {
            // The toggle is disabled
            editor.putBoolean("enabled", false);
            editor.commit();
            Log.v(TAG, "Notifications disabled");
            Toast.makeText(this, "Notification Disabled", Toast.LENGTH_SHORT).show();

        }

    }
}