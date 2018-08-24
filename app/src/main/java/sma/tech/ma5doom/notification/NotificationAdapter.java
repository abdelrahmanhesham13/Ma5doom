package sma.tech.ma5doom.notification;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.notification.Notification;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private String title = "تم حجز الاستراحة بواسطة";

    private Context mContext;
    private ArrayList<Notification> data;
    public NotificationAdapter(Context context ) {
        mContext = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {
        if (data==null || data.size()==0)
            return;
        Notification rest = data.get(position);
        holder.name.setText(rest.getName());
        holder.content.setText(rest.getTitle());
        holder.date.setText(rest.getDate());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id= data.get(position).getId();
                showDialog(id,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }

    public void changeData(ArrayList<Notification> newData) {
        data=newData;
        notifyDataSetChanged();

    }


    private void showDialog(final String id , final int postion){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        removeFromNotification(id , postion);

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("هل تريد حذف هذا التنبيه ؟").setPositiveButton("موافق", dialogClickListener).setNegativeButton("الغاء",dialogClickListener).show();

    }


    public void removeFromNotification(String id , final int postion) {
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاولة",(AppCompatActivity)mContext);
            return ;
        }



        showLoading();
        try{
            Service.deleteNotification(id, mContext, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try{
                        JSONObject root =new JSONObject(response);
                        String msg= root.getString("message");
                        boolean status = root.getBoolean("status");
                        if (status){
                            dismissLoading();
                            showSnackBarMessage(msg,(AppCompatActivity)mContext);
                            data.remove(postion);
                            changeData(data);
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

                }
            });
        }catch (Exception e){
            dismissLoading();
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)mContext);
        }
    }

    ProgressDialog progressDialog;
    private void showLoading(){
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("جاري الحذف");
        progressDialog.show();
    }

    private void dismissLoading(){
        progressDialog.dismiss();
    }




    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView content;
        public TextView date;
        ImageView btn_delete;

        public NotificationViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));
            name = (TextView) itemView.findViewById(R.id.rest_name);
            content = (TextView) itemView.findViewById(R.id.tv_list_content);
            date = (TextView) itemView.findViewById(R.id.client_reservation_date);
            btn_delete=(ImageView)itemView.findViewById(R.id.btn_delete);


        }

    }


    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }

}
