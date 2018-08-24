package sma.tech.ma5doom.reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.RestViewHolder> {

    private Context mContext;
    private ArrayList<Product> data;
    private boolean current ;
    SharedPrefManager pm;
    OnDataChanged onDataChanged;
    public ReservationAdapter(Context context , ArrayList<Product> data, boolean current,OnDataChanged onDataChanged) {
        mContext = context;
        this.data=data;
        this.current=current;
        pm=new SharedPrefManager(context);
        progressDialog=new ProgressDialog(mContext);
        this.onDataChanged = onDataChanged;

    }

    @NonNull
    @Override
    public ReservationAdapter.RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReservationAdapter.RestViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.RestViewHolder holder, final int position) {
        Product product = data.get(position);
        holder.name.setText(product.getName());
        holder.clientName.setText(product.getUser());
        holder.date.setText(product.getDate());
        holder.duration.setText(product.getDuration()+"يوم");
        holder.time.setText(product.getTime());
//        Picasso.with(mContext).
//                load(product.getImage()).
//                placeholder(R.drawable.logo).
//                into(holder.iv_reservation);

        if (!current){
            holder.cancel.setVisibility(View.GONE);
            holder.accepet.setVisibility(View.GONE);
            }else {
            holder.cancel.setVisibility(View.VISIBLE);
            holder.accepet.setVisibility(View.VISIBLE);

        }

        int state =Integer.parseInt(product.getStatus());

        switch (state){
            case 0:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorOrange));
                holder.accepet.setVisibility(View.VISIBLE);
                holder.cancel.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
                holder.accepet.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                break;
            case 2:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                holder.accepet.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                break;
            case 3:
                holder.state.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                holder.accepet.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                break;

        }

        holder.state.setText(product.getType());


        holder.accepet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                try {
                    User_ user= pm.getUserDate().getUser();
                    String user_id=user.getId();
                    Service.acceptReservation(user_id, data.get(position).getId(), mContext
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
                                            onDataChanged.setOnDataChanged();
                                        }
                                        else {
                                            dismissLoading();
                                            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);

                                        }
                                    } catch (JSONException e) {
                                        dismissLoading();
                                        GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
                                    }
                                }

                                @Override
                                public void error(Exception exc) {
                                    GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
                                    dismissLoading();
                                }
                            });
                }catch (Exception e){
                    GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
                    dismissLoading();
                }
            }
        });


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
                                            showSnackBarMessage(msg,(AppCompatActivity) mContext);
                                            data.remove(position);
                                            changeData(data , current);
                                            onDataChanged.setOnDataChanged();
                                        }
                                        else {
                                            dismissLoading();
                                            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);

                                        }
                                    } catch (JSONException e) {
                                        dismissLoading();
                                        GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
                                    }
                                }

                                @Override
                                public void error(Exception exc) {
                                    GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
                                    dismissLoading();
                                }
                            });
                }catch (Exception e){
                    GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
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
        public TextView clientName;

        public TextView time;
        public TextView date;
        public TextView duration;
        public TextView state;
        public Button cancel , accepet;
        public ImageView iv_reservation;

        public RestViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.reservation_list_item, parent, false));
            name = (TextView) itemView.findViewById(R.id.rest_name);
            clientName = (TextView) itemView.findViewById(R.id.tv_client_name);

            date = (TextView) itemView.findViewById(R.id.tv_client_res_date);
            time = (TextView) itemView.findViewById(R.id.tv_client_res_time);
            duration = (TextView) itemView.findViewById(R.id.tv_client_res_duration);
            state = (TextView) itemView.findViewById(R.id.tv_client_res_state);
            cancel = (Button) itemView.findViewById(R.id.btn_cancel);
            accepet = (Button) itemView.findViewById(R.id.btn_confirm);

            iv_reservation=(ImageView)itemView.findViewById(R.id.iv_reservation);


        }

    }

    ProgressDialog progressDialog;
    private void showLoading(){
        progressDialog.setTitle("جاري  التحميل");
        progressDialog.show();
    }

    private void dismissLoading(){
        progressDialog.dismiss();
    }


    interface OnDataChanged{
        void setOnDataChanged();
    }

    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }

}
