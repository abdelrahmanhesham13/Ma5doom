package sma.tech.ma5doom.notification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.notification.Notification;
import sma.tech.ma5doom.model.notification.NotificationBase;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;

public class NotificationActivity extends AppCompatActivity {

    NotificationAdapter adapter;
    ProgressDialog progressDialog;
    SharedPrefManager prefManager;
    String response;
    User_ user;
    ArrayList<Notification> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        prefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        user = prefManager.getUserDate().getUser();


        Intent intent = getIntent();
        if (intent.hasExtra("res")) {
            response = intent.getStringExtra("res");
        } else {
            loadNotification();
        }




        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new NotificationAdapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        if (intent.hasExtra("res")) {
            load();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().
                updateConfiguration(config, getApplicationContext().
                        getResources().getDisplayMetrics());
    }


    private void load() {


        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاولة",NotificationActivity.this);

            return;
        }

        showLoading();

        try {
            Gson gson = new Gson();
            NotificationBase notificationBase = gson.fromJson(response, NotificationBase.class);
            list = (ArrayList<Notification>) notificationBase.getNotifications();
            adapter.changeData(list);
            dismissLoading();

        } catch (Exception e) {
            GMethods.showSnackBarMessage("حدث خطأ من فضلك اعد المحاوله",NotificationActivity.this);

        }

    }

    private void showLoading() {
        progressDialog.setMessage("جاري التحميل");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    private void dismissLoading() {
        progressDialog.dismiss();
    }


    public void finishActivity(View v) {
        finish();
    }


    private void loadNotification() {

        progressDialog = new ProgressDialog(this);

        showLoading();

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاولة",NotificationActivity.this);
            dismissLoading();
            return;
        }

        try {

            Service.getNotifications(user.getId(), this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        NotificationBase noti = gson.fromJson(response, NotificationBase.class);
                        Log.i("user", noti.getStatus().toString());

                        if (noti.getStatus()) {
                            NotificationActivity.this.response = response;
                            load();
                        } else {
                        }


                        dismissLoading();
                    } catch (Exception e) {
                        dismissLoading();
                    }
                }

                @Override
                public void error(Exception exc) {
                    GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاولة",NotificationActivity.this);

                }
            });

        } catch (Exception e) {
            GMethods.showSnackBarMessage("حدث خطأ من فضلك حاول مره اخري",NotificationActivity.this);

        }

    }

}

