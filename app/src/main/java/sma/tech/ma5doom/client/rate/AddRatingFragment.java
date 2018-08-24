package sma.tech.ma5doom.client.rate;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.client.reservation.Reservation;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;

public class AddRatingFragment extends AppCompatDialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button rate;
    String  product_id , comment ,review;
    TextView rest_name;
    EditText et_comment;
    RatingBar ratingBar;
    ImageView close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_ratting, container, false);
        rate = ( Button )v.findViewById(R.id.btn_rate);
        rest_name = (TextView)v.findViewById(R.id.rest_name);
        et_comment = (EditText) v.findViewById(R.id.et_comment);
        ratingBar = (RatingBar)v.findViewById(R.id.rating_bar);
        close = v.findViewById(R.id.close);


        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comm = et_comment.getText().toString();
                String rate = String.valueOf(ratingBar.getRating());
                if (rate.equals("0.0")){
                    rate = "0";
                }
                Log.i("Rate",rate);
                if (!comm.isEmpty()) {
                    addReview(comm, rate);
                } else {
                    if (getActivity()!= null){
                        showSnackBarMessage("من فضلك ادخل التعليق",(AppCompatActivity)getActivity());
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss_dialog();
            }
        });

        // Do all the stuff to initialize your custom view
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return v;
    }

    private void dismiss_dialog(){
        this.dismiss();
    }

    public void setProduct_id(String id){
        product_id=id;
    }

    private void addReview(String com , final String rat){
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            if (getActivity()!= null){
                showSnackBarMessage("تأكد من اتصالك بالانترنت ثم اعد المحاوله",(AppCompatActivity)getActivity());
            }
            return ;
        }

       try{
           Service.addRate(getContext(), product_id, com, rat, new RequestCallBack() {
               @Override
               public void success(String response) {
                   Gson gson = new Gson();
                   Reservation reservation = gson.fromJson(response , Reservation.class);
                   if (reservation.getStatus()){
                       if (getActivity()!= null){
                           showSnackBarMessage("تم التقييم بنجاح",(AppCompatActivity)getActivity());
                       }
                       dismiss_dialog();
                   }else {
                       if (getActivity()!= null){
                           showSnackBarMessage("عفوا لايمكنك اضافة تقييم بدون زيارة هذه الاستراحه",(AppCompatActivity)getActivity());
                       }
                       dismiss_dialog();
                   }
               }

               @Override
               public void error(Exception exc) {
                   dismiss_dialog();
               }
           });
       }catch (Exception e){
            dismiss_dialog();
       }
    }


    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }




}