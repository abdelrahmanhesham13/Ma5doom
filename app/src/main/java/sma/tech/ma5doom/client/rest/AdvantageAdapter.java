package sma.tech.ma5doom.client.rest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.products.details.Advantage;
import sma.tech.ma5doom.model.products.details.Type;

public class AdvantageAdapter extends RecyclerView.Adapter<AdvantageAdapter.MrafkViewHolder> {


    private Context mContext;
    private ArrayList<Type> dataType;
    private ArrayList<Advantage> dataAdvantages;
    int type; //0 for mrafk 1 for advantages
    String BASE_URL;


    public AdvantageAdapter(Context context, ArrayList<Type> data,
                            ArrayList<Advantage> data2, int type) {
        mContext = context;
        this.dataType = data;
        this.type = type;
        this.dataAdvantages = data2;

    }

    public void setURL(String url){
        this.BASE_URL = url;
    }

    @NonNull
    @Override
    public MrafkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rest_detail_advantages, parent, false);
        return new AdvantageAdapter.MrafkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvantageAdapter.MrafkViewHolder holder, final int position) {

        Type typeObj = null;
        Advantage advantage = null;
        if (type == 0) {
            typeObj = dataType.get(position);

        } else advantage = dataAdvantages.get(position);

        holder.name.setText((type == 0 ? typeObj.getName() : advantage.getName()));
        Picasso.with(mContext).
                load((type == 0 ? typeObj.getImage() : advantage.getImage())).
                into(holder.imgSrc);

        if (typeObj != null){
            Log.i("Image",typeObj.getImage());
        }

//        (type == 0 ? typeObj.getImage() : advantage.getImage())
    }

    @Override
    public int getItemCount() {
        if (type == 0) {
            if (dataType != null)
                return dataType.size();
        } else {
            if (dataAdvantages != null)
                return dataAdvantages.size();
        }

        return 0;
    }

    public void changeData(ArrayList<Type> newData , ArrayList<Advantage> newAdvant ) {
        dataType = newData;
        dataAdvantages=newAdvant;
        notifyDataSetChanged();
    }


    static class MrafkViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView imgSrc;

        public MrafkViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            imgSrc = (ImageView) itemView.findViewById(R.id.iv_src);


        }

    }

}
