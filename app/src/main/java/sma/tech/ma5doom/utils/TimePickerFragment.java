package sma.tech.ma5doom.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("ValidFragment")
public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    DataAndTimeClicked dataAndTime;

    public TimePickerFragment(DataAndTimeClicked dt){
        this.dataAndTime=dt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String hour= Integer.toString(hourOfDay) ;
        String min= Integer.toString(minute) ;

        if (hourOfDay<10)
            hour="0"+hour;
        if (minute<10)
            min="0"+min;



        dataAndTime.timeSelected(hour+":"+min);
    }

    public static String timeToTwelveHours(String time){
        String hoursMint[] =time.split(":");
        int hour =Integer.parseInt(hoursMint[0]);
        String formatedTime;
        if (hour>11){
            hour-=12;
            formatedTime = hour+":"+hoursMint[1]+" PM";

        }else{
            formatedTime = hour+":"+hoursMint[1]+" AM";
        }

        return formatedTime;
    }

    public static long dataTimeInMilis(String date ,String time){
        String dateAndTime = date+time;
        //parsing date into mills
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy HH:mm");
        long dt = System.currentTimeMillis();
        try {
            Date mDateTime = dateFormat.parse(dateAndTime);
            long dateTimeInMills = mDateTime.getTime();
            return dateTimeInMills;

        } catch (ParseException e) {
            e.printStackTrace();
            return 100;
        }
    }
}