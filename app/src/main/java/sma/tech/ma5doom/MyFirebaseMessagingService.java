package sma.tech.ma5doom;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            Map<String,String> notificationMessage = remoteMessage.getData();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "0");
            mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            //mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
            //   R.drawable.im_logo));
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(notificationMessage.get("body")));
            mBuilder.setContentTitle(notificationMessage.get("title"));
            mBuilder.setContentText(notificationMessage.get("body"));
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            int color = ContextCompat.getColor(this, android.R.color.white);
            mBuilder.setColor(color);
            Intent resultIntent = new Intent(this, SplashActivity.class);

            SharedPrefManager prefManager=new SharedPrefManager(this);
            User_ user = prefManager.getUserDate().getUser();

            if (user != null) {

                if (notificationMessage.containsKey("targetScreen")) {

                    if (notificationMessage.get("targetScreen").equals("notifications")) {
                        resultIntent = new Intent(this, MainActivity.class).putExtra("goToReservations", true).putExtra("role", user.getRole());
                    } else {
                        resultIntent = new Intent(this, MainActivity.class).putExtra("role", user.getRole());
                    }
                }
            }




            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SplashActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int random = (int)System.currentTimeMillis();
            if (mNotificationManager != null) {
                mNotificationManager.notify(random, mBuilder.build());
            }

    }


    /*@Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",s);
        Log.i("token",s);
        editor.apply();
    }*/
}
