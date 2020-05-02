package com.tariqaliiman.tariqaliiman.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.SetAlarmActivity;
import com.tariqaliiman.tariqaliiman.scheduler.RamadanAlarmReceiver;
import com.tariqaliiman.tariqaliiman.scheduler.SalaatAlarmReceiver;
import com.tariqaliiman.tariqaliiman.utils.AppSettings;
import com.tariqaliiman.tariqaliiman.utils.PrayTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;

public class SalaatTimesFragment extends Fragment implements Constants {
    private static boolean sIsAlarmInit = false;
    int mIndex = 0;
    Location mLastLocation;
    TextView mAlarm;
    View mRamadanContainer;

    public static SalaatTimesFragment newInstance(int index, Location location) {
        SalaatTimesFragment fragment = new SalaatTimesFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_ALARM_INDEX, index);
        args.putParcelable(EXTRA_LAST_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public SalaatTimesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(EXTRA_ALARM_INDEX);
            mLastLocation = (Location) getArguments().getParcelable(EXTRA_LAST_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salaat_times, container, false);
        init(view);
        return view;
    }

    protected void init(View view) {
        // In future releases we will add more cards.
        // Then we'll need to do this for each card.
        // For now it's included in the layout which
        // makes it easier to work with the layout editor.
        // inflater.inflate(R.layout.view_prayer_times, timesContainer, true);

        if (mLastLocation == null) {
            return;
        }


        //Toolbar will now take on default Action Bar characteristics
        LinkedHashMap<String, String> prayerTimes =
                PrayTime.getPrayerTimes(getActivity(), mIndex, mLastLocation.getLatitude(), mLastLocation.getLongitude());

        TextView title = (TextView) view.findViewById(R.id.card_title);
        title.setText(TimeZone.getDefault().getID());

        TextView fajr = (TextView) view.findViewById(R.id.fajr);
        TextView dhuhr = (TextView) view.findViewById(R.id.dhuhr);
        TextView asr = (TextView) view.findViewById(R.id.asr);
        TextView maghrib = (TextView) view.findViewById(R.id.maghrib);
        TextView isha = (TextView) view.findViewById(R.id.isha);
        ////////////////
        TextView fajr2 = (TextView) view.findViewById(R.id.fajr2);
        TextView dhuhr2 = (TextView) view.findViewById(R.id.dhuhr2);
        TextView asr2 = (TextView) view.findViewById(R.id.asr2);
        TextView maghrib2 = (TextView) view.findViewById(R.id.maghrib2);
        TextView isha2 = (TextView) view.findViewById(R.id.isha2);
        ////////////////
        TextView sunrise = (TextView) view.findViewById(R.id.sunrise);
        TextView sunset = (TextView) view.findViewById(R.id.sunset);
        mAlarm = (TextView) view.findViewById(R.id.alarm);
        mRamadanContainer = view.findViewById(R.id.ramadan_container);
        TextView fajr4 = view.findViewById(R.id.fajr4);
        TextView dhuhr6 = view.findViewById(R.id.dhuhr6);
        TextView asr9 = view.findViewById(R.id.asr9);
        TextView maghrib11 = view.findViewById(R.id.maghrib11);
        TextView isha13 = view.findViewById(R.id.isha13);

        fajr.setText(prayerTimes.get(String.valueOf(fajr.getTag())));
        dhuhr.setText(prayerTimes.get(String.valueOf(dhuhr.getTag())));
        asr.setText(prayerTimes.get(String.valueOf(asr.getTag())));
        maghrib.setText(prayerTimes.get(String.valueOf(maghrib.getTag())));
        isha.setText(prayerTimes.get(String.valueOf(isha.getTag())));
        sunrise.setText(prayerTimes.get(String.valueOf(sunrise.getTag())));
        sunset.setText(prayerTimes.get(String.valueOf(sunset.getTag())));

        String fajrV = prayerTimes.get(String.valueOf(fajr.getTag()));
        assert fajrV != null;
        String t = getPearerTime(fajrV);
        Log.d(Constants.log + "t", "t = " + t);
        String[] ts = t.split(":", 2);
        Log.d(Constants.log + "ts[0]", "ts[0] = " + ts[0]);
        Log.d(Constants.log + "ts[1]", "ts[1] = " + ts[1]);
        if (Integer.parseInt(ts[1]) < 0)
            fajr4.setText(getString(R.string.elapsedtime));
        else
            fajr4.setText(getString(R.string.remainingtime));
        fajr2.setText(t);
        ///////////////////
        String dhuhrV = prayerTimes.get(String.valueOf(dhuhr.getTag()));
        assert dhuhrV != null;
        String t1 = getPearerTime(dhuhrV);
        String[] ts1 = t1.split(":", 2);
        if (Integer.parseInt(ts1[1]) < 0)
            dhuhr6.setText(getString(R.string.elapsedtime));
        else
            dhuhr6.setText(getString(R.string.remainingtime));
        dhuhr2.setText(t1);
        /////////////////////
        String asrV = prayerTimes.get(String.valueOf(asr.getTag()));
        assert asrV != null;
        String t2 = getPearerTime(asrV);
        String[] ts2 = t2.split(":", 2);
        if (Integer.parseInt(ts2[1]) < 0)
            asr9.setText(getString(R.string.elapsedtime));
        else
            asr9.setText(getString(R.string.remainingtime));
        asr2.setText(t2);
        /////////////////////
        String maghribV = prayerTimes.get(String.valueOf(maghrib.getTag()));
        assert maghribV != null;
        String t3 = getPearerTime(maghribV);
        String[] ts3 = t3.split(":", 2);
        if (Integer.parseInt(ts3[1]) < 0)
            maghrib11.setText(getString(R.string.elapsedtime));
        else
            maghrib11.setText(getString(R.string.remainingtime));
        maghrib2.setText(t3);
        /////////////////////
        String ishaV = prayerTimes.get(String.valueOf(isha.getTag()));
        assert ishaV != null;
        String t4 = getPearerTime(ishaV);
        String[] ts4 = t4.split(":", 2);
        if (Integer.parseInt(ts4[1]) < 0)
            isha13.setText(getString(R.string.elapsedtime));
        else
            isha13.setText(getString(R.string.remainingtime));
        isha2.setText(t4);
        /////////////////////

        //set text for the first card.
        setAlarmButtonText(mAlarm, mIndex);
        setAlarmButtonClickListener(mAlarm, mIndex);

        if (!sIsAlarmInit) {
            if (AppSettings.getInstance().isDefaultSet()) {
                AppSettings.getInstance().setLatFor(mIndex, mLastLocation.getLatitude());
                AppSettings.getInstance().setLngFor(mIndex, mLastLocation.getLongitude());
                updateAlarmStatus();
                sIsAlarmInit = true;
            }
        }
    }

    /*private String getPearerTime(String timeV) {
        int hours = 0;
        int mins = 0;
        String[] ampm = timeV.split(" ", 2);
        String[] time1 = ampm[0].split(":", 2);
        if (time1[0].length() == 1) {
            timeV = "0" + timeV.toUpperCase(Locale.getDefault());
        } else {
            timeV = timeV.toUpperCase(Locale.getDefault());
        }

        Date date1 = new Date(mLastLocation.getTime());
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            //todo pattern "hh:mm a" == 06:00 pm >> 12 hours
            @SuppressLint("SimpleDateFormat") SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            //todo pattern "HH:mm" == 18:00 >> 24 hours
            Date date24 = null;
            date24 = parseFormat.parse(timeV);

            assert date24 != null;
            String time24 = displayFormat.format(date24);
            *//*java.text.DateFormat dateFormat = DateFormat.getDateFormat(getActivity());

            Date date24 = dateFormat.parse(timeV);
            assert date24 != null;
            String time24 = dateFormat.format(date24);*//*

            //    String[] ampm = result.split(" ", 2);
            String[] time = time24.split(":", 2);
            Log.d(Constants.log, "time0 = " + time[0] + " time1 = " + time[1]);
            String minutes = time[1].substring(0, 2);
            Log.d(Constants.log + "time", "hour = " + time[0] + " | minutes = " + minutes);
            Date date = getDate(Integer.parseInt(time[0]), Integer.parseInt(minutes));
            long mills = date.getTime() - date1.getTime();
            Log.d(Constants.log + "Data1", "" + date1.getTime());
            Log.d(Constants.log + "Data", "" + date.getTime());
            hours = (int) (mills / (1000 * 60 * 60));
            mins = (int) (mills / (1000 * 60)) % 60;

            Log.d(Constants.log + "result", "h = " + hours + " m = " + mins);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//    String result = hours +" "+ getString(R.string.hours) +" "+ mins +" "+ getString(R.string.minits);
        return hours + ":" + mins;

    *//*String result = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      result =                                       // Text representing the value of our date-time object.
              LocalTime.parse(                                  // Class representing a time-of-day value without a date and without a time zone.
                      "03:30 AM" ,                                  // Your `String` input text.
                      DateTimeFormatter.ofPattern(                  // Define a formatting pattern to match your input text.
                              "hh:mm a" ,
                              Locale.US                              // `Locale` determines the human language and cultural norms used in localization. Needed here to translate the `AM` & `PM` value.
                      )                                             // Returns a `DateTimeFormatter` object.
              )                                                 // Return a `LocalTime` object.
                      .format( DateTimeFormatter.ofPattern("HH:mm") )   // Generate text in a specific format. Returns a `String` object.
              ;
    }*//*

    *//*int hours = 0;
    int mins = 0;
    try {
      Date date2 = new Date();
      String[] ampm = timeV.split(" ", 2);
      String[] time = ampm[0].split(":", 2);
      Log.d(Constants.log, "time0 = "+time[0]+" time1 = "+time[1]);
      String minutes = time[1].substring(0, 2);
      Log.d(Constants.log+"time", "hour = "+time[0]+" | minutes = "+minutes);
      Date date = getDate(Integer.parseInt(time[0]), Integer.parseInt(minutes));
      @SuppressLint("SimpleDateFormat") java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm");
      java.util.Date date1 = df.parse(time[0]+":"+time[1]);
//      java.util.Date date2 = df.parse("19:05");
      assert date1 != null;
      long diff = date.getTime() - date2.getTime();
      Log.d("baherr", getDate1(diff)+" time ="+timeV);
      hours = (int) (diff/(1000 * 60 * 60));
      mins = (int) (diff/(1000*60)) % 60;
    } catch (ParseException e) {
      e.printStackTrace();
    }*//*

//    return "";
    }*/
    private String getPearerTime(String timeV) {
        String[] ampm = timeV.split(" ", 2);
        String[] time1 = ampm[0].split(":", 2);
        if (time1[0].length() == 1){
            timeV = "0"+timeV.toUpperCase(Locale.US);
        }else{
            timeV = timeV.toUpperCase(Locale.US);
        }
        Date date1 = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.US);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date24 = null;
        try {
            date24 = parseFormat.parse(timeV);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date24 != null;
        String time24 = displayFormat.format(date24);
    /*String result = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      result =                                       // Text representing the value of our date-time object.
              LocalTime.parse(                                  // Class representing a time-of-day value without a date and without a time zone.
                      "03:30 AM" ,                                  // Your `String` input text.
                      DateTimeFormatter.ofPattern(                  // Define a formatting pattern to match your input text.
                              "hh:mm a" ,
                              Locale.US                              // `Locale` determines the human language and cultural norms used in localization. Needed here to translate the `AM` & `PM` value.
                      )                                             // Returns a `DateTimeFormatter` object.
              )                                                 // Return a `LocalTime` object.
                      .format( DateTimeFormatter.ofPattern("HH:mm") )   // Generate text in a specific format. Returns a `String` object.
              ;
    }*/
//    String[] ampm = result.split(" ", 2);
        assert time24 != null;
        String[] time = time24.split(":", 2);
        Log.d(Constants.log, "time0 = "+time[0]+" time1 = "+time[1]);
        String minutes = time[1].substring(0, 2);
        Log.d(Constants.log+"time", "hour = "+time[0]+" | minutes = "+minutes);
        Date date = getDate(Integer.parseInt(time[0]), Integer.parseInt(minutes));
        long mills =  date.getTime() - date1.getTime();
        Log.d(Constants.log+"Data1", ""+date1.getTime());
        Log.d(Constants.log+"Data", ""+date.getTime());
        int hours = (int) (mills/(1000 * 60 * 60));
        int mins = (int) (mills/(1000*60)) % 60;
    /*int hours = 0;
    int mins = 0;
    try {
      Date date2 = new Date();
      String[] ampm = timeV.split(" ", 2);
      String[] time = ampm[0].split(":", 2);
      Log.d(Constants.log, "time0 = "+time[0]+" time1 = "+time[1]);
      String minutes = time[1].substring(0, 2);
      Log.d(Constants.log+"time", "hour = "+time[0]+" | minutes = "+minutes);
      Date date = getDate(Integer.parseInt(time[0]), Integer.parseInt(minutes));
      @SuppressLint("SimpleDateFormat") java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm");
      java.util.Date date1 = df.parse(time[0]+":"+time[1]);
//      java.util.Date date2 = df.parse("19:05");
      assert date1 != null;
      long diff = date.getTime() - date2.getTime();
      Log.d("baherr", getDate1(diff)+" time ="+timeV);
      hours = (int) (diff/(1000 * 60 * 60));
      mins = (int) (diff/(1000*60)) % 60;
    } catch (ParseException e) {
      e.printStackTrace();
    }*/
        Log.d(Constants.log+"result", "h = "+hours+" m = "+mins);
//        String result = hours +" "+ getString(R.string.hours) +" "+ mins +" "+ getString(R.string.minits);
        return hours + ":" + mins;
//    return "";
    }

    private String getDate1(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm aa", cal).toString();
        return date;
    }

    @SuppressLint("WrongConstant")
    public static Date getDate(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
//    cal.set(Calendar.YEAR, 0);
//    cal.set(Calendar.MONTH, 0);
//    cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
    /*if (typeAmPm.equalsIgnoreCase("am"))
      cal.set(Calendar.AM, 0);
    else
      cal.set(Calendar.PM, 1);*/

        return cal.getTime();
    }

    private void setAlarmButtonText(TextView button, int index) {
        boolean isAlarmSet = AppSettings.getInstance(getActivity()).isAlarmSetFor(index);
        int isAlarmSetInt = isAlarmSet ? 0 : 1;
        String buttonText = getResources().getQuantityString(R.plurals.button_alarm, isAlarmSetInt);
        button.setText(buttonText);
        boolean isRamadanSet = AppSettings.getInstance(getActivity()).getBoolean(AppSettings.Key.IS_RAMADAN);
        mRamadanContainer.setVisibility(isRamadanSet ? View.VISIBLE : View.GONE);
    }

    private void setAlarmButtonClickListener(TextView alarm, int index) {
        alarm.setOnClickListener(new View.OnClickListener() {
            int index = 0;

            @Override
            public void onClick(View v) {
                AppSettings settings = AppSettings.getInstance(getActivity());
                settings.setLatFor(mIndex, mLastLocation.getLatitude());
                settings.setLngFor(mIndex, mLastLocation.getLongitude());
                Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
                intent.putExtra(EXTRA_ALARM_INDEX, index);
                startActivityForResult(intent, REQUEST_SET_ALARM);
            }

            public View.OnClickListener init(int index) {
                this.index = index;
                return this;
            }

        }.init(index));
    }

    public void setLocation(Location location) {
        mLastLocation = location;
        AppSettings.getInstance().setLatFor(mIndex, location.getLatitude());
        AppSettings.getInstance().setLngFor(mIndex, location.getLatitude());
        if (isAdded()) {
            init(getView());
        }
    }

    private void updateAlarmStatus() {
        setAlarmButtonText(mAlarm, mIndex);

        AppSettings settings = AppSettings.getInstance(getActivity());

        SalaatAlarmReceiver sar = new SalaatAlarmReceiver();
        boolean isAlarmSet = settings.isAlarmSetFor(mIndex);
        sar.cancelAlarm(getActivity());
        if (isAlarmSet) {
            sar.setAlarm(getActivity());
        }

        RamadanAlarmReceiver rar = new RamadanAlarmReceiver();
        boolean isRamadanAlarmSet = settings.getBoolean(AppSettings.Key.IS_RAMADAN);
        rar.cancelAlarm(getActivity());
        if (isRamadanAlarmSet) {
            rar.setAlarm(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SET_ALARM) {
            if (resultCode == Activity.RESULT_OK) {
                updateAlarmStatus();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
