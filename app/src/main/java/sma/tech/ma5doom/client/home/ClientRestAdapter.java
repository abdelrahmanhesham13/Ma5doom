package sma.tech.ma5doom.client.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.client.rest.RestDetailActivity;
import sma.tech.ma5doom.model.products.Product;
import sma.tech.ma5doom.utils.NetworkUtils;


public class ClientRestAdapter extends RecyclerView.Adapter<ClientRestAdapter.RestViewHolder> {
    private Context mContext;
    private ArrayList<Product> data;
    private boolean current;
    boolean sortByDist=false;

    public ClientRestAdapter(Context context) {
        mContext = context;
//        this.data=data;
        this.current = current;

    }

    public void updateData(ArrayList<Product> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ClientRestAdapter.RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientRestAdapter.RestViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientRestAdapter.RestViewHolder holder, final int position) {
        if (data == null || data.size() == 0)
            return;
        Product rest = data.get(position);


        holder.name.setText(rest.getName());
        try {
            String dist =String.format("%.02f", rest.getDistance());
            if (rest.getDistance() < 0.01) {
                holder.distance.setText("قريب جدا");
                if (Double.compare(rest.getDistance(),-1) == 0){
                    holder.distance.setText("قم بتفعيل خدمة الموقع لتحديد المسافه");
                }
            } else {
                holder.distance.setText(dist + " كيلو متر ");
            }
            holder.rating_bar.setRating(Float.parseFloat(rest.getRate() + ""));
            holder.itemView.setTag(rest.getId());
            Picasso.with(mContext).
                    load(rest.getImage()).
                    fit().
                    into(holder.restImage);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!NetworkUtils.isNetworkConnected(mContext)) {
                        GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) mContext);
                        return;
                    }

                    Intent intent = new Intent(mContext, RestDetailActivity.class);
                    intent.putExtra("ID", data.get(position).getId());
                    mContext.startActivity(intent);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();

        return 0;
    }


    static class RestViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RatingBar rating_bar;
        public TextView distance;
        public ImageView restImage;

        public RestViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_client_rest, parent, false));
            name = (TextView) itemView.findViewById(R.id.rest_name);
            rating_bar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            distance = (TextView) itemView.findViewById(R.id.tv_distance);
            restImage = (ImageView) itemView.findViewById(R.id.iv_rest);

        }

    }







}
