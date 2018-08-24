package sma.tech.ma5doom.bankAccounts;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.bankAccounts.model.Bank;
import sma.tech.ma5doom.bankAccounts.model.BankAccounts;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;
public class BankAccountActivity extends AppCompatActivity {

//    WebView myWebView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bank_account);
//        myWebView = (WebView) findViewById(R.id.webview);
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        loadData();
//    }
//
//
//    private void loadData(){
//        if (!NetworkUtils.isNetworkConnected(this)) {
//            return ;
//        }
//        myWebView.loadUrl("http://www.mazad-sa.net/ma5dom/api/webview?type=5");
//
//    }
//
//}


    BankAccountAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);


        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerview);
        adapter = new BankAccountAdapter(this);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        progressBar= findViewById(R.id.progressBar);
        progressDialog=new ProgressDialog(this);

        loadData();
    }


    private void loadData(){

        if (!NetworkUtils.isNetworkConnected(this)) {
           GMethods.showSnackBarMessage("تاكد من الاتصال بالانترنت اولا واعد المحاولة",BankAccountActivity.this);
            return ;
        }

        try {

            Service.getBanks(this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        BankAccounts bankAccounts = gson.fromJson(response , BankAccounts.class);
                        Log.i("status", bankAccounts.getStatus().toString());
                        if (bankAccounts.getStatus()){
                            updateData(bankAccounts);
                        }else {
                           GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",BankAccountActivity.this);
                        }

                        dismissLoading();
                    }catch (Exception e){
                        Log.e("EX" ,e.toString());
                        dismissLoading();
                        GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",BankAccountActivity.this);
                    }

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",BankAccountActivity.this);

        }

    }

    private void updateData(BankAccounts bankAccounts) {
        adapter.updateData((ArrayList< Bank>)bankAccounts.getBanks());
    }

//    ProgressBar progressBar;
    private void showLoading() {
        progressDialog.setMessage("جاري التحميل");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading() {
//        progressDialog.dismiss();
//        progressBar.setVisibility(View.INVISIBLE);

    }
}
