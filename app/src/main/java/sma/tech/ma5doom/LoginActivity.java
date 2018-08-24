package sma.tech.ma5doom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.Locale;

import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_phone_number)
    EditText phoneNumber;

    @BindView(R.id.et_pass)
    EditText pass;

//    @BindView(R.id.rember_me)
//    CheckBox remberMe;

    ProgressDialog progressDialog;

    SharedPrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
//        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        dismissLoading();
        prefManager=new SharedPrefManager(this);
        phoneNumber.requestFocus();
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
        Intent intent =new Intent(this, StartupActivity.class);
        startActivity(intent);

    }

    public void forgetPass(View view) {
        startActivity(new Intent(this, ForgetpasswordActivity.class));

    }

    public void login(View view) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            Snackbar.make(findViewById(R.id.scrollView),"تاكد من الاتصال بالانترنت وحاول مرة اخري", Snackbar.LENGTH_SHORT).show();
            return ;
        }
//        finish();
        String mobile = phoneNumber.getText().toString();
        if (mobile == null || mobile.length() < 4 || mobile.trim().length() < 2) {
            phoneNumber.setError("ادخل رقم صحيح");

            return;
        }
        final String password = pass.getText().toString();

        if (password == null || password.length() < 2 || mobile.trim().length() < 1) {
            pass.setError("ادخل رقم سري جيد");
            return;
        }

        showLoading();

       try{

           Service.login(mobile, password, this, new RequestCallBack() {
               @Override
               public void success(String response) {
                   Gson gson = new Gson();
                   User user = gson.fromJson(response, User.class);
                   Log.i("user", user.getStatus().toString());
                   if (user.getStatus()){
                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       intent.putExtra("role",user.getUser().getRole());
                       startActivity(intent);
                       prefManager.Logout();
                       user.getUser().setRemindMe(true);
                       user.getUser().setPassword(password);
                       prefManager.setUserDate(user);
                       dismissLoading();
                       finish();
                   }
                   else{
                       dismissLoading();
                       Snackbar.make(findViewById(R.id.scrollView),"من فضلك تاكد من بياناتك", Snackbar.LENGTH_SHORT).show();

                   }

               }

               @Override
               public void error(Exception exc) {
                   dismissLoading();
               }
           });

       }catch (Exception e){
           dismissLoading();
           Snackbar.make(findViewById(R.id.scrollView),"حدث خطأ حاول مرة اخري", Snackbar.LENGTH_SHORT).show();
           }
    }

    ProgressBar progressBar;

    private void showLoading() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        setProgressBarIndeterminateVisibility(true);

//        progressBar.setVisibility(View.VISIBLE);

    }

    private void dismissLoading() {
        progressDialog.dismiss();
//        setProgressBarIndeterminateVisibility(false);


//        progressBar.setVisibility(View.INVISIBLE);


    }

}
