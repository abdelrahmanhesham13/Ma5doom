package sma.tech.ma5doom;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import sma.tech.ma5doom.bankAccounts.BankAccountAdapter;
import sma.tech.ma5doom.bankAccounts.model.Bank;
import sma.tech.ma5doom.bankAccounts.model.BankAccounts;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class BankAccountFragment extends Fragment {


    BankAccountAdapter adapter;
    ProgressDialog progressDialog;

    public BankAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bank_account, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recyclerview);
        adapter = new BankAccountAdapter(getContext());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        progressBar= findViewById(R.id.progressBar);
        progressDialog=new ProgressDialog(getContext());

        loadData();


        return v;
    }


    private void loadData(){

        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",(AppCompatActivity)getActivity());
            return ;
        }

        try {

            Service.getBanks(getContext(), new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        BankAccounts bankAccounts = gson.fromJson(response , BankAccounts.class);
                        Log.i("status", bankAccounts.getStatus().toString());
                        if (bankAccounts.getStatus()){
                            updateData(bankAccounts);
                        }else {
                            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)getActivity());

                        }

                        dismissLoading();
                    }catch (Exception e){
                        Log.e("EX" ,e.toString());
                        dismissLoading();
                        GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)getActivity());

                    }

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)getActivity());


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
