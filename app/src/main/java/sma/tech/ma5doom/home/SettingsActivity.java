package sma.tech.ma5doom.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.cities.CitiesDialogFragment;
import sma.tech.ma5doom.cities.CityCallBack;
import sma.tech.ma5doom.model.cities.CitiesList;
import sma.tech.ma5doom.model.cities.City;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.Validate;
import sma.tech.ma5doom.networkController.model.login.User;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {


    @BindView(R.id.tv_page_name)
    TextView pageName;

    @BindView(R.id.et_user_name)
    EditText userName;

    @BindView(R.id.et_phone_number)
    EditText phoneNumber;


    @BindView(R.id.et_pass)
    EditText pass;

    @BindView(R.id.et_pass_confirm)
    EditText confirmPass;

    @BindView(R.id.et_city)
    EditText city;

    @BindView(R.id.et_email)
    EditText email;

    @BindView(R.id.tv_cond)
            TextView mCond;
    @BindView(R.id.deal)
    CheckBox deal;

    @BindView(R.id.btn_register)
    Button register;

    CitiesDialogFragment citiesDialogFragment;

    private String cityId;


    ProgressDialog progressDialog;
    User_ user;

    SharedPrefManager prefManager;
    boolean isEdit;

    ArrayList<City> cities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        prefManager = new SharedPrefManager(this);
         user = prefManager.getUserDate().getUser();
        progressDialog=new ProgressDialog(this);

        loadCities();

        pageName.setText("اعدادات الحساب");

        userName.setText(user.getName());
        userName.setEnabled(false);
        phoneNumber.setText(user.getMobile());
        phoneNumber.setEnabled(false);
        confirmPass.setText(user.getPassword());
        confirmPass.setEnabled(false);
        confirmPass.setInputType(InputType.TYPE_CLASS_TEXT);
        pass.setText(user.getPassword());
        pass.setEnabled(false);
        pass.setInputType(InputType.TYPE_CLASS_TEXT);

        city.setEnabled(false);
        email.setText(user.getUsername());
        email.setEnabled(false);
        mCond.setVisibility(View.GONE);
        deal.setVisibility(View.GONE);
        register.setText("تعديل البيانات");
        register.setBackground(getResources().getDrawable(R.drawable.btn_orange));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit){

                    userName.setEnabled(true);
                    userName.requestFocus();
                    phoneNumber.setEnabled(true);
                    confirmPass.setEnabled(true);
                    pass.setEnabled(true);
                    city.setEnabled(true);
                    city.setFocusable(false);
                    city.setClickable(true);
                    email.setEnabled(true);
                    isEdit=true;
                    register.setText("حفظ");
                    register.setBackground(getResources().getDrawable(R.drawable.button_press));

                }else {
                    register();
                }
            }
        });


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
    private boolean register() {
        final String name = userName.getText().toString();
        if (name == null || name.length() < 3) {
            userName.setError("من فضلك ادخل اسم صحيح");
            return false;
        }

        final String phone = phoneNumber.getText().toString();
        if (phone == null || phone.length() < 4) {
            phoneNumber.setError("من فضلك ادخل رقم صحيح");
            return false;
        }

        final String cityName = city.getText().toString();
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
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",SettingsActivity.this);
            return false;
        }



        showLoading();

        try {
            Service.edit(user.getId() , name, cityId, mail, phone, password,this, new RequestCallBack() {
                @Override
                public void success(String response) {
                   try {


                       if (response.contains("true")) {
                           User user = prefManager.getUserDate();
                           User_ user_ =user.getUser();
                           user_.setName(name);
                           user_.setMobile(phone);
                           user_.setCityId(cityName);
                           user_.setUsername(email.getText().toString());
                           user_.setPassword(password);
                           prefManager.Logout();
                           prefManager.setUserDate(user);
                           Log.i("user", user.getStatus().toString());
                           GMethods.showSnackBarMessage("تم تعديل بياناتك بنجاح",SettingsActivity.this);
                           finish();
                       } else {
                           GMethods.showSnackBarMessage("من فضلك تأكد من بياناتك",SettingsActivity.this);
                       }

                       dismissLoading();
                   }catch (Exception e){
                       dismissLoading();
                   }
                }

                @Override
                public void error(Exception exc) {
                    GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",SettingsActivity.this);

                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",SettingsActivity.this);

        }
        return true;

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
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",SettingsActivity.this);
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

                        String cityName ="";
                        for (int i = 0 ; i<cities.size() ; ++i){
                            String id=user.getCityId();
                            if (id.equals(cities.get(i).getId())) {
                                cityName = cities.get(i).getName();
                                city.setText(cityName);
                                break;
                            }
                        }
                    }
                    else {
                        GMethods.showSnackBarMessage("خطأ عند تحميل المدن حاول مره اخري",SettingsActivity.this);

                    }
                    dismissLoading();

                }

                @Override
                public void error(Exception exc) {
                    GMethods.showSnackBarMessage("خطأ عند تحميل المدن حاول مره اخري",SettingsActivity.this);

                    dismissLoading();

                }
            });
        }catch (Exception e){
            GMethods.showSnackBarMessage("خطأ عند تحميل المدن حاول مره اخري",SettingsActivity.this);

            dismissLoading();

        }
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


}

