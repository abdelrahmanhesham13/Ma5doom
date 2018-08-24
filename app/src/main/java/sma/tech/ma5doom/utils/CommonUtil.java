package sma.tech.ma5doom.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sma.tech.ma5doom.R;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;


public class CommonUtil {

    public static boolean isALog = true;

    public static void onPrintLog(Object o) {
        if (isALog) {
            Log.e("Response >>>>", new Gson().toJson(o));
        }

    }


    public static void PrintLogE(String print) {
        // if (BuildConfig.DEBUG) {
//        Log.e(AppControlle.TAG, print);
        //  }
    }

    public static String getLanguage() {
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("English"))
           language="en";
        if (language.equals("العربية"))
            language="ar";

        return language;
    }

    // Playing notification sound
    public static void playSound(Context context , int Soundtype) {
        try {
            Uri alarmSound = null;
//            if (Soundtype == Constant.NotificationType.NotificationSound) {
//                alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                        + "://" + context.getPackageName() + "/raw/notification");
//            }else if (Soundtype == Constant.NotificationType.ChatSound){
//                alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                        + "://" + context.getPackageName() + "/raw/whatapp_new_message");
//            }
            Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestFocus(View view, Window window) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void ShareApp(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out Ay E3lan App at: https://play.google.tech/store/apps/details?id=tech.tatweer.kole3lan");
        sendIntent.setType("text/plain");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }

    public static void RateApp(AppCompatActivity context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.tech/store/apps/details?id=" + appPackageName)));
        }
    }

//    public static int handleException(Context context, Throwable t) {
//        if (t instanceof SocketTimeoutException) {
//            makeToast(context, R.string.time_out_error);
//            return R.string.time_out_error;
//        } else if (t instanceof UnknownHostException) {
//            makeToast(context, R.string.connection_error);
//            return R.string.connection_error;
//        } else if (t instanceof ConnectException) {
//            makeToast(context, R.string.connection_error);
//            return R.string.connection_error;
//        } else if (t instanceof NoRouteToHostException) {
//            makeToast(context, R.string.connection_error);
//            return R.string.connection_error;
//        } else if (t instanceof PortUnreachableException) {
//            makeToast(context, R.string.connection_error);
//            return R.string.connection_error;
//        } else if (t instanceof UnknownServiceException) {
//            makeToast(context, R.string.connection_error);
//            return R.string.connection_error;
//        } else if (t instanceof BindException) {
//            makeToast(context, R.string.connection_error);
//            return R.string.connection_error;
//        } else {
//            makeToast(context, Integer.parseInt(t.getLocalizedMessage()));
//            return R.string.connection_error;
//        }
//    }


//    public static void makeToast(Context context, int msgId) {
//        Toaster toaster = new Toaster(context);
//        toaster.makeToast(context.getString(msgId));
//
//    }




    public static void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static String getFormattedTime(String date) {
        Date parse = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        long updated = calendar.getTimeInMillis();
        return DateUtils.getRelativeTimeSpanString(updated, System.currentTimeMillis(), MINUTE_IN_MILLIS).toString();
    }

    public static void madeSnackBar(View layout, String message, Context context) {
        Snackbar snack = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        snack.show();
    }





//    public static void PrintResponseLog(Object o) {
//        if (BuildConfig.DEBUG) {
//            Log.e("aye3lan", "response :" + new Gson().toJson(o));
//        }
//    }


}
