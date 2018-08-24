package sma.tech.ma5doom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetpasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        ButterKnife.bind(this);
        progressDialog =new ProgressDialog(this);
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

    public void verifyEmail(View view) {
        String sEmail = email.getText().toString();
        if (sEmail== null ||sEmail.trim().length()<8){
           GMethods.showSnackBarMessage("تأكد من البريد الالكتروني واعد المحاوله",this);
            return;
        }

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",this);

            return ;
        }

        try {
            showLoading();

            Service.foregetPassword(this, sEmail, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject root= new JSONObject(response);
                        boolean status = root.getBoolean("status");
                        if (status){
                            GMethods.showSnackBarMessage("تم ارسال رسالة التأكيد الي البريد الالكتروني",ForgetpasswordActivity.this);
                            startActivity(new Intent(getApplicationContext(), StartupActivity.class));
                        }
                        else {
                            GMethods.showSnackBarMessage("حدث خطأ تأكد من صحة بريدك الالكتروني",ForgetpasswordActivity.this);}
                    } catch (JSONException e) {
                        e.printStackTrace();
                        GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",ForgetpasswordActivity.this);
                        dismissLoading();
                    }
                    dismissLoading();
                }

                @Override
                public void error(Exception exc) {
                }
            });

        }catch (Exception e){
            dismissLoading();
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",ForgetpasswordActivity.this);

        }

        dismissLoading();
    }


    ProgressDialog progressDialog;
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
}
