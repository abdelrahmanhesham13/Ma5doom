package sma.tech.ma5doom.client.rate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.rate.Review;


public class RestRateAdapter extends RecyclerView.Adapter<RestRateAdapter.RateAdapter> {
    private Context mContext;
    private ArrayList<Review> data;
    public RestRateAdapter(Context context) {
        mContext = context;
//        this.data=data;

    }

    public void updateData(ArrayList<Review> data){
        this.data=data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RestRateAdapter.RateAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestRateAdapter.RateAdapter(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RestRateAdapter.RateAdapter holder, final int position) {
//        if (data==null || data.size()==0)
//            return;
        try{
            Review review = data.get(position);
            holder.name.setText(review.getUser());
            holder.rating_bar.setRating(Float.parseFloat(review.getReview()));
            holder.desc.setText(review.getComment());
        }catch (Exception e){

        }
//

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }



    static class RateAdapter extends RecyclerView.ViewHolder {
        public TextView name;
        public RatingBar rating_bar;
        public TextView desc;

        public RateAdapter(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list_rate, parent, false));
            name = (TextView) itemView.findViewById(R.id.tv_client_name);
            rating_bar = (RatingBar) itemView.findViewById(R.id.rb_rate);
            desc = (TextView) itemView.findViewById(R.id.tv_rate);

        }

    }

}
