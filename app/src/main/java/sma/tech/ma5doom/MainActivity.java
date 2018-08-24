package sma.tech.ma5doom;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import sma.tech.ma5doom.client.home.ClientHomeFragment;
import sma.tech.ma5doom.client.reservation.ClientFavoriteReservationFragment;
import sma.tech.ma5doom.client.reservation.ClientReservationFragment;
import sma.tech.ma5doom.conditions.ConditionFragment;
import sma.tech.ma5doom.contact.ContactUsFragment;
import sma.tech.ma5doom.home.HomeCallBack;
import sma.tech.ma5doom.home.HomeFragment;
import sma.tech.ma5doom.model.notification.NotificationBase;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.reservation.ReservationFragment;
import sma.tech.ma5doom.rest.RestFragment;
import sma.tech.ma5doom.utils.NetworkUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeCallBack {

    private static final long MIN_TIME_BW_UPDATES =100000 ;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    Spinner spinner;
    Integer role = 0;
    boolean isHome = false;
    FragmentManager fragmentManager;
    NavigationView navigationView;
    TextView title;
    ProgressDialog progressDialog;
    User_ user ;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    SharedPrefManager prefManager;
    LocationManager mLocationManager;
    int notificationCount=0;
    String notiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        GMethods.ChangeFont(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        title = findViewById(R.id.title);
        mTitle = mDrawerTitle = getTitle();
        progressDialog = new ProgressDialog(this);
        prefManager=new SharedPrefManager(this);
        user = prefManager.getUserDate().getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        role = Integer.parseInt(intent.getStringExtra("role"));
        if (role == 1)
            hideItem();
        displayMain();

        prefManager = new SharedPrefManager(this);


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                overrideMenuFontsFonts(navigationView);
            }
        });




//
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private Location getLastKnownLocation() {

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (getIntent().hasExtra("goToReservations")){
            displayReservation();
            navigationView.setCheckedItem(R.id.nav_my_reservation);
            getIntent().removeExtra("goToReservations");
        } else {
            displayMain();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayMain();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!isHome) {
            displayMain();
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displayMain();
        } else if (id == R.id.nav_my_reservation) {
            displayReservation();

        } else if (id == R.id.nav_my_profile) {
            title.setText("تعديل الحساب");
            getSupportFragmentManager().beginTransaction().replace(R.id.content,new SettingsFragment()).commit();

        } else if (id == R.id.nav_bank_accounts) {

            title.setText("الحسابات البنكيه");
            getSupportFragmentManager().beginTransaction().replace(R.id.content,new BankAccountFragment()).commit();


        } else if (id == R.id.nav_condition) {
            displayConditionFragmetn();
        } else if (id == R.id.nav_contactus) {
            displayContactUsFragmetn();

        } else if (id == R.id.nav_logout) {
            finish();
            prefManager.Logout();
            startActivity(new Intent(this, SplashActivity.class));
        } else if (id == R.id.home) {
            displayMain();

        }else if (id == R.id.nav_my_favorite) {
            displayMyFavorite();
        } else if (id == R.id.nav_my_notifications){
            load();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle=title;
//        getActionBar().setTitle(title);
//
//    }

    private void displayClientHome() {
        isHome = true;
        navigationView.setCheckedItem(R.id.nav_home);
        title.setText("الرئيسيه");
        Location location=null;
        try{
            location =getLastKnownLocation();
        }catch (Exception e){
            e.printStackTrace();
        }

        ClientHomeFragment homeFragment = new ClientHomeFragment();
        homeFragment.setMyLocation(location);
        fragmentManager.beginTransaction().replace(R.id.content, homeFragment).commit();
        homeFragment.setCallBack(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);    }

    private void displayHome() {
        isHome = true;
        navigationView.setCheckedItem(R.id.nav_home);
        title.setText("الرئيسيه");
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.content, homeFragment).commit();
        homeFragment.setCallBack(this);

    }

    private void displayReservation() {
        isHome = false;

        title.setText("حجوزاتي");

        Fragment fragment;
        if (role==2){
            ClientReservationFragment reservationFragment = new ClientReservationFragment();
            reservationFragment.setCallBack(this);
            fragmentManager.beginTransaction().replace(R.id.content, reservationFragment).commit();
        }else {
            ReservationFragment reservationFragment = new ReservationFragment();
            reservationFragment.setCallBack(this);
            fragmentManager.beginTransaction().replace(R.id.content, reservationFragment).commit();
        }


    }


    private void displayMyResets() {
        isHome = false;
        RestFragment restFragment = new RestFragment();
        fragmentManager.beginTransaction().replace(R.id.content, restFragment).commit();
        restFragment.setCallBack(this);
        title.setText("استراحاتي");

    }

    private void displayMyFavorite(){
        isHome = false;
        ClientFavoriteReservationFragment restFragment = new ClientFavoriteReservationFragment();
        fragmentManager.beginTransaction().replace(R.id.content, restFragment).commit();
        restFragment.setCallBack(this);
        title.setText("المفضله");
    }

    private void displayConditionFragmetn() {
        isHome = false;
        ConditionFragment conditionFragment = new ConditionFragment();
        fragmentManager.beginTransaction().replace(R.id.content, conditionFragment).commit();
        conditionFragment.setCallBack(this);
        title.setText("سياسة الشروط والاحكام");

    }

    private void displayContactUsFragmetn() {
        isHome = false;
        ContactUsFragment contactUsFragment = new ContactUsFragment();

        fragmentManager.beginTransaction().replace(R.id.content, contactUsFragment).commit();
        contactUsFragment.setCallBack(this);
        title.setText("اتصل بنا");

    }


    @Override
    public void myReservationsClicked() {
        navigationView.setCheckedItem(R.id.nav_my_reservation);
        navigationView.getMenu().performIdentifierAction(R.id.nav_my_reservation, 0);

    }

    @Override
    public void restClicked() {
        displayMyResets();
    }

    @Override
    public void onBack() {
        displayMain();
    }

    private void displayMain(){
        if (role == 1)
            displayHome();

        else displayClientHome();
    }


    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_my_favorite).setVisible(false);
        nav_Menu.findItem(R.id.nav_my_reservation).setVisible(false);
        nav_Menu.findItem(R.id.nav_my_notifications).setVisible(false);

    }






    //
    boolean canGetLocation=false;
//    public Location getLocation() {
//        try {
//            // getting GPS status
//            boolean isGPSEnabled = mLocationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // getting network status
//            boolean isNetworkEnabled = mLocationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // no network provider is enabled
//            } else {
//                this.canGetLocation = true;
//                Location location;
//                double latitude,longitude;
//                if (isNetworkEnabled) {
//                    mLocationManager.requestLocationUpdates(mLocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES,);
//
//
//
//                    Log.d("Network", "Network Enabled");
//                    if (mLocationManager != null) {
//                        location = mLocationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//                // if GPS Enabled get lat/long using GPS Services
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        mLocationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS", "GPS Enabled");
//                        if (mLocationManager != null) {
//                            location = mLocationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    public void overrideMenuFontsFonts(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    overrideMenuFontsFonts(vg.getChildAt(i));
                }
            } else if (v instanceof TextView) {
                CalligraphyUtils.applyFontToTextView(this, (TextView) v, "fonts/Droid-Arabic-Kufi.ttf");
            }
        } catch (Exception e) {
            //Log it, but ins't supposed to be here.
        }
    }


    private void load(){


        showLoading();

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",MainActivity.this);

            dismissLoading();
            return ;
        }

        try {

            Service.getNotifications(user.getId(),this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        NotificationBase noti = gson.fromJson(response, NotificationBase.class);
                        Log.i("user", noti.getStatus().toString());

                        if (noti.getStatus()){
                            updateNumber(noti.getCount());
                            notiResponse=response;
                        }else {
                            updateNumber(0);
                        }

                        if (notificationCount>0){
                            isHome = false;
                            NotificationFragment notificationFragment = new NotificationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("res",response);
                            notificationFragment.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(R.id.content, notificationFragment).commit();
                            title.setText("التنبيهات");
                        } else {
                            showSnackBarMessage("ليس لديك تنبيهات الان",MainActivity.this);
                        }


                        dismissLoading();
                    }catch (Exception e){
                        dismissLoading();
                    }
                }

                @Override
                public void error(Exception exc) {
                    GMethods.showSnackBarMessage("خطأ اثناء الاتصال حاول مره اخري",MainActivity.this);

                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("خطأ اثناء الاتصال حاول مره اخري",MainActivity.this);

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



    private void updateNumber(int num){
        notificationCount=num;
    }


    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }




}
