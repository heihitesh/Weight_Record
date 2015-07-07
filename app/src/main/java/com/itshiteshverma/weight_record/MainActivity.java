package com.itshiteshverma.weight_record;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {
    Button save, view;
    EditText weight;
    TextView CurrentWeight, PastWeight, ReducedWeight, RelativeWeight, Date, DateDiffforPast;
    TextView IntialWeightDiff, CurrentWeightDiff;
    SharedPreferences someData, DateDiff;
    public static String filename = "MySharedString";  //static means that the file name will not change
    String relativeweight;
    int DateofStart = 167;  // 16 June 2015
    NotificationManager nm;



    Button Settings;


    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";

    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intilize();
        loadCurrentWeight();
        loadCurrentTime();

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


    private void loadCurrentTime() {
        DateDiff = getSharedPreferences(filename, 0);
        int getdate = someData.getInt("Date", 00);

        //getting current time
        Calendar cal = Calendar.getInstance();

        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);

        //subtracting time form the time when we pressed the save option

        int datediff = dayofyear - getdate;
        String showdate = String.valueOf(datediff);
        Date.setText(showdate);
        String Datediffformpast = String.valueOf(getdate - DateofStart);
        DateDiffforPast.setText(Datediffformpast);


    }

    private void loadCurrentWeight() {
        someData = getSharedPreferences(filename, 0);
        String dataReturn = someData.getString("sharedString", "Could not Load ");
        //first param is the open the string form the putString (Line no -58 )
        //and the second param is the default string . when it is unable to find sharedString

        CurrentWeight.setText(dataReturn + "\nKg");
        relativeweight = dataReturn;
    }

    private void intilize() {
        save = (Button) findViewById(R.id.bSave);
        view = (Button) findViewById(R.id.bView);
        weight = (EditText) findViewById(R.id.etWeight);
        CurrentWeight = (TextView) findViewById(R.id.tvShowCurrentWeight);
        PastWeight = (TextView) findViewById(R.id.tvShowPastWeight);
        RelativeWeight = (TextView) findViewById(R.id.tvRelativeWeight);
        ReducedWeight = (TextView) findViewById(R.id.tvReducedWeigth);
        IntialWeightDiff = (TextView) findViewById(R.id.tvIntialWeightDiff);
        CurrentWeightDiff = (TextView) findViewById(R.id.tvCurrentWeightDiff);


        Date = (TextView) findViewById(R.id.tvDate);
        DateDiffforPast = (TextView) findViewById(R.id.tvDate2);


        Settings = (Button) findViewById(R.id.bsetting);
        Settings.setOnClickListener(this);
        save.setOnClickListener(this);
        view.setOnClickListener(this);
        weight.setOnEditorActionListener(this);
        //if we want that when we click the enter button in the keyboard it will click the go button or load the page
        someData = getSharedPreferences(filename, 0);
        //second param is the default loader

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSave:
                try {
                    String si = weight.getText().toString();


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //this will hide our keyboard
                    imm.hideSoftInputFromWindow(weight.getWindowToken(), 0);//first param weight and get the WIndowToken
                    //Secnd param is default

                    boolean didItWorked = true;

                    //THIS WILL GET THE CURRENT TIME OF THE SYSTEM
                    DateFormat df = new SimpleDateFormat("--> EEE, MMM d, ''yy");
                    String date = df.format(Calendar.getInstance().getTime());

                    try {
                        String name = weight.getText().toString() + "Kg"; //getting the text or data
                        WeightDataBase entry = new WeightDataBase(MainActivity.this); //passing this class context to HotOrNot,java
                        entry.open();
                        entry.createEntry(name, date);
                        entry.close();


                    } catch (Exception e) {
                        didItWorked = false;
                        //Making a Dialog Box
                        Dialog d = new Dialog(this);
                        d.setTitle("Exception Caght");

                        String error = e.toString();

                        TextView tv = new TextView(this);
                        tv.setText(error);
                        d.setContentView(tv);
                        d.show();
                    } finally {
                        if (didItWorked) {
                            Intent show = new Intent("com.itshiteshverma.weight_record.Show_Dialoge");
                            startActivity(show);
                        } else {
                            Toast.makeText(this, "NOT STORED TRY AGAIN !!!! ", Toast.LENGTH_SHORT).show();
                        }
                    }


                    //FOr theText VIew
                    // CurrentWeight.setText(weight.getText().toString() + " Kg");


                    //This if for the Reduced Weight
                    float pastw = (float) 115.6;  // value written on the past weight
                    float currW = Float.parseFloat(String.valueOf(weight.getText()));
                    float diffWeight = pastw - currW;
                    String diff = String.format("%.1f%n", diffWeight);  // this will get the float to a limit decimal
                    String hi = diff + "Kg";

                    IntialWeightDiff.setVisibility(View.VISIBLE);
                    ReducedWeight.setVisibility(View.VISIBLE);
                    ReducedWeight.setText(hi);


                    //for the Shared Preference //to relative weight

                    try {
                        //getting the data written on the edittext and storing it in string
                        String stringData = weight.getText().toString();

                        //saving the string
                        SharedPreferences.Editor editor = someData.edit();
                        editor.putString("sharedString", stringData);

                        Calendar cal = Calendar.getInstance();

                        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
                        editor.putInt("Date", dayofyear);


                        editor.commit(); //finilizing the editor
                    } catch (Exception e) {
                        Toast.makeText(this, "Please Enter a Valid Value", Toast.LENGTH_SHORT).show();
                    }

                    // calcluating the relative Weight
                    float actualW = Float.parseFloat(relativeweight);
                    float curr = Float.parseFloat(weight.getText().toString());
                    float dif = actualW - curr;
                    String diff3 = String.format("%.1f%n", dif);
                    String relative = String.valueOf(diff3);
                    CurrentWeightDiff.setVisibility(View.VISIBLE);

                    if (dif >= 0) {   //if its positive
                        RelativeWeight.setText(relative + "\nKg");
                        RelativeWeight.setVisibility(View.VISIBLE);
                    } else {
                        RelativeWeight.setText(relative + "\nKg");
                        ;
                        RelativeWeight.setBackgroundColor(Color.RED);
                        RelativeWeight.setVisibility(View.VISIBLE);
                    }
                    // this is for the Notification
                    Log.v(TAG, "Recording time action done");
                    editor.putLong("lastTimeActionDone", System.currentTimeMillis());
                    editor.commit();

                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.cancel(1); //this will dissaper the notification when we click on it

                }catch (Exception e){
                    Toast.makeText(this, e.toString(),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bView:

                Intent i2 = new Intent("com.itshiteshverma.weight_record.WeightDataBaseView");
                startActivity(i2);


                break;


            case R.id.bsetting:

                Intent in = new Intent("com.itshiteshverma.weight_record.Settings");
                startActivity(in);

                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        TextView hit = (TextView) v;
        if (actionId == EditorInfo.IME_ACTION_DONE) {


            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //this will hide our keyboard
            imm.hideSoftInputFromWindow(weight.getWindowToken(), 0);//first param weight and get the WIndowToken
            //Secnd param is default

            boolean didItWorked = true;

            //THIS WILL GET THE CURRENT TIME OF THE SYSTEM
            DateFormat df = new SimpleDateFormat("--> EEE, MMM d, ''yy");
            String date = df.format(Calendar.getInstance().getTime());

            try {
                String name = weight.getText().toString() + "Kg"; //getting the text or data
                WeightDataBase entry = new WeightDataBase(MainActivity.this); //passing this class context to HotOrNot,java
                entry.open();
                entry.createEntry(name, date);
                entry.close();


            } catch (Exception e) {
                didItWorked = false;
                //Making a Dialog Box
                Dialog d = new Dialog(this);
                d.setTitle("Exception Caght");

                String error = e.toString();

                TextView tv = new TextView(this);
                tv.setText(error);
                d.setContentView(tv);
                d.show();
            } finally {
                if (didItWorked) {
                    Toast.makeText(this, "Weight Stored :" + weight.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "NOT STORED TRY AGAIN !!!! ", Toast.LENGTH_SHORT).show();
                }
            }


            //FOr theText VIew
            // CurrentWeight.setText(weight.getText().toString() + " Kg");


            //This if for the Reduced Weight
            float pastw = (float) 115.6;  // value written on the past weight
            float currW = Float.parseFloat(String.valueOf(weight.getText()));
            float diffWeight = pastw - currW;
            String diff = String.format("%.1f%n", diffWeight);  // this will get the float to a limit decimal
            String hi = diff + "Kg";


            ReducedWeight.setVisibility(View.VISIBLE);
            ReducedWeight.setText(hi);


            //for the Shared Preference //to relative weight

            try {
                //getting the data written on the edittext and storing it in string
                String stringData = weight.getText().toString();

                //saving the string
                SharedPreferences.Editor editor = someData.edit();
                editor.putString("sharedString", stringData);

                Calendar cal = Calendar.getInstance();

                int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
                editor.putInt("Date", dayofyear);


                editor.commit(); //finilizing the editor
            } catch (Exception e) {
                Toast.makeText(this, "Please Enter a Valid Value", Toast.LENGTH_SHORT).show();
            }

            // calcluating the relative Weight
            float actualW = Float.parseFloat(relativeweight);
            float curr = Float.parseFloat(weight.getText().toString());
            float dif = actualW - curr;
            String relative = String.valueOf(dif);

            if (dif >= 0) {   //if its positive
                RelativeWeight.setText(relative + " Kg");
                RelativeWeight.setVisibility(View.VISIBLE);
            } else {
                RelativeWeight.setText(relative + " Kg");
                ;
                RelativeWeight.setBackgroundColor(Color.RED);
                RelativeWeight.setVisibility(View.VISIBLE);
            }
            // this is for the Notification
            Log.v(TAG, "Recording time action done");
            editor.putLong("lastTimeActionDone", System.currentTimeMillis());
            editor.commit();

            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(1); //this will dissaper the notification when we click on it

        }
        return false;
    }

    @Override
    //This method gona a start the Menu options when menu or setting button is clicked  ..cool_menu.xml
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        //delete the return form the super
        super.onCreateOptionsMenu(menu);
        MenuInflater blowUp = getMenuInflater();
        blowUp.inflate(R.menu.cool_menu, menu);
        return true;
        //it will return true because it hava boolean return type
    }

    @Override
    //this
    public boolean onOptionsItemSelected(MenuItem item) {
        //no need to return the super value
        switch (item.getItemId()) {
            case R.id.aboutUS:
                Intent i = new Intent("com.itshiteshverma.weight_record.About");
                startActivity(i);
                break;


            case R.id.exit:
                Intent e = new Intent("com.itshiteshverma.travis01.Exit");
                startActivity(e);
                //finish(); //close our app
                break;
            case R.id.dexit:
                Toast.makeText(this, "GOOD BYE !!", Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        return false;//if none of them a choosen it will return false
    }


}
