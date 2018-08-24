package sma.tech.ma5doom.client.reservation;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.client.rest.RestDetailActivity;
import sma.tech.ma5doom.model.products.Product;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;

public class ClientFavoritReservationAdapter extends RecyclerView.Adapter<ClientFavoritReservationAdapter.RestViewHolder> {
    private Context mContext;
    private ArrayList<Product> data;
    SharedPrefManager sp;

    public ClientFavoritReservationAdapter(Context context) {
        mContext = context;
//        mContext.data=data;
        sp=new SharedPrefManager(mContext);
    }

    public void updateData(ArrayList<Product> data){
        this.data=data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ClientFavoritReservationAdapter.RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientFavoritReservationAdapter.RestViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientFavoritReservationAdapter.RestViewHolder holder, final int position) {
        if (data==null || data.size()==0)
            return;
        final Product rest = data.get(position);
        holder.name.setText(rest.getName());
        holder.distance.setText(25+" كيلو متر ");
        holder.rating_bar.setRating(Float.parseFloat(rest.getRate()+""));

        holder.btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(rest.getId() ,position);
            }
        });
        
        holder.itemView.setTag(rest.getId());
        try{
            Picasso.with(mContext).
                    load(rest.getImage()+"").
                    fit().
                    into(holder.restImage);
        }catch (Exception e){

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isNetworkConnected(mContext)) {
                    GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) mContext);
                    return ;
                }

                Intent intent = new Intent(mContext , RestDetailActivity.class);
                intent.putExtra("ID" ,data.get(position).getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }

    private void showDialog(final String PRODUCT_ID , final int postion){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        removeFromFavorite(PRODUCT_ID , postion);

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
        builder.setMessage("هل تريد حذف هذه الاستراحة من المفضلة ؟").setPositiveButton("موافق", dialogClickListener).setNegativeButton("الغاء",dialogClickListener).show();

    }


    public void removeFromFavorite(String PRODUCT_ID , final int postion) {
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) mContext);
            return ;
        }
        
        

        showLoading();
        try{
            String user_id = sp.getUserDate().getUser().getId();
            Service.addRestToFavorite(user_id, PRODUCT_ID, mContext, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try{
                        JSONObject root =new JSONObject(response);
                        String msg= root.getString("message");
                        boolean status = root.getBoolean("status");
                        if (status){
                            dismissLoading();
                            GMethods.showSnackBarMessage(msg,(AppCompatActivity) mContext);
                            data.remove(postion);
                            updateData(data);
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
                    dismissLoading();
                }
            });
        }catch (Exception e){
            dismissLoading();
            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity) mContext);
        }
    }

    ProgressDialog progressDialog;
    private void showLoading(){
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("جاري الحذف من الفضلة");
        progressDialog.show();
    }

    private void dismissLoading(){
        progressDialog.dismiss();
    }




    static class RestViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RatingBar rating_bar;
        public TextView distance;
        public ImageView restImage;
        public ImageButton btn_dislike;

        public RestViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_client_favorit_rservation, parent, false));
            name = (TextView) itemView.findViewById(R.id.rest_name);
            rating_bar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            distance = (TextView) itemView.findViewById(R.id.tv_distance);
            restImage =(ImageView) itemView.findViewById(R.id.iv_rest);
            btn_dislike=(ImageButton)itemView.findViewById(R.id.btn_dislike);

        }

    }

}
