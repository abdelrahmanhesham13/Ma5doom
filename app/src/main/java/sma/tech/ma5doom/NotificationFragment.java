package sma.tech.ma5doom;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import sma.tech.ma5doom.model.notification.Notification;
import sma.tech.ma5doom.model.notification.NotificationBase;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.notification.NotificationAdapter;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    NotificationAdapter adapter;
    ProgressDialog progressDialog;
    SharedPrefManager prefManager;
    String response;
    User_ user;
    ArrayList<Notification> list;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_notification, container, false);

        response=getArguments().getString("res");
        v.findViewById(R.id.toolbar_parent).setVisibility(View.GONE);
        //response = intent.getStringExtra("res");

        prefManager=new SharedPrefManager(getContext());
        progressDialog = new ProgressDialog(getContext());
        user=prefManager.getUserDate().getUser();


        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recyclerview);
        adapter = new NotificationAdapter(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        load();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().
                updateConfiguration(config, getActivity().
                        getResources().getDisplayMetrics());
    }


    private void load(){


        if (!NetworkUtils.isNetworkConnected(getContext())) {
           GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",(AppCompatActivity)getActivity());

            return ;
        }

        showLoading();

        try {
            Gson gson = new Gson();
            NotificationBase notificationBase = gson.fromJson(response,NotificationBase.class);
            list=(ArrayList<Notification>)notificationBase.getNotifications();
            adapter.changeData(list);
            dismissLoading();

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ اثناء الاتصال حاول مره اخري",(AppCompatActivity)getActivity());

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


}
