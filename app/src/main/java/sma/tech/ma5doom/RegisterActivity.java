package sma.tech.ma5doom;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import sma.tech.ma5doom.cities.CitiesDialogFragment;
import sma.tech.ma5doom.cities.CityCallBack;
import sma.tech.ma5doom.model.cities.CitiesList;
import sma.tech.ma5doom.model.cities.City;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.Validate;
import sma.tech.ma5doom.networkController.model.login.User;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_user_name)
    EditText userName;

    @BindView(R.id.et_phone_number)
    EditText phoneNumber;


    @BindView(R.id.et_city)
    EditText city;

    @BindView(R.id.et_pass)
    EditText pass;

    @BindView(R.id.et_pass_confirm)
    EditText confirmPass;

    @BindView(R.id.deal)
    CheckBox acceptCondition;

    @BindView(R.id.btn_register)
    Button registerBtn;

    @BindView(R.id.et_email)
    EditText email;



    ProgressDialog progressDialog;

    SharedPrefManager prefManager;
    String ROLE;

    CitiesDialogFragment citiesDialogFragment;

    private String cityId;

    ArrayList<City> cities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        prefManager = new SharedPrefManager(this);
        ROLE =getIntent().getStringExtra("ROLE");
        userName.requestFocus();
        city.setFocusable(false);
        city.setClickable(true);
        loadCities();
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCitiesDialog();
            }
        });
       

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


    public void register(View view) {
        register();
    }

    private boolean register() {
        String name = userName.getText().toString();
        if (name == null || name.length() < 3) {
            userName.setError("من فضلك ادخل اسم صحيح");
            return false;
        }

        String phone = phoneNumber.getText().toString();
        if (phone == null || phone.length() < 10) {
            phoneNumber.setError("من فضلك ادخل رقم صحيح مكون من 10 ارقام");
            return false;
        }

        String cityName = city.getText().toString();
        if (cityName == null || cityName.length() < 1) {
            city.setError("من فضلك ادخل اسم مدينة صحيح");
            return false;
        }

        String mail = email.getText().toString();
        if (mail == null || !Validate.email(mail)) {
            email.setError("من فضلك ادخل بريد الكتروني صالح");
            return false;
        }

        final String password = pass.getText().toString();
        String confirm = confirmPass.getText().toString();

        if (password == null || password.trim().length() < 4) {
            pass.setError(" من فضلك ادخل رقم سري قوي");
            return false;
        }

        if (!password.equals(confirm)) {
            confirmPass.setError("تاكد من الرقم تطابق الرقم السري");
            return false;
        }

        if (!NetworkUtils.isNetworkConnected(this)) {
            Snackbar.make(findViewById(R.id.scrollView),"تاكد من الاتصال بالانترنت اولا واعد المحاول", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (!acceptCondition.isChecked()) {
            Snackbar.make(findViewById(R.id.scrollView),"من فضلك اقبل سياسة الشركة والشروط للتسجيل", Snackbar.LENGTH_SHORT).show();
            return false;
        }


        showLoading();

        try {
            Service.register(name, cityId, mail, phone, password, ROLE,this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    String message = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    Log.i("user", user.getStatus().toString());
                    if (user.getStatus()) {
                        prefManager.Logout();
                        user.getUser().setRemindMe(true);
                        user.getUser().setPassword(password);
                        prefManager.setUserDate(user);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("role", user.getUser().getRole());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();
                    } else if (user.getStatus()==false) {

                        try{
                            if (message != null)
                                Snackbar.make(findViewById(R.id.scrollView),message, Snackbar.LENGTH_SHORT).show();
                            else
                                Snackbar.make(findViewById(R.id.scrollView),"البريد الالكتروني او رقم الهاتف مسجل من قبل", Snackbar.LENGTH_SHORT).show();

                        }catch (Exception e){
                            Snackbar.make(findViewById(R.id.scrollView),"حدث خطا اثناء الاتصال حاول مره اخري", Snackbar.LENGTH_SHORT).show();

                        }


                    }

                    dismissLoading();
                }

                @Override
                public void error(Exception exc) {
                    Snackbar.make(findViewById(R.id.scrollView),"حدث خطا اثناء الاتصال حاول مره اخري", Snackbar.LENGTH_SHORT).show();

                    dismissLoading();
                }
            });

        }catch (Exception e){
            Snackbar.make(findViewById(R.id.scrollView),"حدث خطا اثناء الاتصال حاول مره اخري", Snackbar.LENGTH_SHORT).show();

        }
        return true;

    }


    public void showCitiesDialog() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        citiesDialogFragment = new CitiesDialogFragment();
        if (cities==null) {
            loadCities();
            return;
        }

        citiesDialogFragment.setCities(cities);

        citiesDialogFragment.setCallBack(new CityCallBack() {
            @Override
            public void onClick(City cit) {
                citiesDialogFragment.dismiss();
                city.setText(cit.getName());
                cityId=cit.getId();
            }
        });
        

        citiesDialogFragment.show(this.getSupportFragmentManager(), "");

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

    private void loadCities(){
        if (!NetworkUtils.isNetworkConnected(this)) {
            Snackbar.make(findViewById(R.id.scrollView),"تاكد من الاتصال بالانترنت اولا واعد المحاول", Snackbar.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        try {
            Service.getCities(this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    CitiesList citiesList = gson.fromJson(response , CitiesList.class);
                    if (citiesList.getStatus()){
                        cities = ((ArrayList<City>) citiesList.getCities());
                    }
                    else {
                                Snackbar.make(findViewById(R.id.scrollView),"حدث خطأ عند تحميل المدن , حاول مره اخري", Snackbar.LENGTH_SHORT).show();

                    }
                    dismissLoading();

                }

                @Override
                public void error(Exception exc) {
                    Snackbar.make(findViewById(R.id.scrollView),"حدث خطأ عند تحميل المدن , حاول مره اخري", Snackbar.LENGTH_SHORT).show();
                    dismissLoading();

                }
            });
        }catch (Exception e){
            Snackbar.make(findViewById(R.id.scrollView),"حدث خطأ عند تحميل المدن , حاول مره اخري", Snackbar.LENGTH_SHORT).show();

            dismissLoading();

        }
    }


}
