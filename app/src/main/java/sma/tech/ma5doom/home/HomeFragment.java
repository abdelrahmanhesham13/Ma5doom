package sma.tech.ma5doom.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.notification.Notification;
import sma.tech.ma5doom.model.notification.NotificationBase;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.notification.NotificationActivity;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.rest.AddRestActivity;
import sma.tech.ma5doom.utils.NetworkUtils;

public class HomeFragment extends Fragment {

    ImageView addRest , myReservation , rests , notification;


    private  HomeCallBack callBack;
    SharedPrefManager prefManager;
    User_ user ;
    ProgressDialog progressDialog;
    String notiResponse="";
    TextView tv_notification_num;
    int notificationCount=0;
    ArrayList<Notification> notificationsList;
    public void setCallBack(HomeCallBack callBack){
        this.callBack=callBack;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        addRest = (ImageView)v.findViewById(R.id.img_add_rest);
        addRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) {
                addRest();
            }
        });
        progressDialog = new ProgressDialog(getContext());
        prefManager=new SharedPrefManager(getContext());
        user = prefManager.getUserDate().getUser();
        tv_notification_num=(TextView)v.findViewById(R.id.tv_notification_num);



        myReservation = (ImageView)v.findViewById(R.id.img_reservation);
        myReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) {
                callBack.myReservationsClicked();
            }
        });


        rests = (ImageView)v.findViewById(R.id.img_myrest);
        rests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) {
                callBack.restClicked();
            }
        });


        notification = (ImageView)v.findViewById(R.id.img_notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) {
                if (notificationCount>0){
                    Intent intent =new Intent(getContext() , NotificationActivity.class);
                    intent.putExtra("res",notiResponse);
                    getActivity().startActivity(intent);

                } else {
                    if (getActivity() != null) {
                        showSnackBarMessage("ليس لديك تنبيهات الان", (AppCompatActivity) getActivity());
                    }
                }
            }
        });

//        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recyclerview);
//        ReservationAdapter adapter = new ReservationAdapter(getContext(), FakeData.restFakeData() ,true);
//        rv.setAdapter(adapter);
//        rv.setHasFixedSize(true);
        load();
//        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;

    }



    private void addRest() {
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",(AppCompatActivity) getActivity());
            dismissLoading();
            return ;
        }
        startActivity(new Intent(getContext() , AddRestActivity.class));
    }
//
//
//    public void myReservation(View view) {
//        startActivity(new Intent(this , ReservationActivity.class));
//
//    }
//
//    public void showNotification(View view) {
//        startActivity(new Intent(this , NotificationActivity.class));
//
//    }
//
//
//    public void showRests(View view) {
//        startActivity(new Intent(this , RestActivity.class));
//
//
//    }

    private void updateNumber(int num){
        notificationCount=num;
        tv_notification_num.setText(num+"");
    }


    private void load(){


        showLoading();

        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",(AppCompatActivity) getActivity());
            dismissLoading();
            return ;
        }

        try {

            Service.getNotifications(user.getId(),getContext(), new RequestCallBack() {
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


                        dismissLoading();
                    }catch (Exception e){
                        dismissLoading();
                    }
                }

                @Override
                public void error(Exception exc) {
                    GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity) getActivity());

                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity) getActivity());

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


    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }

}

