package sma.tech.ma5doom.client.reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.client.reservation.Product;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;

public class ClientReservatinAdapter extends RecyclerView.Adapter<ClientReservatinAdapter.RestViewHolder> {


    private Context mContext;
    private ArrayList<Product> data;
    SharedPrefManager pm;

    private boolean current ;
    public ClientReservatinAdapter(Context context , ArrayList<Product> data, boolean current) {
        mContext = context;
        this.data=data;
        pm=new SharedPrefManager(mContext);
        this.current=current;

    }

    @NonNull
    @Override
    public ClientReservatinAdapter.RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientReservatinAdapter.RestViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientReservatinAdapter.RestViewHolder holder, final int position) {
        Product product = data.get(position);
        holder.name.setText(product.getName());
        holder.date.setText(product.getDate());
        holder.duration.setText(product.getDuration()+"يوم");
        holder.time.setText(product.getTime());
        holder.price.setText(product.getPrice()+" ريال ");
        holder.arbon.setText(String.valueOf((Double.valueOf(product.getPrice())*10/100.0)) + " ريال");
        Picasso.with(mContext).
                load(product.getImage()).
                placeholder(R.drawable.logo).
                fit().
                into(holder.iv_reservation);

        int state =Integer.parseInt(product.getStatus());
        int colorId;

        if (!current){
            holder.cancel.setVisibility(View.GONE);
        }else {
            holder.cancel.setVisibility(View.VISIBLE);
        }

        switch (state){
            case 0:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorOrange));
                holder.state.setOnClickListener(null);
                holder.cancel.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
                holder.state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar
                            );
                        } else {
                            builder = new AlertDialog.Builder(mContext);
                        }
                        builder.setMessage("يرجي دفع مبلغ العربون (10%) من تكلفة الحجز علي احد حسابات التطبيق البنكيه خلال ساعتين والا سيتم الغاء حجزك تلقائيا")
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
                holder.cancel.setVisibility(View.GONE);
                break;
            case 2:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                holder.state.setOnClickListener(null);
                holder.cancel.setVisibility(View.GONE);
                break;
            case 3:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                holder.state.setOnClickListener(null);
                holder.cancel.setVisibility(View.GONE);
                break;

        }





        holder.state.setText(product.getType());



        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                try {
                    User_ user= pm.getUserDate().getUser();
                    String user_id=user.getId();
                    Service.cancelReservation(user_id, data.get(position).getId(), mContext
                            , new RequestCallBack() {
                                @Override
                                public void success(String response) {
                                    try{
                                        JSONObject root =new JSONObject(response);
                                        String msg= root.getString("message");
                                        boolean status = root.getBoolean("status");
                                        if (status){
                                            dismissLoading();
                                            showSnackBarMessage(msg,(AppCompatActivity)mContext);
                                            data.remove(position);
                                            changeData(data , current);
                                        }
                                        else {
                                            dismissLoading();
                                            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity) mContext);

                                        }
                                    } catch (JSONException e) {
                                        dismissLoading();
                                        GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity) mContext);
                                    }
                                }

                                @Override
                                public void error(Exception exc) {
                                    GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity) mContext);
                                    dismissLoading();
                                }
                            });
                }catch (Exception e){
                    GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity) mContext);
                    dismissLoading();
                }
            }
        });
//

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }

    public void changeData(ArrayList<Product> newData , boolean iscurrent) {
        data=newData;
        current=iscurrent;
        notifyDataSetChanged();
    }

    static class RestViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView time;
        public TextView date;
        public TextView duration , price;
        public TextView state;
        public Button cancel;
        TextView arbon;
        public ImageView iv_reservation;

        public RestViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_client_reservation, parent, false));
            name = (TextView) itemView.findViewById(R.id.rest_name);
            date = (TextView) itemView.findViewById(R.id.tv_client_res_date);
            time = (TextView) itemView.findViewById(R.id.tv_client_res_time);
            duration = (TextView) itemView.findViewById(R.id.tv_client_res_duration);
            state = (TextView) itemView.findViewById(R.id.tv_client_res_state);
            price=(TextView)  itemView.findViewById(R.id.tv_client_res_salary);
            cancel = (Button) itemView.findViewById(R.id.btn_cancel);
            arbon = itemView.findViewById(R.id.tv_client_res_salary2);
            iv_reservation=(ImageView)itemView.findViewById(R.id.iv_reservation);


        }

    }

    ProgressDialog progressDialog;
    private void showLoading(){
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("جاري الغاء الحجز");
        progressDialog.show();
    }

    private void dismissLoading(){
        progressDialog.dismiss();
    }


    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }



}
