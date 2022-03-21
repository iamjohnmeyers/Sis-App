package com.iamjohnmeyers.sisapp;

import static java.lang.Boolean.*;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import java.time.*;
import java.time.temporal.ChronoUnit;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    FloatingActionButton btnTimePicker, settings;
    private int mHour, mMinute;
    int setHour = 6, setMinuteI = 0;
    int setHour24HR = 18;
    String setMinute = "00";
    TextView currentText;
    String AMorPM = "PM";
    Boolean backgroundFlash = TRUE;
    Runnable flashRunnable, timeRunnable;
    Handler flashHandler = new Handler(), timeHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTimePicker = (FloatingActionButton) findViewById(R.id.clock);
        settings = (FloatingActionButton) findViewById(R.id.set);
        btnTimePicker.setOnClickListener(this);
        currentText = (TextView) findViewById(R.id.message);
        settings.setOnClickListener(this);
        currentText.setText("TABLET NEEDS TO BE CONFIGURED. CALL BEN (xxx) xxx-xxxx");

    }

    public void flashColors(){
        //change to red
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));

        Handler orange = new Handler();
        orange.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.orange, null));
            }
        }, 1000);

        Handler yellow = new Handler();
        yellow.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
            }
        }, 2000);

        Handler green = new Handler();
        green.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
            }
        }, 3000);

        Handler blue = new Handler();
        blue.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
            }
        }, 4000);

        Handler indigo = new Handler();
        indigo.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.indigo, null));
            }
        }, 5000);

        Handler violet = new Handler();
        violet.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.violet, null));
            }
        }, 6000);

        Handler white = new Handler();
        white.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                constraintLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
            }
        }, 7000);
    }

    public void set(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String careGiver = pref.getString("caregiver_pref", "BEN").toUpperCase();
        String tabletNum = pref.getString("day_or_night_pref", "Day time Tablet");
        if (tabletNum.equals("1")){
            currentText.setText("DRINK " + getDay(0) +" NIGHT AFTER " + careGiver + " LEAVES, AND BEFORE BEDTIME!");
        }
        if (tabletNum.equals("0")) {
            currentText.setText("DRINK " + getDay(1) +" DURING THE DAY BEFORE " + careGiver + " ARRIVES AT " + setHour + ":" + setMinute + AMorPM);
        }
    }

    public void updateMessage(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        //System.out.println("System:" + mHour + " " + mMinute);
        //System.out.println("User:" + setHour24HR + " " + setMinuteI);

        LocalTime currentTime = LocalTime.of(mHour, mMinute);
        LocalTime updateTime = LocalTime.of(setHour24HR, setMinuteI);

        long hours = ChronoUnit.HOURS.between(updateTime, currentTime);
        long minutes = ChronoUnit.MINUTES.between(updateTime, currentTime) % 60;

        System.out.println("hours" + hours);
        System.out.println("minutes" + minutes);

        if (hours == 0 && minutes == 0){
            set();
        }
    }

    public void onPause(){
        super.onPause();
        flashHandler.removeCallbacks(flashRunnable);
        timeHandler.removeCallbacks(timeRunnable);
    }

    public void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        backgroundFlash = pref.getBoolean("flash_background", TRUE);
        String flashInterval = pref.getString("flash_interval", "30");
        long flashIntervalMilli = Math.abs(Integer.parseInt(flashInterval)) * 60000;
        set();
        if (backgroundFlash){
            //create runnable for delay
            flashRunnable = new Runnable() {
                @Override
                public void run() {
                    flashHandler.postDelayed(this, flashIntervalMilli);
                    flashColors();
                }
            };
            flashRunnable.run();
        }

        timeRunnable = new Runnable() {
            @Override
            public void run() {
                timeHandler.postDelayed(this, 5000);
                updateMessage();
            }
        };
        timeRunnable.run();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String careGiver = pref.getString("caregiver_pref", "BEN").toUpperCase();
        String tabletNum = pref.getString("day_or_night_pref", "Day time Tablet");
        if(v == settings) {
            Intent switchActivityIntent = new Intent(this, SettingsActivity.class);
            startActivity(switchActivityIntent);
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            setHour24HR = hourOfDay;
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                AMorPM = "AM";
                            } else if (hourOfDay == 12) {
                                AMorPM = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                AMorPM = "PM";
                            } else {
                                AMorPM = "AM";
                            }
                            setHour = hourOfDay;
                            if (minute <= 9){
                                setMinute = "0" + Integer.toString(minute);
                                setMinuteI = minute;
                            } else {
                                setMinute = Integer.toString(minute);
                                setMinuteI = minute;
                            }
                            if(tabletNum.equals("0")){
                                currentText.setText("DRINK " + getDay(1) +" DURING THE DAY BEFORE " + careGiver + " ARRIVES AT " + setHour + ":" + setMinute + AMorPM);
                            }
                            Toast.makeText(MainActivity.this, "Time Set To: " + setHour + ":" + setMinute + AMorPM, Toast.LENGTH_SHORT).show();
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDay (int i) {
        int day = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_WEEK);
            day = day + i;
        } else {
            System.exit(1);
        }
        if (day == 8){
            day = 1;
        }
        System.out.println(day);
        String dayOfWeek;
        switch (day) {
            case 1:
                dayOfWeek = "SUNDAY";
                break;
            case 2:
                dayOfWeek = "MONDAY";
                break;
            case 3:
                dayOfWeek = "TUESDAY";
                break;
            case 4:
                dayOfWeek = "WEDNESDAY";
                break;
            case 5:
                dayOfWeek = "THURSDAY";
                break;
            case 6:
                dayOfWeek = "FRIDAY";
                break;
            case 7:
                dayOfWeek = "SATURDAY";
                break;
            default:
                dayOfWeek = "NOT A VALID DATE";
        }

        return dayOfWeek;
    }

}