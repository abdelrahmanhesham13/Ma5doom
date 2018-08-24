package sma.tech.ma5doom.rest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.products.Product;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;

public class RestAdapter extends RecyclerView.Adapter<RestAdapter.RestViewHolder> {
    private Context mContext;
    private ArrayList<Product> data;
    SharedPrefManager sp;
    private boolean current ;
    public RestAdapter(Context context) {
        mContext = context;
//        this.data=data;
        this.current=current;
        sp=new SharedPrefManager(mContext);
        progressDialog=new ProgressDialog(mContext);

    }

    public void updateData(ArrayList<Product> data){
        this.data=data;
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public RestAdapter.RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestAdapter.RestViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RestAdapter.RestViewHolder holder, final int position) {
        if (data==null || data.size()==0)
            return;

        try{
            final Product rest = data.get(position);
            holder.name.setText(rest.getName());
            holder.rating_bar.setRating(Float.parseFloat(rest.getRate()+""));
            holder.itemView.setTag(rest.getId());
            Picasso.with(mContext).
                    load(rest.getImage()).
                    placeholder(R.drawable.logo).
                    fit().
                    into(holder.restImage);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext , AddRestActivity.class);
                    intent.putExtra("is_edit",true);
                    intent.putExtra("product_id",data.get(position).getId());
                    mContext.startActivity(intent);
                }
            });


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(rest.getId() , position);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!NetworkUtils.isNetworkConnected(mContext)) {
//                    return ;
//                }
//
//                Intent intent = new Intent(mContext , RestDetailActivity.class);
//                intent.putExtra("ID" ,data.get(position).getId());
//                mContext.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }



    static class RestViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RatingBar rating_bar;
        public Button edit , delete;
        public ImageView restImage;

        public RestViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_rest, parent, false));
            name = (TextView) itemView.findViewById(R.id.rest_name);
            rating_bar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            restImage=(ImageView)itemView.findViewById(R.id.iv_rest) ;
            edit = (Button)itemView.findViewById(R.id.btn_edit);
            delete = (Button)itemView.findViewById(R.id.btn_delete);


        }

    }


    private void showDialog(final String rest_id , final int postion){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        deleteRest(rest_id , postion);
                        break;
                    default:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("هل تريد حذف هذه الاستراحة؟").setPositiveButton("موافق", dialogClickListener).setNegativeButton("الغاء",dialogClickListener).show();

    }







    public void deleteRest(String PRODUCT_ID , final int postion) {
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",(AppCompatActivity)mContext);
            return ;
        }



        showLoading();
        try{
            String user_id = sp.getUserDate().getUser().getId();
            Service.deleteRest(user_id, PRODUCT_ID, mContext, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try{
                        JSONObject root =new JSONObject(response);
                        String msg= root.getString("message");
                        boolean status = root.getBoolean("status");
                        if (status){
                            dismissLoading();
                            GMethods.showSnackBarMessage(msg,(AppCompatActivity)mContext);
                            data.remove(postion);
                            updateData(data);
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
        progressDialog.setTitle("جاري حذف الاستراحة");
        progressDialog.show();
    }

    private void dismissLoading(){
        progressDialog.dismiss();
    }

}
