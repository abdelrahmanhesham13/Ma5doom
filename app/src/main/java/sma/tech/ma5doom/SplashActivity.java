package sma.tech.ma5doom;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.Locale;

import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;

public class SplashActivity extends Activity implements Animation.AnimationListener {
    Animation animFadeIn;
    LinearLayout linearLayout;
    SharedPrefManager prefManager;


    final int  MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    //    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        prefManager = new PrefManager(this);
        prefManager = new SharedPrefManager(this);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }
        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        // set animation listener
        animFadeIn.setAnimationListener(this);
        // animation for image
        linearLayout = (LinearLayout) findViewById(R.id.layout_linear);
        // start the animation
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animFadeIn);
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

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //under Implementation
    }

    public void onAnimationEnd(Animation animation) {

        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            User user = prefManager.getUserDate();

            User_ user_=user!=null?user.getUser():null;


            if (user!=null&&(user_!=null && user_.isRemindMe())){
                Service.login(user_.getMobile(), user_.getPassword(), SplashActivity.this, new RequestCallBack() {
                    @Override
                    public void success(String response) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        finish();
                        Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                        intent.putExtra("role",user.getUser().getRole());
                        startActivity(intent);
                    }

                    @Override
                    public void error(Exception exc) {

                    }
                });

            }
            else {
                finish();
                Intent intent = new Intent(this , LoginActivity.class);
                startActivity(intent);
            }
        }



    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        //under Implementation
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    User user = prefManager.getUserDate();

                    User_ user_=user!=null?user.getUser():null;


                    if (user!=null&&(user_!=null && user_.isRemindMe())){
                        Service.login(user_.getMobile(), user_.getPassword(), SplashActivity.this, new RequestCallBack() {
                            @Override
                            public void success(String response) {
                                Gson gson = new Gson();
                                User user = gson.fromJson(response, User.class);
                                finish();
                                Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                                intent.putExtra("role",user.getUser().getRole());
                                startActivity(intent);
                            }

                            @Override
                            public void error(Exception exc) {

                            }
                        });
                    }
                    else {
                        finish();
                        Intent intent = new Intent(this , LoginActivity.class);
                        startActivity(intent);
                    }

                } else {
                    finish();
                }
                return;
            }

        }
    }

}